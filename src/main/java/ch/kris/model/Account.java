package ch.kris.model;

public class Account {
    private Long uid;
    private String playerName;
    private int rootCredits;
    private int entropy;

    public Account() {
    }

    public Account(Long uid, String playerName, int rootCredits, int entropy) {
        this.uid = uid;
        this.playerName = playerName;
        this.rootCredits = rootCredits;
        this.entropy = entropy;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
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
