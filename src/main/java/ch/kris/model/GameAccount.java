package ch.kris.model;

public class GameAccount {
    private Long uid;
    private String playerName;
    private Wallet wallet;

    public GameAccount() {
    }

    public GameAccount(Long uid, String playerName, Wallet wallet) {
        this.uid = uid;
        this.playerName = playerName;
        this.wallet = wallet;
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

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}
