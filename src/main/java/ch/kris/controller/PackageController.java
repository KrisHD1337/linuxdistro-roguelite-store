package ch.kris.controller;

import ch.kris.dto.PackageDto;
import ch.kris.model.StorePackage;
import ch.kris.service.PackageService;
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
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/packages")
public class PackageController {

    @Inject
    PackageService packageService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<StorePackage> index() {
        return packageService.findAllPackages();
    }

    @GET
    @Path("/{packageId}")
    @Produces(MediaType.APPLICATION_JSON)
    public StorePackage show(@PathParam("packageId") Long packageId) {
        return packageService.findPackageById(packageId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public StorePackage create(PackageDto packageDto) {
        return packageService.createPackage(packageDto);
    }

    @PUT
    @Path("/{packageId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("packageId") Long packageId, PackageDto packageDto) {
        StorePackage updatedPackage = packageService.updatePackage(packageId, packageDto);
        return Response.status(Response.Status.NO_CONTENT)
                .entity(updatedPackage)
                .build();
    }

    @DELETE
    @Path("/{packageId}")
    @Produces(MediaType.APPLICATION_JSON)
    public StorePackage delete(@PathParam("packageId") Long packageId) {
        return packageService.deletePackage(packageId);
    }
}
