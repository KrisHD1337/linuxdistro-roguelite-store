package ch.kris.service;

import ch.kris.dto.GameAccountDto;
import ch.kris.dto.OrderDto;
import ch.kris.dto.SpendWalletDto;
import ch.kris.dto.StorePackageDto;
import ch.kris.model.GameAccount;
import ch.kris.model.StoreOrder;
import ch.kris.model.StorePackage;
import ch.kris.model.Wallet;
import ch.kris.model.WalletTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@ApplicationScoped
public class StoreService {
    private static final String ROOT_CREDITS = "ROOT_CREDITS";
    private static final String ENTROPY = "ENTROPY";

    private final List<StorePackage> packages = new ArrayList<>();
    private final List<GameAccount> accounts = new ArrayList<>();
    private final List<StoreOrder> orders = new ArrayList<>();
    private final List<WalletTransaction> transactions = new ArrayList<>();

    private long nextPackageId = 4;
    private long nextOrderId = 3;
    private long nextTransactionId = 4;

    public StoreService() {
        packages.add(new StorePackage(1L, "Starter Root-Credits", 500, 500, 50, true));
        packages.add(new StorePackage(2L, "Kernel Cache", 1200, 1200, 150, true));
        packages.add(new StorePackage(3L, "Root Vault", 2500, 2500, 500, false));

        accounts.add(new GameAccount(123456789L, "TuxRunner", new Wallet(123456789L, 2400, 25)));
        accounts.add(new GameAccount(987654321L, "DebianMage", new Wallet(987654321L, 900, 10)));

        orders.add(new StoreOrder(1L, 123456789L, 2L, "PAID", 1200, 2400, 1200, true));
        orders.add(new StoreOrder(2L, 987654321L, 1L, "PAID", 500, 1000, 500, true));

        transactions.add(new WalletTransaction(1L, 123456789L, 2400, ROOT_CREDITS, "CREDIT", "Order 1", 2400));
        transactions.add(new WalletTransaction(2L, 987654321L, 1000, ROOT_CREDITS, "CREDIT", "Order 2", 1000));
        transactions.add(new WalletTransaction(3L, 987654321L, 100, ROOT_CREDITS, "DEBIT", "Unlock distro skin", 900));
    }

    public List<StorePackage> findAllPackages() {
        return packages;
    }

