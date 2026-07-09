package ch.kris.controller;

import ch.kris.dto.OrderDto;
import ch.kris.model.Order;
import ch.kris.service.OrderService;
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

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Order> index() {
        return orderService.findAllOrders();
    }

    @GET
    @Path("/{orderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Order show(@PathParam("orderId") Long orderId) {
        return orderService.findOrderById(orderId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Order create(OrderDto orderDto) {
        return orderService.createOrder(orderDto);
    }
}
