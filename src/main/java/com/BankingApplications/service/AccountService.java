package com.BankingApplications.service;

import com.BankingApplications.dto.AccountDto;
import com.BankingApplications.dto.TransferRequest;
import com.BankingApplications.entity.Account;
import com.BankingApplications.entity.Transaction;
import com.BankingApplications.entity.User;
import com.BankingApplications.repository.AccountRepository;
import com.BankingApplications.repository.TransactionRepository;
import com.BankingApplications.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Get all accounts for a specific user
     */
    public List<AccountDto> getAccountsByUserId(Long userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        return accounts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * Get account by ID for a specific user
     */
    public AccountDto getAccountById(Long accountId, Long userId) {
        Account account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return convertToDto(account);
    }

    /**
     * Create a new account for a user
     */
    @Transactional
    public AccountDto createAccount(AccountDto accountDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Account account = new Account();
        account.setAccountType(accountDto.getAccountType());
        
        // Set currency to INR by default
        if (accountDto.getCurrency() != null && !accountDto.getCurrency().isEmpty()) {
            account.setCurrency(accountDto.getCurrency());
        } else {
            account.setCurrency("INR");
        }
        
        // Set initial balance
        if (accountDto.getBalance() != null) {
            account.setBalance(accountDto.getBalance());
        } else {
            account.setBalance(BigDecimal.ZERO);
        }
        
        account.setUser(user);
        account.setActive(true);
        account.setCreatedDate(LocalDateTime.now());
        account.setUpdatedDate(LocalDateTime.now());
        
        Account savedAccount = accountRepository.save(account);
        
        // Create initial transaction if deposit amount > 0
        if (accountDto.getBalance() != null && accountDto.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            Transaction transaction = new Transaction();
            transaction.setTransactionType("DEPOSIT");
            transaction.setAmount(accountDto.getBalance());
            transaction.setDescription("Initial deposit in INR");
            transaction.setAccount(savedAccount);
            transaction.setStatus("COMPLETED");
            transaction.setTransactionDate(LocalDateTime.now());
            transactionRepository.save(transaction);
        }
        
        return convertToDto(savedAccount);
    }

    /**
     * Deposit money into an account
     */
    @Transactional
    public AccountDto deposit(Long accountId, Long userId, BigDecimal amount) {
        Account account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be positive");
        }
        
        // Update balance
        account.setBalance(account.getBalance().add(amount));
        account.setUpdatedDate(LocalDateTime.now());
        Account updatedAccount = accountRepository.save(account);
        
        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setTransactionType("DEPOSIT");
        transaction.setAmount(amount);
        transaction.setDescription("Deposit to account");
        transaction.setAccount(account);
        transaction.setStatus("COMPLETED");
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);
        
        return convertToDto(updatedAccount);
    }

    /**
     * Withdraw money from an account
     */
    @Transactional
    public AccountDto withdraw(Long accountId, Long userId, BigDecimal amount) {
        Account account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be positive");
        }
        
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        
        // Update balance
        account.setBalance(account.getBalance().subtract(amount));
        account.setUpdatedDate(LocalDateTime.now());
        Account updatedAccount = accountRepository.save(account);
        
        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setTransactionType("WITHDRAWAL");
        transaction.setAmount(amount);
        transaction.setDescription("Withdrawal from account");
        transaction.setAccount(account);
        transaction.setStatus("COMPLETED");
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);
        
        return convertToDto(updatedAccount);
    }

    /**
     * Transfer money between accounts
     */
    @Transactional
    public void transfer(TransferRequest request, Long userId) {
        // Validate source account
        Account fromAccount = accountRepository.findByIdAndUserId(request.getFromAccountId(), userId)
                .orElseThrow(() -> new RuntimeException("Source account not found"));
        
        // Validate destination account
        Account toAccount = accountRepository.findById(request.getToAccountId())
                .orElseThrow(() -> new RuntimeException("Destination account not found"));
        
        // Validate amount
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be positive");
        }
        
        // Check sufficient balance
        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        
        // Perform transfer
        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));
        
        fromAccount.setUpdatedDate(LocalDateTime.now());
        toAccount.setUpdatedDate(LocalDateTime.now());
        
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        
        // Create debit transaction record
        Transaction debitTransaction = new Transaction();
        debitTransaction.setTransactionType("TRANSFER");
        debitTransaction.setAmount(request.getAmount());
        debitTransaction.setDescription("Transfer to account #" + request.getToAccountId());
        debitTransaction.setAccount(fromAccount);
        debitTransaction.setToAccountId(request.getToAccountId());
        debitTransaction.setStatus("COMPLETED");
        debitTransaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(debitTransaction);
        
        // Create credit transaction record
        Transaction creditTransaction = new Transaction();
        creditTransaction.setTransactionType("TRANSFER");
        creditTransaction.setAmount(request.getAmount());
        creditTransaction.setDescription("Transfer from account #" + request.getFromAccountId());
        creditTransaction.setAccount(toAccount);
        creditTransaction.setToAccountId(request.getFromAccountId());
        creditTransaction.setStatus("COMPLETED");
        creditTransaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(creditTransaction);
    }

    /**
     * Delete (deactivate) an account
     */
    @Transactional
    public void deleteAccount(Long accountId, Long userId) {
        Account account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        
        // Check if account has balance
        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new RuntimeException("Cannot delete account with positive balance. Please withdraw all funds first.");
        }
        
        // Soft delete - deactivate account
        account.setActive(false);
        account.setUpdatedDate(LocalDateTime.now());
        accountRepository.save(account);
    }

    /**
     * Get all transactions for an account
     */
    public List<Transaction> getTransactions(Long accountId, Long userId) {
        // Verify account belongs to user
        Account account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        
        return transactionRepository.findByAccountIdOrderByTransactionDateDesc(accountId);
    }

    /**
     * Get total balance for all accounts of a user
     */
    public BigDecimal getTotalBalance(Long userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        return accounts.stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Get active accounts count for a user
     */
    public long getActiveAccountsCount(Long userId) {
        return accountRepository.findByUserIdAndActiveTrue(userId).size();
    }

    /**
     * Update account currency (if needed)
     */
    @Transactional
    public AccountDto updateAccountCurrency(Long accountId, Long userId, String currency) {
        Account account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        
        account.setCurrency(currency);
        account.setUpdatedDate(LocalDateTime.now());
        Account updatedAccount = accountRepository.save(account);
        
        return convertToDto(updatedAccount);
    }

    /**
     * Convert entity to DTO
     */
    private AccountDto convertToDto(Account account) {
        AccountDto dto = new AccountDto();
        dto.setId(account.getId());
        dto.setAccountType(account.getAccountType());
        dto.setBalance(account.getBalance());
        dto.setCurrency(account.getCurrency() != null ? account.getCurrency() : "INR");
        dto.setActive(account.isActive());
        dto.setUserId(account.getUser().getId());
        dto.setCreatedDate(account.getCreatedDate() != null ? account.getCreatedDate().toString() : null);
        return dto;
    }
}