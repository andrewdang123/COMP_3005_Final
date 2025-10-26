package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.DayOfWeek;

@Entity
@Table(name = "trainer_availability")
public class TrainerAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainerAvailabilityId;

    @ManyToOne(optional = false)
    @JoinColumn(
        name = "trainer_id", 
        referencedColumnName = "trainerId",
        foreignKey = @ForeignKey(name = "FK_trainerAvailability_trainer")
    )
    private Trainer trainer;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Min(0) @Max(24)
    private int startTime;

    @Min(0) @Max(24)
    private int endTime;

    public TrainerAvailability() {
    }

    public TrainerAvailability(Trainer trainer, DayOfWeek dayOfWeek, int startTime, int endTime) {
        this.trainer = trainer;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TrainerAvailability(Trainer trainer, String dayOfWeek, int startTime, int endTime) {
        this.trainer = trainer;
        this.dayOfWeek = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters
    public Long getTrainerAvailabilityId() { return trainerAvailabilityId; }

    public Trainer getTrainer() { return trainer; }
    public void setTrainer(Trainer trainer) { this.trainer = trainer; }

    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = DayOfWeek.valueOf(dayOfWeek.toUpperCase()); }

    public int getStartTime() { return startTime; }
    public void setStartTime(int startTime) { this.startTime = startTime; }

    public int getEndTime() { return endTime; }
    public void setEndTime(int endTime) { this.endTime = endTime; }

}
