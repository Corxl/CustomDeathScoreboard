package me.corxl.customdeathscoreboard;

import java.io.Serializable;

public class PlayerScoreData implements Serializable {
    private int deathScore;
    private boolean toggle;
    private String displayName;


    public PlayerScoreData(String displayName) {
        deathScore = 0;
        toggle = true;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getDeathScore() {
        return this.deathScore;
    }

    public void increaseDeathScore() {
        this.deathScore  += 1;
    }

    public boolean getToggle() {
        return toggle;
    }

    public void toggleBoard() {
        toggle = !toggle;
    }

    public void setDeathScore(int num) {
        this.deathScore = num;
    }
}
