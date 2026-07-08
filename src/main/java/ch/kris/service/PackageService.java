package ch.kris.service;

import ch.kris.dto.PackageDto;
import ch.kris.model.Package;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PackageService {
    private final List<Package> packages = new ArrayList<>();

    private long nextPackageId = 4;

    public List<Package> findAllPackages() {
        return packages;
    }

    public Package findPackageById(Long packageId) {
        return packages.stream()
                .filter(Package -> Package.getPackageId().equals(packageId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Package with id " + packageId + " was not found."));
    }

    public Package createPackage(PackageDto packageDto) {
        Package Package = createPackageFromDto(nextPackageId++, packageDto);
        packages.add(Package);
        return Package;
    }

    public Package updatePackage(Long packageId, PackageDto packageDto) {
        Package existingPackage = findPackageById(packageId);
        existingPackage.setName(packageDto.getName());
        existingPackage.setPriceChfCents(packageDto.getPriceChfCents());
        existingPackage.setCurrencyAmount(packageDto.getCurrencyAmount());
        existingPackage.setBonusAmount(packageDto.getBonusAmount());
        existingPackage.setActive(packageDto.isActive());
        return existingPackage;
    }

    public Package deletePackage(Long packageId) {
        Package Package = findPackageById(packageId);
        packages.remove(Package);
        return Package;
    }

    private Package createPackageFromDto(Long packageId, PackageDto packageDto) {
        return new Package(
                packageId,
                packageDto.getName(),
                packageDto.getPriceChfCents(),
                packageDto.getCurrencyAmount(),
                packageDto.getBonusAmount(),
                packageDto.isActive()
        );
    }
}
