package ch.kris.service;

import ch.kris.dto.PackageDto;
import ch.kris.model.Package;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class PackageService {
    public List<Package> findAllPackages() {
        return Package.listAll();
    }

    public Package findPackageById(Long packageId) {
        Package distroPackage = Package.findById(packageId);
        if (distroPackage == null) {
            throw new NotFoundException("Package with id " + packageId + " was not found.");
        }
        return distroPackage;
    }

    @Transactional
    public Package createPackage(PackageDto packageDto) {
        Package distroPackage = createPackageFromDto(packageDto);
        distroPackage.persist();
        return distroPackage;
    }

    @Transactional
    public Package updatePackage(Long packageId, PackageDto packageDto) {
        Package existingPackage = findPackageById(packageId);
        existingPackage.setName(packageDto.getName());
        existingPackage.setPriceChfCents(packageDto.getPriceChfCents());
        existingPackage.setCurrencyAmount(packageDto.getCurrencyAmount());
        existingPackage.setBonusAmount(packageDto.getBonusAmount());
        existingPackage.setActive(packageDto.isActive());
        return existingPackage;
    }

    @Transactional
    public Package deletePackage(Long packageId) {
        Package distroPackage = findPackageById(packageId);
        distroPackage.delete();
        return distroPackage;
    }

    private Package createPackageFromDto(PackageDto packageDto) {
        return new Package(
                null,
                packageDto.getName(),
                packageDto.getPriceChfCents(),
                packageDto.getCurrencyAmount(),
                packageDto.getBonusAmount(),
                packageDto.isActive()
        );
    }
}
