package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "group_fitness_classes")
public class GroupFitnessClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classId;

    @ManyToOne
    @JoinColumn(name = "trainer_id", referencedColumnName = "trainerId", foreignKey = @ForeignKey(name = "FK_groupFitnessClass_Trainer"))
    private Trainer trainer;

    @NotNull
    @Column(nullable = false)
    private String className;

    @NotNull
    @Column(nullable = false)
    private int currentMembers;

    @OneToMany(mappedBy = "groupFitnessClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GroupFitnessClassMembers> members = new HashSet<>();

    public GroupFitnessClass() {
    }

    public GroupFitnessClass(Trainer trainer, String className) {
        this.trainer = trainer;
        this.className = className;
        this.currentMembers = 0;
    }

    // --- Safe counter adjustment ---
    public void incrementMembers() {
        currentMembers++;
    }

    public void decrementMembers() {
        if (currentMembers > 0)
            currentMembers--;
    }

    // --- Getters and Setters ---
    public Long getClassId() {
        return classId;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getCurrentMembers() {
        return currentMembers;
    }

    public Set<GroupFitnessClassMembers> getMembers() {
        return members;
    }
}
