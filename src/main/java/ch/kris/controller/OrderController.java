package ch.kris.controller;

import ch.kris.dto.OrderDto;
import ch.kris.model.Order;
import ch.kris.security.CurrentUser;
import ch.kris.service.OrderService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CurrentUser currentUser;

    public OrderController(OrderService orderService, CurrentUser currentUser) {
        this.orderService = orderService;
        this.currentUser = currentUser;
    }

    @GET
    @RolesAllowed("ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Order> index() {
        return orderService.findAllOrders();
    }

    @GET
    @Path("/{orderId}")
    @RolesAllowed({"ADMIN", "USER"})
    @Produces(MediaType.APPLICATION_JSON)
    public Order show(@PathParam("orderId") Long orderId) {
        Order order = orderService.findOrderById(orderId);
        currentUser.requireOwnerOrAdmin(order.getUid());
        return order;
    }

    @POST
    @RolesAllowed({"ADMIN", "USER"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Order create(OrderDto orderDto) {
        if (currentUser.isAdmin()) {
            if (orderDto.getUid() == null) {
                throw new BadRequestException("uid is required.");
            }
        } else {
            Long ownUid = currentUser.getUid();
            if (ownUid == null) {
                throw new BadRequestException("Your login is not linked to a game account.");
            }
            orderDto.setUid(ownUid);
        }
        return orderService.createOrder(orderDto);
    }
}
