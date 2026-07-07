package ch.kris.controller;

import ch.kris.dto.SpendWalletDto;
import ch.kris.model.Wallet;
import ch.kris.model.WalletTransaction;
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

@Path("/wallets")
public class WalletController {

    @Inject
    StoreService storeService;

    @GET
    @Path("/{uid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Wallet show(@PathParam("uid") Long uid) {
        return storeService.findWalletByUid(uid);
    }

    @GET
    @Path("/{uid}/transactions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<WalletTransaction> transactions(@PathParam("uid") Long uid) {
        return storeService.findTransactionsByUid(uid);
    }

    @GET
    @Path("/transactions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<WalletTransaction> allTransactions() {
        return storeService.findAllTransactions();
    }

    @POST
    @Path("/spend")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public WalletTransaction spend(SpendWalletDto spendWalletDto) {
        return storeService.spendWalletBalance(spendWalletDto);
    }
}
