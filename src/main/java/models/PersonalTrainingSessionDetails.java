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
 * PersonalTrainingSessionDetails stores the room and time for a PT session:
 * - Uses a shared primary key with PersonalTrainingSession:
 *   • sessionId is both the PK of this table and an FK back to
 *     PersonalTrainingSession via @MapsId.
 *   • The DB creates a primary key index on session_id, which makes joins
 *     between personal_training_sessions and personal_training_session_details fast.
 * - personalTrainingSession is the owning 1–1 parent; @MapsId guarantees there is
 *   exactly one details row per session.
 * - roomNum is the room where the session happens.
 * - sessionTime is an embedded Schedule (dayOfWeek + startTime + endTime);
 *   this is what the app uses in queries to check for room conflicts and to
 *   build the trainer’s schedule view.
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
