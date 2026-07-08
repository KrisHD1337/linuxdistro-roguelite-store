package ch.kris.service;

import ch.kris.dto.OrderDto;
import ch.kris.model.Account;
import ch.kris.model.Order;
import ch.kris.model.Package;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class OrderService {
    private final List<Order> orders = new ArrayList<>();

    private long nextOrderId = 3;

    @Inject
    AccountService accountService;

    @Inject
    PackageService packageService;

    public List<Order> findAllOrders() {
        return orders;
    }

    public Order findOrderById(Long orderId) {
        return orders.stream()
                .filter(order -> order.getOrderId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Order with id " + orderId + " was not found."));
    }

    public Order createOrder(OrderDto orderDto) {
        Account account = accountService.findAccountByUid(orderDto.getUid());
        Package Package = packageService.findPackageById(orderDto.getPackageId());

        if (!Package.isActive()) {
            throw new BadRequestException("Package with id " + Package.getPackageId() + " is not active.");
        }

        boolean firstTimePurchase = isFirstTimePackagePurchase(account.getUid(), Package.getPackageId());
        int bonusAmount = firstTimePurchase ? Package.getCurrencyAmount() : Package.getBonusAmount();
        int creditedAmount = Package.getCurrencyAmount() + bonusAmount;

        Order order = new Order(
                nextOrderId++,
                account.getUid(),
                Package.getPackageId(),
                "PAID",
                Package.getPriceChfCents(),
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
