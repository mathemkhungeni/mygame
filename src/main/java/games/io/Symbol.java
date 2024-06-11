package games.io;

public class Symbol {
    public Symbol(double rewardMultiplier, String type) {
        this.rewardMultiplier = rewardMultiplier;
        this.type = type;
    }

    private double rewardMultiplier;
    private String type;

    public Symbol() {
    }

    public double getRewardMultiplier() {
        return rewardMultiplier;
    }

    public void setRewardMultiplier(double rewardMultiplier) {
        this.rewardMultiplier = rewardMultiplier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
