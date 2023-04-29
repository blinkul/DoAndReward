package com.example.doandreward.entities;

public class HistoryWithObjectiveOrPrize {

    private int id;
    private String objectiveDescription;
    private String prizeDescription;
    private Integer totalPoints;
    private Integer objectivePoints;
    private Integer prizeCost;

    public String getObjectiveDescription() {
        return objectiveDescription;
    }

    public void setObjectiveDescription(String objectiveDescription) {
        this.objectiveDescription = objectiveDescription;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getObjectivePoints() {
        if (objectivePoints == null) {
            return 0;
        }
        return objectivePoints;
    }

    public void setObjectivePoints(Integer objectivePoints) {
        this.objectivePoints = objectivePoints;
    }

    public Integer getPrizeCost() {
        if (prizeCost == null) {
            return 0;
        }
        return prizeCost;
    }

    public void setPrizeCost(Integer prizeCost) {
        this.prizeCost = prizeCost;
    }

    public String getPrizeDescription() {
        return prizeDescription;
    }

    public void setPrizeDescription(String prizeDescription) {
        this.prizeDescription = prizeDescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
