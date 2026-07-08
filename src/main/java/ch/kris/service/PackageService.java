package ch.kris.service;

import ch.kris.dto.PackageDto;
import ch.kris.model.StorePackage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PackageService {
    private final List<StorePackage> packages = new ArrayList<>();

    private long nextPackageId = 4;

    public List<StorePackage> findAllPackages() {
        return packages;
    }

    public StorePackage findPackageById(Long packageId) {
        return packages.stream()
                .filter(storePackage -> storePackage.getPackageId().equals(packageId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Package with id " + packageId + " was not found."));
    }

    public StorePackage createPackage(PackageDto packageDto) {
        StorePackage storePackage = createPackageFromDto(nextPackageId++, packageDto);
        packages.add(storePackage);
        return storePackage;
    }

    public StorePackage updatePackage(Long packageId, PackageDto packageDto) {
        StorePackage existingPackage = findPackageById(packageId);
        existingPackage.setName(packageDto.getName());
        existingPackage.setPriceChfCents(packageDto.getPriceChfCents());
        existingPackage.setCurrencyAmount(packageDto.getCurrencyAmount());
        existingPackage.setBonusAmount(packageDto.getBonusAmount());
        existingPackage.setActive(packageDto.isActive());
        return existingPackage;
    }

    public StorePackage deletePackage(Long packageId) {
        StorePackage storePackage = findPackageById(packageId);
        packages.remove(storePackage);
        return storePackage;
    }

    private StorePackage createPackageFromDto(Long packageId, PackageDto packageDto) {
        return new StorePackage(
                packageId,
                packageDto.getName(),
                packageDto.getPriceChfCents(),
                packageDto.getCurrencyAmount(),
                packageDto.getBonusAmount(),
                packageDto.isActive()
        );
    }
}
