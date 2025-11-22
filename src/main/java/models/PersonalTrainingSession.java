package models;

import java.time.DayOfWeek;

import app.FunctionsTrainer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * PersonalTrainingSession uses PK and FK indexes (sessionId, member_id, trainer_id)
 * for fast lookups. Its @PrePersist/@PreUpdate methods act like application-level
 * triggers to prevent double-booking:
 * - Before insert/update, it checks trainer availability.
 * - If unavailable, it throws an error and cancels the transaction.
 * - If available, it updates the trainer’s availability so the slot becomes “taken.”
 * This keeps all PT scheduling consistent and conflict-free.
 */

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

    @PrePersist
    public void beforeInsert() {
        Trainer trainer = this.getTrainer();
        String dayOfWeek = this.getSessionDetails().getSessionTime().getDayOfWeek().toString();
        int startTime = this.getSessionDetails().getSessionTime().getStartTime().getHour();
        int endTime = this.getSessionDetails().getSessionTime().getEndTime().getHour();
        if (!FunctionsTrainer.trainerCheckAvailability(trainer, dayOfWeek, startTime, endTime)) {
            throw new RuntimeException("Trainer " + trainer.getName() + " unavailable at that time");
        }
        FunctionsTrainer.trainerAdjustAvailability(trainer, dayOfWeek, startTime, endTime);
    }

    @PreUpdate
    public void beforeUpdate() {
        Trainer trainer = this.getTrainer();
        String dayOfWeek = this.getSessionDetails().getSessionTime().getDayOfWeek().toString();
        int startTime = this.getSessionDetails().getSessionTime().getStartTime().getHour();
        int endTime = this.getSessionDetails().getSessionTime().getEndTime().getHour();
        if (!FunctionsTrainer.trainerCheckAvailability(trainer, dayOfWeek, startTime, endTime)) {
            throw new RuntimeException("Trainer " + trainer.getName() + " unavailable at that time");
        }
        FunctionsTrainer.trainerAdjustAvailability(trainer, dayOfWeek, startTime, endTime);
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

    public void print() {
        System.out.println("Personal training session booked successfully!");
        System.out.println("Trainer: " + trainer.getName());
        System.out.println("Member: " + member.getName());
        System.out.println("Day: " + personalTrainingSesssionDetails.getSessionTime().getDayOfWeek().toString());
        System.out.println("Time: " + personalTrainingSesssionDetails.getSessionTime().getStartTime() + " - "
                + personalTrainingSesssionDetails.getSessionTime().getEndTime());
        System.out.println("Room: " + personalTrainingSesssionDetails.getRoomNum());
    }

    public void setSessionDetails(PersonalTrainingSessionDetails details) {
        if (this.personalTrainingSesssionDetails != null && details != this.personalTrainingSesssionDetails) {
            throw new IllegalStateException("Session details already exist. Modify them instead of replacing.");
        }
        this.personalTrainingSesssionDetails = details;
        if (details != null) {
            details.setPersonalTrainingSession(this);
        }
    }

    public String toString() {
        return "Trainer: " + trainer.getTrainerId() + " " + trainer.getName() + " | Member: " + member.getName() + " | "
                + personalTrainingSesssionDetails.toString();
    }

}
