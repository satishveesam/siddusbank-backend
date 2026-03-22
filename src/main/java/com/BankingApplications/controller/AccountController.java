package com.BankingApplications.controller;

import com.BankingApplications.dto.AccountDto;
import com.BankingApplications.dto.TransferRequest;
import com.BankingApplications.entity.Transaction;
import com.BankingApplications.security.JwtUtil;
import com.BankingApplications.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "http://localhost:3000")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtUtil jwtUtil;

    private Long getUserIdFromToken(String token) {
        String username = jwtUtil.extractUsername(token.substring(7));
        // You should have a method to get userId from username
        // For now, we'll assume it's handled in service
        return null;
    }

    @GetMapping
    public ResponseEntity<?> getAllAccounts(@RequestHeader("Authorization") String token) {
        try {
            // Extract userId from token (you need to implement this)
            // For demonstration, using a temporary method
            Long userId = 1L; // Replace with actual user ID extraction
            List<AccountDto> accounts = accountService.getAccountsByUserId(userId);
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            Long userId = 1L; // Replace with actual user ID extraction
            AccountDto account = accountService.getAccountById(id, userId);
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountDto accountDto, @RequestHeader("Authorization") String token) {
        try {
            Long userId = 1L; // Replace with actual user ID extraction
            AccountDto createdAccount = accountService.createAccount(accountDto, userId);
            return ResponseEntity.ok(createdAccount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<?> deposit(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request, 
                                     @RequestHeader("Authorization") String token) {
        try {
            Long userId = 1L; // Replace with actual user ID extraction
            BigDecimal amount = request.get("amount");
            AccountDto account = accountService.deposit(id, userId, amount);
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request,
                                      @RequestHeader("Authorization") String token) {
        try {
            Long userId = 1L; // Replace with actual user ID extraction
            BigDecimal amount = request.get("amount");
            AccountDto account = accountService.withdraw(id, userId, amount);
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request, @RequestHeader("Authorization") String token) {
        try {
            Long userId = 1L; // Replace with actual user ID extraction
            accountService.transfer(request, userId);
            return ResponseEntity.ok(Map.of("message", "Transfer completed successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            Long userId = 1L; // Replace with actual user ID extraction
            accountService.deleteAccount(id, userId);
            return ResponseEntity.ok(Map.of("message", "Account deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<?> getTransactions(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            Long userId = 1L; // Replace with actual user ID extraction
            List<Transaction> transactions = accountService.getTransactions(id, userId);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}