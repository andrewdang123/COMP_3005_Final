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

/*
 * “The PT session entity is indexed on sessionId and uses FK indexes on member_id and
 * trainer_id. The important part is the @PrePersist/@PreUpdate callbacks, which act like
 * application-level triggers to enforce ‘no double booking’ for trainers and to keep their
 * availability in sync whenever PT sessions are created or changed.”
 * 
 * TRIGGER-LIKE BEHAVIOUR (entity lifecycle callbacks):
 * - @PrePersist (beforeInsert):
 *   • Runs right before INSERT.
 *   • Reads the trainer, day, start, and end time from the details.
 *   • Calls FunctionsTrainer.trainerCheckAvailability(...) to ensure the trainer is free.
 *   • If trainer is not available, throws a RuntimeException and the transaction rolls back.
 *   • If they are available, calls FunctionsTrainer.trainerAdjustAvailability(...) to
 *     update the trainer’s availability model so that slot is no longer free.
 * - @PreUpdate (beforeUpdate):
 *   • Same logic, but for UPDATE: prevents rescheduling a session into a busy slot and
 *     updates availability when the time changes.
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
