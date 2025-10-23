package models;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "health_metrics")
public class HealthMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long metricId;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private int currentWeight;

    @Column(nullable = false)
    private int currentBmi;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public HealthMetric() {
    }

    public HealthMetric(Long memberId, int currentWeight, int currentBmi) {
        this.memberId = memberId;
        this.currentWeight = currentWeight;
        this.currentBmi = currentBmi;
        timestamp = LocalDateTime.now();
    }

   // Getters and Setters
    public Long getMetricId() { return metricId; }

    public Long getMemberId() { return memberId; }

    public void setMemberId(Long memberId) { this.memberId = memberId; }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(int currentWeight) { this.currentWeight = currentWeight; }

    public int getCurrentBmi() { return currentBmi; }
    public void setCurrentBmi(int currentBMI) { this.currentBmi = currentBMI; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

}