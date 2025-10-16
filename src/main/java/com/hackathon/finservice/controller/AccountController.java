package com.hackathon.finservice.controller;

import com.hackathon.finservice.dto.account.AccountCreateRequest;
import com.hackathon.finservice.dto.account.AccountDashboardResponse;
import com.hackathon.finservice.dto.account.UserDashboardResponse;
import com.hackathon.finservice.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/dashboard/user")
    public ResponseEntity<UserDashboardResponse> getUserDashboardResponse() {
        UserDashboardResponse userDashboardResponse = accountService.getUserDashboardResponse();
        return ResponseEntity.ok(userDashboardResponse);
    }

    @GetMapping("/dashboard/account")
    public ResponseEntity<AccountDashboardResponse> getAccountDashboardResponse() {
        AccountDashboardResponse accountDashboardResponse = accountService.getAccountDashboardResponse();
        return ResponseEntity.ok(accountDashboardResponse);
    }

    @GetMapping("/dashboard/account/{index}")
    public ResponseEntity<AccountDashboardResponse> getAccountByIndex(@PathVariable int index) {
        AccountDashboardResponse accountDashboardResponse = accountService.getAccountByIndex(index);
        return ResponseEntity.ok(accountDashboardResponse);
    }

    @PostMapping("/account/create")
    public ResponseEntity<String> createNewAccount(@RequestBody AccountCreateRequest accountCreateRequest) {
        accountService.createNewAccount(accountCreateRequest);
        return ResponseEntity.ok("New account added successfully for user");
    }
}
