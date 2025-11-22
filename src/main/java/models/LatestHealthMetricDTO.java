package models;

import java.time.LocalDateTime;

/**
 * LatestHealthMetricDTO is a simple DTO (not an entity).
 * - Holds the *latest* HealthMetric for each member, created directly from a
 *   SELECT query in MemberService.
 * - Stores memberId, name, weight, BMI, and the timestamp of the newest record.
 */

public class LatestHealthMetricDTO {
    private Long memberId;
    private String memberName;
    private int currentWeight;
    private int currentBmi;
    private LocalDateTime timestamp;

    public LatestHealthMetricDTO(Long memberId, String memberName, int currentWeight, int currentBmi,
            LocalDateTime timestamp) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.currentWeight = currentWeight;
        this.currentBmi = currentBmi;
        this.timestamp = timestamp;
    }

    // Getters
    public Long getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public int getCurrentBmi() {
        return currentBmi;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
