package models;

import java.time.LocalDateTime;

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
