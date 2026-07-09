package ch.kris.service;

import ch.kris.dto.OrderDto;
import ch.kris.model.Account;
import ch.kris.model.Order;
import ch.kris.model.Package;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class OrderService {
    private final AccountService accountService;
    private final PackageService packageService;

    public OrderService(AccountService accountService, PackageService packageService) {
        this.accountService = accountService;
        this.packageService = packageService;
    }

    public List<Order> findAllOrders() {
        return Order.listAll();
    }

    public Order findOrderById(Long orderId) {
        Order order = Order.findById(orderId);
        if (order == null) {
            throw new NotFoundException("Order with id " + orderId + " was not found.");
        }
        return order;
    }

    @Transactional
    public Order createOrder(OrderDto orderDto) {
        Account account = accountService.findAccountByUid(orderDto.getUid());
        Package distroPackage = packageService.findPackageById(orderDto.getPackageId());

        if (!distroPackage.isActive()) {
            throw new BadRequestException("Package with id " + distroPackage.getPackageId() + " is not active.");
        }

        boolean firstTimePurchase = isFirstTimePackagePurchase(account.getUid(), distroPackage.getPackageId());
        int bonusAmount = firstTimePurchase ? distroPackage.getCurrencyAmount() : distroPackage.getBonusAmount();
        int creditedAmount = distroPackage.getCurrencyAmount() + bonusAmount;

        Order order = new Order(
                null,
                account.getUid(),
                distroPackage.getPackageId(),
                "PAID",
                distroPackage.getPriceChfCents(),
                creditedAmount,
                bonusAmount,
                firstTimePurchase
        );
        order.persistAndFlush();
        accountService.creditRootCredits(account.getUid(), creditedAmount, "Order " + order.getOrderId());
        return order;
    }

    private boolean isFirstTimePackagePurchase(Long uid, Long packageId) {
        return Order.count("uid = ?1 and packageId = ?2 and status = ?3", uid, packageId, "PAID") == 0;
    }
}
