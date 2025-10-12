package com.hackathon.finservice.service;

import com.hackathon.finservice.data.entity.Account;
import com.hackathon.finservice.data.entity.User;
import com.hackathon.finservice.data.repository.AccountRepository;
import com.hackathon.finservice.data.repository.UserRepository;
import com.hackathon.finservice.dto.AccountCreateRequest;
import com.hackathon.finservice.dto.AccountDashboardResponse;
import com.hackathon.finservice.dto.UserDashboardResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public UserDashboardResponse getUserDashboardResponse() {
        User user = getAuthenticatedUser();

        Account main = getMainAccountFromAuthenticatedUser(user);

        return new UserDashboardResponse(
                user.getName(),
                user.getEmail(),
                main.getAccountNumber(),
                main.getAccountType(),
                user.getPassword()
        );
    }

    public AccountDashboardResponse getAccountDashboardResponse() {
        User user = getAuthenticatedUser();

        Account main = getMainAccountFromAuthenticatedUser(user);

        return new AccountDashboardResponse(
                main.getAccountNumber(),
                main.getBalance(),
                main.getAccountType()
        );
    }

    public AccountDashboardResponse getAccountByIndex(int index) {
        User user = getAuthenticatedUser();

        List<Account> userAccountsOrderedById = user.getAccounts().stream()
                .sorted(Comparator.comparing(Account::getId))
                .toList();

        if (index < 0 || index >= userAccountsOrderedById.size()) {
            throw new ResponseStatusException(BAD_REQUEST, "Account index out of range");
        }

        Account account = userAccountsOrderedById.get(index);
        return new AccountDashboardResponse(
                account.getAccountNumber(),
                account.getBalance(),
                account.getAccountType()
        );
    }

    public void createNewAccount(AccountCreateRequest accountCreateRequest) {
        User user = getAuthenticatedUser();
        user.getAccounts().stream()
                .filter(account -> account.getAccountNumber().equals(accountCreateRequest.accountNumber()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        BAD_REQUEST,
                        "Invalid main account number for the given identifier: " + user.getEmail()
                ));

        Account newAccount = new Account();
        newAccount.setAccountNumber(generateAccountNumber());
        newAccount.setAccountType(accountCreateRequest.accountType());
        newAccount.setBalance(BigDecimal.ZERO);
        newAccount.setUser(user);

        accountRepository.save(newAccount);
    }

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(
                BAD_REQUEST,
                "User not found for the given identifier: " + email
        ));
    }

    private Account getMainAccountFromAuthenticatedUser(User user) {
        return user.getAccounts().stream()
            .filter(account -> "Main".equalsIgnoreCase(account.getAccountType()))
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(
                    BAD_REQUEST,
                    "Main account not found for the given identifier: " + user.getEmail()
            ));
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6);
    }
}
