package ch.kris.service;

import ch.kris.dto.AccountDto;
import ch.kris.dto.SpendBalanceDto;
import ch.kris.model.Account;
import ch.kris.model.AccountTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@ApplicationScoped
public class AccountService {
    private static final String ROOT_CREDITS = "ROOT_CREDITS";
    private static final String ENTROPY = "ENTROPY";

    private final List<Account> accounts = new ArrayList<>();
    private final List<AccountTransaction> transactions = new ArrayList<>();

    private long nextTransactionId = 4;

    public List<Account> findAllAccounts() {
        return accounts;
    }

    public Account findAccountByUid(Long uid) {
        return accounts.stream()
                .filter(account -> account.getUid().equals(uid))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Account with uid " + uid + " was not found."));
    }

    public Account createAccount(AccountDto accountDto) {
        Long uid = generateUniqueUid();
        Account account = new Account(uid, accountDto.getPlayerName(), 0, 0);
        accounts.add(account);
        return account;
    }

    public List<AccountTransaction> findAllTransactions() {
        return transactions;
    }

    public List<AccountTransaction> findTransactionsByUid(Long uid) {
        findAccountByUid(uid);
        return transactions.stream()
                .filter(transaction -> transaction.getUid().equals(uid))
                .toList();
    }

    public AccountTransaction creditRootCredits(Long uid, int amount, String reason) {
        if (amount <= 0) {
            throw new BadRequestException("Amount must be greater than 0.");
        }

        Account account = findAccountByUid(uid);
        int balanceAfter = account.getRootCredits() + amount;
        account.setRootCredits(balanceAfter);

        AccountTransaction transaction = new AccountTransaction(
                nextTransactionId++,
                uid,
                amount,
                ROOT_CREDITS,
                "CREDIT",
                reason,
                balanceAfter
        );
        transactions.add(transaction);
        return transaction;
    }

    public AccountTransaction spendBalance(SpendBalanceDto spendBalanceDto) {
        if (spendBalanceDto.getAmount() <= 0) {
            throw new BadRequestException("Amount must be greater than 0.");
        }

        Account account = findAccountByUid(spendBalanceDto.getUid());
        String currency = normalizeCurrency(spendBalanceDto.getCurrency());
        int balance = getBalance(account, currency);

        if (balance < spendBalanceDto.getAmount()) {
            throw new BadRequestException("Account balance is too low.");
        }

        int balanceAfter = balance - spendBalanceDto.getAmount();
        setBalance(account, currency, balanceAfter);
        AccountTransaction transaction = new AccountTransaction(
                nextTransactionId++,
                account.getUid(),
                spendBalanceDto.getAmount(),
                currency,
                "DEBIT",
                spendBalanceDto.getReason(),
                balanceAfter
        );
        transactions.add(transaction);
        return transaction;
    }

    private Long generateUniqueUid() {
        Long uid;
        do {
            uid = ThreadLocalRandom.current().nextLong(100000000L, 1000000000L);
        } while (uidExists(uid));
        return uid;
    }

    private boolean uidExists(Long uid) {
        return accounts.stream()
                .anyMatch(account -> account.getUid().equals(uid));
    }

    private String normalizeCurrency(String currency) {
        if (currency == null || currency.isBlank()) {
            return ROOT_CREDITS;
        }

        String normalizedCurrency = currency.trim().toUpperCase().replace("-", "_");
        if (ROOT_CREDITS.equals(normalizedCurrency) || ENTROPY.equals(normalizedCurrency)) {
            return normalizedCurrency;
        }

        throw new BadRequestException("Currency must be ROOT_CREDITS or ENTROPY.");
    }

    private int getBalance(Account account, String currency) {
        if (ROOT_CREDITS.equals(currency)) {
            return account.getRootCredits();
        }
        return account.getEntropy();
    }

    private void setBalance(Account account, String currency, int balance) {
        if (ROOT_CREDITS.equals(currency)) {
            account.setRootCredits(balance);
            return;
        }
        account.setEntropy(balance);
    }
}
