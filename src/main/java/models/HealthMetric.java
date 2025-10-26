package models;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "health_metrics")
public class HealthMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long metricId;

    @ManyToOne(optional = false)
    @JoinColumn(
        name = "member_id", 
        referencedColumnName = "memberId",
        foreignKey = @ForeignKey(name = "FK_healthMetric_member")
    )
    private Member member;

    @Column(nullable = false)
    private int currentWeight;

    @Column(nullable = false)
    private int currentBmi;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public HealthMetric() {
    }

    public HealthMetric(Member member, int currentWeight, int currentBmi) {
        this.member = member;
        this.currentWeight = currentWeight;
        this.currentBmi = currentBmi;
        timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getMetricId() { return metricId; }

    public Member getMember() { return member; }

    public void setMember(Member member) { this.member = member; }

    public int getCurrentWeight() { return currentWeight; }
    public void setCurrentWeight(int currentWeight) { this.currentWeight = currentWeight; }

    public int getCurrentBmi() { return currentBmi; }
    public void setCurrentBmi(int currentBMI) { this.currentBmi = currentBMI; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

}