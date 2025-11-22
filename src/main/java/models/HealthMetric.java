package models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * HealthMetric stores a single snapshot of a member's health:
 * - metricId is the primary key; the DB creates a PK index on this column so
 *   lookups by metricId are efficient.
 * - member is a @ManyToOne FK to Member (member_id); queries such as
 *   “all metrics for a given member” use the FK index on member_id.
 * - currentWeight and currentBmi are the recorded values for that snapshot.
 * - timestamp records when this metric was created/updated.
 *
 * TRIGGER-LIKE BEHAVIOUR (entity lifecycle callbacks):
 * - @PrePersist runs automatically before INSERT and sets timestamp = now.
 * - @PreUpdate runs automatically before UPDATE and refreshes timestamp.
 *   → This works like an application-level trigger that auto-stamps each
 *     health metric with the current time so later queries (including the
 *     latest-metric view/DTO used by MemberService) can sort or filter by it.
 */

@Entity
@Table(name = "health_metrics")
public class HealthMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long metricId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", referencedColumnName = "memberId", foreignKey = @ForeignKey(name = "FK_healthMetric_member"))
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
    }

    // TRIGGERS
    // --- Lifecycle triggers ---
    @PrePersist
    public void onPrePersist() {
        // runs before insert
        timestamp = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        // runs before update
        timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getMetricId() {
        return metricId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(int currentWeight) {
        this.currentWeight = currentWeight;
    }

    public int getCurrentBmi() {
        return currentBmi;
    }

    public void setCurrentBmi(int currentBMI) {
        this.currentBmi = currentBMI;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

}