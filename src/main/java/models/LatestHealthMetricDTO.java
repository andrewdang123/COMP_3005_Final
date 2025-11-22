package models;

import java.time.LocalDateTime;

/**
 * LatestHealthMetricDTO is a simple Data Transfer Object (DTO), not an @Entity:
 * - It represents the *latest* HealthMetric for each member returned by a query
 *   in MemberService (often built using GROUP BY / MAX(timestamp) or a DB view).
 * - It is populated directly from a SELECT query (e.g., constructor expression)
 *   rather than from a mapped table.
 * - Contains: memberId, memberName, currentWeight, currentBmi, and the timestamp
 *   of that latest metric.
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
