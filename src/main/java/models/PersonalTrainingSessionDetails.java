package models;

import jakarta.persistence.*;
import java.time.DayOfWeek;

@Entity
@Table(name = "personal_training_session_details")
public class PersonalTrainingSessionDetails {

    @Id
    private Long sessionId; // same as PersonalTrainingSession.sessionId

    @OneToOne
    @MapsId
    @JoinColumn(name = "session_id")
    private PersonalTrainingSession personalTrainingSession;

    @Column(nullable = false)
    private int roomNum;

    @Embedded
    private Schedule sessionTime;

    public PersonalTrainingSessionDetails() {

    }

    public PersonalTrainingSessionDetails(PersonalTrainingSession personalTrainingSession, int roomNum,
            String dayOfWeek, int startTime, int endTime) {
        this.personalTrainingSession = personalTrainingSession;
        this.roomNum = roomNum;
        this.sessionTime = new Schedule(dayOfWeek, startTime, endTime);
    }

    public PersonalTrainingSessionDetails(PersonalTrainingSession personalTrainingSession, int roomNum,
            DayOfWeek dayOfWeek, int startTime, int endTime) {
        this.personalTrainingSession = personalTrainingSession;
        this.roomNum = roomNum;
        this.sessionTime = new Schedule(dayOfWeek, startTime, endTime);
    }

    // getters and setters
    public Long getSessionId() {
        return sessionId;
    }

    public PersonalTrainingSession getPersonalTrainingSession() {
        return personalTrainingSession;
    }

    public void setPersonalTrainingSession(PersonalTrainingSession personalTrainingSession) {
        this.personalTrainingSession = personalTrainingSession;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public Schedule getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(Schedule sessionTime) {
        this.sessionTime = sessionTime;
    }

    public String toString() {
        return "Room " + roomNum + " at " + sessionTime.toString();
    }

}
