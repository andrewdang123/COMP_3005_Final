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
 * Trainer represents a personal trainer in the system:
 * - trainerId is the primary key; the DB creates a PK index on this column.
 *   → Used in joins and lookups like “find all classes or PT sessions for this trainer”.
 * - email is marked unique = true; this creates a UNIQUE index on email so:
 *   → the DB enforces one trainer per email,
 *   → email lookups are efficient if used in queries.
 * - availabilities is the 1–many list of TrainerAvailability rows for this trainer;
 *   those rows store day/time ranges the trainer is free.
 *
 * How this ties into “triggers” and scheduling:
 * - The booking logic for both:
 *     • Group classes (`ClassSchedule` @PrePersist/@PreUpdate), and
 *     • Personal training sessions (`PersonalTrainingSession` @PrePersist/@PreUpdate)
 *   uses the trainer’s availabilities list to check if a requested slot is free
 *   (via FunctionsTrainer.trainerCheckAvailability).
 * - When a slot is booked, those same callbacks adjust the availability data
 *   (FunctionsTrainer.trainerAdjustAvailability / trainerRestoreAvailability),
 *   so this Trainer entity is the central source of truth for what times are free.
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
