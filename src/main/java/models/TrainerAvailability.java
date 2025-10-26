package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.DayOfWeek;
import java.time.LocalTime;

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

}
