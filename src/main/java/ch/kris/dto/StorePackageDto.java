package ch.kris.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StorePackageDto {
    private String name;
    private int priceChfCents;
    private int currencyAmount;
    private int bonusAmount;

    @JsonProperty("isActive")
    private boolean active;

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
