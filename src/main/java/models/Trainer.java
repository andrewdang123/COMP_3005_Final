package models;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Trainer represents a personal trainer.
 * - trainerId is the PK (indexed) and email is UNIQUE (unique index).
 * - Holds a list of availability slots (TrainerAvailability) used to decide
 *   when the trainer is free.
 *
 * Scheduling notes:
 * - ClassSchedule and PersonalTrainingSession use @PrePersist/@PreUpdate to
 *   check availability before booking.
 * - Availability is updated or restored through FunctionsTrainer, making this
 *   the main source of truth for the trainer’s free time.
 */

@Entity
@Table(name = "trainer")
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainerAvailability> availabilities = new ArrayList<>();

    public Trainer() {
    }

    public Trainer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getters and setters
    public Long getTrainerId() {
        return trainerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<TrainerAvailability> getAvailabilities() {
        return availabilities;
    }

    public void addAvailability(TrainerAvailability availability) {
        availabilities.add(availability);
        availability.setTrainer(this);
    }

    public void removeAvailability(Session session, TrainerAvailability availability) {
        if (availability == null) return;
        this.availabilities.remove(availability);
        availability.setTrainer(null);
        session.remove(availability);
    }

    public void printAvailabilities() {
        System.out.println("\n----------------- Trainer Availability -----------------");
        for (var avail : this.getAvailabilities()) {
            System.out.println(
                    avail.getTrainerAvailabilityId() + ": " + this.getName() + " available on " + avail.getDayOfWeek()
                            + " from " + avail.getStartTime() + " to " + avail.getEndTime());
        }
        System.out.println("--------------------------------------------------------");
    }

    public String toString() {
        return "ID: " + getTrainerId() + " | Name: " + getName() + " | Email: " + getEmail();
    }
}
