package models;

import jakarta.persistence.*;
import java.time.DayOfWeek;

@Entity
@Table(name = "personal_training_sessions")
public class PersonalTrainingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Trainer trainer;

    @OneToOne(mappedBy = "personalTrainingSession", cascade = CascadeType.ALL)
    private PersonalTrainingSessionDetails personalTrainingSesssionDetails;

    public PersonalTrainingSession() {
    }

    public PersonalTrainingSession(Member member, Trainer trainer) {
        this.member = member;
        this.trainer = trainer;
    }

    public PersonalTrainingSession(Member member, Trainer trainer, int roomNum, String dayOfWeek, int startTime,
            int endTime) {
        this.member = member;
        this.trainer = trainer;
        this.personalTrainingSesssionDetails = new PersonalTrainingSessionDetails(this, roomNum, dayOfWeek, startTime,
                endTime);
    }

    public PersonalTrainingSession(Member member, Trainer trainer, int roomNum, DayOfWeek dayOfWeek, int startTime,
            int endTime) {
        this.member = member;
        this.trainer = trainer;
        this.personalTrainingSesssionDetails = new PersonalTrainingSessionDetails(this, roomNum, dayOfWeek, startTime,
                endTime);
    }

    // getters and setters
    public Long getSessionId() {
        return sessionId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public PersonalTrainingSessionDetails getSessionDetails() {
        return personalTrainingSesssionDetails;
    }

    public void setSessionDetails(PersonalTrainingSessionDetails personalTrainingSesssionDetails) {
        this.personalTrainingSesssionDetails = personalTrainingSesssionDetails;
        personalTrainingSesssionDetails.setPersonalTrainingSession(this);
    }
}
