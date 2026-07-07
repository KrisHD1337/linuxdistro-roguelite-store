package ch.kris.model;

public class Wallet {
    private Long uid;
    private int rootCredits;
    private int entropy;

    public Wallet() {
    }

    public Wallet(Long uid, int rootCredits, int entropy) {
        this.uid = uid;
        this.rootCredits = rootCredits;
        this.entropy = entropy;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public int getRootCredits() {
        return rootCredits;
    }

    public void setRootCredits(int rootCredits) {
        this.rootCredits = rootCredits;
    }

    public int getEntropy() {
        return entropy;
    }

    public void setEntropy(int entropy) {
        this.entropy = entropy;
    }
}
