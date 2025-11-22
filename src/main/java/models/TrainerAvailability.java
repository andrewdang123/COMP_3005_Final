package models;

import java.time.DayOfWeek;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * TrainerAvailability stores one “free time slot” for a trainer.
 * - PK trainerAvailabilityId and FK trainer_id are indexed, so lookups (like
 *   finding all availability for a trainer) are efficient.
 * - dayOfWeek, startTime, and endTime define when the trainer is available.
 * - Used by booking logic to confirm availability and update time slots.
 * - No DB triggers or views—availability is managed fully in application
 *   code through lifecycle checks in FunctionsTrainer and @PrePersist/@PreUpdate.
 */

@Entity
@Table(name = "trainer_availability")
public class TrainerAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainerAvailabilityId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "trainer_id", referencedColumnName = "trainerId", foreignKey = @ForeignKey(name = "FK_trainerAvailability_trainer"))
    private Trainer trainer;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Min(0)
    @Max(24)
    private LocalTime startTime;

    @Min(0)
    @Max(24)
    private LocalTime endTime;

    public TrainerAvailability() {
    }

    public TrainerAvailability(Trainer trainer, DayOfWeek dayOfWeek, int startTime, int endTime) {
        this.trainer = trainer;
        this.dayOfWeek = dayOfWeek;
        this.startTime = LocalTime.of(startTime, 0);
        this.endTime = LocalTime.of(endTime, 0);
    }

    public TrainerAvailability(Trainer trainer, String dayOfWeek, int startTime, int endTime) {
        this.trainer = trainer;
        this.dayOfWeek = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
        this.startTime = LocalTime.of(startTime, 0);
        this.endTime = LocalTime.of(endTime, 0);
    }

    // Getters and Setters
    public Long getTrainerAvailabilityId() {
        return trainerAvailabilityId;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = LocalTime.of(startTime, 0);
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = LocalTime.of(endTime, 0);
    }

    public String toString() {
        return dayOfWeek.toString() + " " + startTime.toString() + " - " + endTime.toString();
    }

}
