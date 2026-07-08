package ch.kris.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "account_transactions")
public class AccountTransaction extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    private Long uid;
    private int amount;
    private String currency;
    private String type;
    private String reason;
    private int balanceAfter;

    public AccountTransaction() {
    }

    public AccountTransaction(Long transactionId, Long uid, int amount, String currency, String type, String reason, int balanceAfter) {
        this.transactionId = transactionId;
        this.uid = uid;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.reason = reason;
        this.balanceAfter = balanceAfter;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(int balanceAfter) {
        this.balanceAfter = balanceAfter;
    }
}
