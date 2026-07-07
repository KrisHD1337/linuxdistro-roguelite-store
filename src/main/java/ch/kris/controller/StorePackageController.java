package ch.kris.controller;

import ch.kris.dto.StorePackageDto;
import ch.kris.model.StorePackage;
import ch.kris.service.StoreService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/packages")
public class StorePackageController {

    @Inject
    StoreService storeService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<StorePackage> index() {
        return storeService.findAllPackages();
    }

    @GET
    @Path("/{packageId}")
    @Produces(MediaType.APPLICATION_JSON)
    public StorePackage show(@PathParam("packageId") Long packageId) {
        return storeService.findPackageById(packageId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public StorePackage create(StorePackageDto storePackageDto) {
        return storeService.createPackage(storePackageDto);
    }

    @PUT
    @Path("/{packageId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public StorePackage update(@PathParam("packageId") Long packageId, StorePackageDto storePackageDto) {
        return storeService.updatePackage(packageId, storePackageDto);
    }

    @DELETE
    @Path("/{packageId}")
    @Produces(MediaType.APPLICATION_JSON)
    public StorePackage delete(@PathParam("packageId") Long packageId) {
        return storeService.deletePackage(packageId);
    }
}
