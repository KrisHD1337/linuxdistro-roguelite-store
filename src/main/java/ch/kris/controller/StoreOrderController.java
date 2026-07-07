package ch.kris.controller;

import ch.kris.dto.OrderDto;
import ch.kris.model.StoreOrder;
import ch.kris.service.StoreService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/orders")
public class StoreOrderController {

    @Inject
    StoreService storeService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<StoreOrder> index() {
        return storeService.findAllOrders();
    }

    @GET
    @Path("/{orderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public StoreOrder show(@PathParam("orderId") Long orderId) {
        return storeService.findOrderById(orderId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public StoreOrder create(OrderDto orderDto) {
        return storeService.createOrder(orderDto);
    }
}
