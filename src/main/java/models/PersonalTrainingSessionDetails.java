package models;

import java.time.DayOfWeek;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * PersonalTrainingSessionDetails stores the room + time of a PT session.
 * - Shares its primary key with PersonalTrainingSession via @MapsId, so each
 *   session has exactly one details row. The PK/FK index on session_id makes
 *   joins very fast.
 * - roomNum is where the session occurs.
 * - sessionTime is an embedded Schedule used for availability checks,
 *   room-conflict checks, and schedule displays.
 */

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
