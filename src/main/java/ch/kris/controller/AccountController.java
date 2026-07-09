package ch.kris.controller;

import ch.kris.dto.AccountDto;
import ch.kris.dto.SpendBalanceDto;
import ch.kris.model.Account;
import ch.kris.model.AccountTransaction;
import ch.kris.service.AccountService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Account> index() {
        return accountService.findAllAccounts();
    }

    @GET
    @Path("/{uid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Account show(@PathParam("uid") Long uid) {
        return accountService.findAccountByUid(uid);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Account create(AccountDto accountDto) {
        return accountService.createAccount(accountDto);
    }

    @GET
    @Path("/{uid}/transactions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AccountTransaction> transactions(@PathParam("uid") Long uid) {
        return accountService.findTransactionsByUid(uid);
    }

    @GET
    @Path("/transactions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AccountTransaction> allTransactions() {
        return accountService.findAllTransactions();
    }

    @POST
    @Path("/{uid}/spend")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public AccountTransaction spend(@PathParam("uid") Long uid, SpendBalanceDto spendBalanceDto) {
        spendBalanceDto.setUid(uid);
        return accountService.spendBalance(spendBalanceDto);
    }
}
