package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "personal_training_sessions")
public class PersonalTrainingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @NotNull
    @Column(nullable = false)
    private Long memberId;

    @NotNull
    @Column(nullable = false)
    private Long trainerId;

    @NotNull
    @Column(nullable = false)
    private int roomNum;

    @NotNull
    @Column(nullable = false)
    private int sessionTime;

    public PersonalTrainingSession(Long memberId, Long trainerId, int roomNum, int sessionTime) {
        this.memberId = memberId;
        this.trainerId = trainerId;
        this.roomNum = roomNum;
        this.sessionTime = sessionTime;
    }

    // Getters and setters
    public Long getSessionId() {
        return sessionId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public int getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(int sessionTime) {
        this.sessionTime = sessionTime;
    }

    @Override
    public String toString() {
        return sessionId + "\t" + memberId + "\t" + trainerId + "\t" + roomNum + "\t" + sessionTime;
    }
}
