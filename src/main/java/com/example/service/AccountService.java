package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    
    public Account registerAccount(Account account) {
        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            throw new IllegalArgumentException("Invalid account details");
        }
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new IllegalStateException("Username already exists");
        }
        return accountRepository.save(account);
    }

    public Account validateLogin(String username, String password) {
        return accountRepository.findByUsername(username)
                                .filter(acc -> acc.getPassword().equals(password))
                                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
    }
}
