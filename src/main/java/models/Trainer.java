package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "trainers")
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainerId;

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "dayOfWeek", column = @Column(name = "available_Day", nullable = false)),
        @AttributeOverride(name = "startTime", column = @Column(name = "available_StartTime", nullable = false)),
        @AttributeOverride(name = "endTime", column = @Column(name = "available_EndTime", nullable = false))
    })
    private Schedule availability;

    public Trainer() {}

    public Trainer(String name, String email, String dayOfWeek, int startTime, int endTime) {
        this.name = name;
        this.email = email;
        this.availability = new Schedule(DayOfWeek.fromString(dayOfWeek), startTime, endTime);
    }

    // Getters and setters
    public Long getId() { return trainerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Schedule getAvailability() { return availability; }
    public void setAvailability(String dayOfWeek, int startTime, int endTime) { 
        this.availability = new Schedule(DayOfWeek.valueOf(dayOfWeek), startTime, endTime); 
    }

    public String toString() {
        return trainerId + "\t" + name + "\t" + email + "\t" 
            + availability.getDayOfWeek().toString() + "\t"+ availability.getStartTime() + availability.getEndTime();
    }
}
