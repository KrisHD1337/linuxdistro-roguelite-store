package ch.kris.controller;

import ch.kris.dto.GameAccountDto;
import ch.kris.model.GameAccount;
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

@Path("/accounts")
public class GameAccountController {

    @Inject
    StoreService storeService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<GameAccount> index() {
        return storeService.findAllAccounts();
    }

    @GET
    @Path("/{uid}")
    @Produces(MediaType.APPLICATION_JSON)
    public GameAccount show(@PathParam("uid") Long uid) {
        return storeService.findAccountByUid(uid);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GameAccount create(GameAccountDto gameAccountDto) {
        return storeService.createAccount(gameAccountDto);
    }
}
