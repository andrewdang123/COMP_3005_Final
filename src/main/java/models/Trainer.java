package models;

import jakarta.persistence.*;
import java.util.*;

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

    public void removeAvailability(TrainerAvailability availability) {
        availabilities.remove(availability);
        availability.setTrainer(null);
    }

    public String toString() {
        return trainerId + "\t" + name + "\t" + email;
    }
}
