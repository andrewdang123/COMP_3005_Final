package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "health_metrics")
public class HealthMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long metricId;

    @NotNull
    @Column(nullable = false)
    private Long memberId;

    @NotNull
    @Column(nullable = false)
    private double currentWeight;

    @NotNull
    @Column(nullable = false)
    private double currentBMI;

    @NotNull
    @Column(nullable = false)
    private String timestamp;

    public HealthMetric() {
    }

    public HealthMetric(Long memberId, double currentWeight, double currentBMI, String timestamp) {
        this.memberId = memberId;
        this.currentWeight = currentWeight;
        this.currentBMI = currentBMI;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public Long getMetricId() {
        return metricId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }

    public double getCurrentBMI() {
        return currentBMI;
    }

    public void setCurrentBMI(double currentBMI) {
        this.currentBMI = currentBMI;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return memberId + "\t" + metricId + "\t" + currentWeight + "\t" + currentBMI + "\t" + timestamp;
    }
}
