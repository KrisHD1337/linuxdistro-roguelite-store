package ch.kris.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "packages")
public class Package extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;
    private String name;
    private int priceChfCents;
    private int currencyAmount;
    private int bonusAmount;

    @JsonProperty("isActive")
    private boolean active;

    public Package() {
    }

    public Package(Long packageId, String name, int priceChfCents, int currencyAmount, int bonusAmount, boolean active) {
        this.packageId = packageId;
        this.name = name;
        this.priceChfCents = priceChfCents;
        this.currencyAmount = currencyAmount;
        this.bonusAmount = bonusAmount;
        this.active = active;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriceChfCents() {
        return priceChfCents;
    }

    public void setPriceChfCents(int priceChfCents) {
        this.priceChfCents = priceChfCents;
    }

    public int getCurrencyAmount() {
        return currencyAmount;
    }

    public void setCurrencyAmount(int currencyAmount) {
        this.currencyAmount = currencyAmount;
    }

    public int getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(int bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    @JsonProperty("isActive")
    public boolean isActive() {
        return active;
    }

    @JsonProperty("isActive")
    public void setActive(boolean active) {
        this.active = active;
    }
}
