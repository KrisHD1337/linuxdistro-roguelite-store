package ch.kris.service;

import ch.kris.dto.OrderDto;
import ch.kris.model.Account;
import ch.kris.model.StoreOrder;
import ch.kris.model.StorePackage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class OrderService {
    private final List<StoreOrder> orders = new ArrayList<>();

    private long nextOrderId = 3;

    @Inject
    AccountService accountService;

    @Inject
    PackageService packageService;

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
        Account account = accountService.findAccountByUid(orderDto.getUid());
        StorePackage storePackage = packageService.findPackageById(orderDto.getPackageId());

        if (!storePackage.isActive()) {
            throw new BadRequestException("Package with id " + storePackage.getPackageId() + " is not active.");
        }

        boolean firstTimePurchase = isFirstTimePackagePurchase(account.getUid(), storePackage.getPackageId());
        int bonusAmount = firstTimePurchase ? storePackage.getCurrencyAmount() : storePackage.getBonusAmount();
        int creditedAmount = storePackage.getCurrencyAmount() + bonusAmount;

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
        accountService.creditRootCredits(account.getUid(), creditedAmount, "Order " + order.getOrderId());
        return order;
    }

    private boolean isFirstTimePackagePurchase(Long uid, Long packageId) {
        return orders.stream()
                .noneMatch(order -> order.getUid().equals(uid)
                        && order.getPackageId().equals(packageId)
                        && "PAID".equals(order.getStatus()));
    }
}