    public StorePackage findPackageById(Long packageId) {
        return packages.stream()
                .filter(storePackage -> storePackage.getPackageId().equals(packageId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Package with id " + packageId + " was not found."));
    }

    public StorePackage createPackage(StorePackageDto storePackageDto) {
        StorePackage storePackage = createPackageFromDto(nextPackageId++, storePackageDto);
        packages.add(storePackage);
        return storePackage;
    }

    public StorePackage updatePackage(Long packageId, StorePackageDto storePackageDto) {
        StorePackage existingPackage = findPackageById(packageId);
        existingPackage.setName(storePackageDto.getName());
        existingPackage.setPriceChfCents(storePackageDto.getPriceChfCents());
        existingPackage.setCurrencyAmount(storePackageDto.getCurrencyAmount());
        existingPackage.setBonusAmount(storePackageDto.getBonusAmount());
        existingPackage.setActive(storePackageDto.isActive());
        return existingPackage;
    }

    public StorePackage deletePackage(Long packageId) {
        StorePackage storePackage = findPackageById(packageId);
        packages.remove(storePackage);
        return storePackage;
    }

    public List<GameAccount> findAllAccounts() {
        return accounts;
    }

    public GameAccount findAccountByUid(Long uid) {
        return accounts.stream()
                .filter(account -> account.getUid().equals(uid))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Account with uid " + uid + " was not found."));
    }

    public GameAccount createAccount(GameAccountDto gameAccountDto) {
        Long uid = generateUniqueUid();
        GameAccount account = new GameAccount(uid, gameAccountDto.getPlayerName(), new Wallet(uid, 0, 0));
        accounts.add(account);
        return account;
    }

    public List<StoreOrder> findAllOrders() {
        return orders;
    }

    public StoreOrder findOrderById(Long orderId) {
        return orders.stream()
                .filter(order -> order.getOrderId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Order with id " + orderId + " was not found."));
    }

    public StoreOrder createOrder(OrderDto orderDto) {
        GameAccount account = findAccountByUid(orderDto.getUid());
        StorePackage storePackage = findPackageById(orderDto.getPackageId());

        if (!storePackage.isActive()) {
            throw new BadRequestException("Package with id " + storePackage.getPackageId() + " is not active.");
        }

        boolean firstTimePurchase = isFirstTimePackagePurchase(account.getUid(), storePackage.getPackageId());
        int bonusAmount = firstTimePurchase ? storePackage.getCurrencyAmount() : storePackage.getBonusAmount();
        int creditedAmount = storePackage.getCurrencyAmount() + bonusAmount;
        Wallet wallet = account.getWallet();
        wallet.setRootCredits(wallet.getRootCredits() + creditedAmount);

        StoreOrder order = new StoreOrder(
                nextOrderId++,
                account.getUid(),
                storePackage.getPackageId(),
                "PAID",
                storePackage.getPriceChfCents(),
                creditedAmount,
                bonusAmount,
                firstTimePurchase
        );
        orders.add(order);
        transactions.add(new WalletTransaction(
                nextTransactionId++,
                account.getUid(),
                creditedAmount,
                ROOT_CREDITS,
                "CREDIT",
                "Order " + order.getOrderId(),
                wallet.getRootCredits()
        ));
        return order;
    }

    public Wallet findWalletByUid(Long uid) {
        return findAccountByUid(uid).getWallet();
    }

    public List<WalletTransaction> findAllTransactions() {
        return transactions;
    }

    public List<WalletTransaction> findTransactionsByUid(Long uid) {
        findAccountByUid(uid);
        return transactions.stream()
                .filter(transaction -> transaction.getUid().equals(uid))
                .toList();
    }

    public WalletTransaction spendWalletBalance(SpendWalletDto spendWalletDto) {
        if (spendWalletDto.getAmount() <= 0) {
            throw new BadRequestException("Amount must be greater than 0.");
        }

        Wallet wallet = findWalletByUid(spendWalletDto.getUid());
        String currency = normalizeCurrency(spendWalletDto.getCurrency());
        int balance = getBalance(wallet, currency);

        if (balance < spendWalletDto.getAmount()) {
            throw new BadRequestException("Wallet balance is too low.");
        }

        int balanceAfter = balance - spendWalletDto.getAmount();
        setBalance(wallet, currency, balanceAfter);
        WalletTransaction transaction = new WalletTransaction(
                nextTransactionId++,
                wallet.getUid(),
                spendWalletDto.getAmount(),
                currency,
                "DEBIT",
                spendWalletDto.getReason(),
                balanceAfter
        );
        transactions.add(transaction);
        return transaction;
    }

    private StorePackage createPackageFromDto(Long packageId, StorePackageDto storePackageDto) {
        return new StorePackage(
                packageId,
                storePackageDto.getName(),
                storePackageDto.getPriceChfCents(),
                storePackageDto.getCurrencyAmount(),
                storePackageDto.getBonusAmount(),
                storePackageDto.isActive()
        );
    }

    private boolean isFirstTimePackagePurchase(Long uid, Long packageId) {
        return orders.stream()
                .noneMatch(order -> order.getUid().equals(uid)
                        && order.getPackageId().equals(packageId)
                        && "PAID".equals(order.getStatus()));
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

    private int getBalance(Wallet wallet, String currency) {
        if (ROOT_CREDITS.equals(currency)) {
            return wallet.getRootCredits();
        }
        return wallet.getEntropy();
    }

    private void setBalance(Wallet wallet, String currency, int balance) {
        if (ROOT_CREDITS.equals(currency)) {
            wallet.setRootCredits(balance);
            return;
        }
        wallet.setEntropy(balance);
    }
}
