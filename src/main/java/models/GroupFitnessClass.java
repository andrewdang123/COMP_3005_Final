package models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * GroupFitnessClass models a specific group class (e.g. "Strength 101"):
 * - classId is the primary key; the DB creates a PK index on this, used whenever
 *   we look up or join by class ID.
 * - trainer is a @ManyToOne FK to Trainer stored as trainer_id; queries that find
 *   all classes for a trainer (e.g. in trainerScheduleView) use the FK index on
 *   trainer_id for fast lookups.
 * - currentMembers + capacity track how many people are in this class and its limit.
 *   In the database, there is a trigger on GroupFitnessClassMembers that automatically
 *   updates currentMembers whenever a row is inserted/deleted, so the count stays in
 *   sync with the membership table.
 * - members is a @OneToMany collection of GroupFitnessClassMembers (the join table
 *   between this class and Member).
 * - addMember(...) enforces capacity and “no duplicates” in memory; the actual insert
 *   of GroupFitnessClassMembers (done elsewhere) will fire the trigger that increments
 *   currentMembers in the DB.
 * - incrementMembers()/decrementMembers() are helper methods if we ever want to
 *   manage the count purely from the application side.
 */

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

    @NotNull
    @Column(nullable = false)
    private int capacity;

    @OneToMany(mappedBy = "groupFitnessClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GroupFitnessClassMembers> members = new HashSet<>();

    public GroupFitnessClass() {
    }

    public GroupFitnessClass(Trainer trainer, String className) {
        this.trainer = trainer;
        this.className = className;
        this.currentMembers = 0;
        this.capacity = 5;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean addMember(Member member) {
        // Check if class is already full
        if (currentMembers >= capacity) {
            System.out.println("Cannot add member. Class \"" + className + "\" is already full.");
            return false;
        }

        // Check if member already exists in this class
        boolean alreadyExists = members.stream()
                .anyMatch(m -> m.getMember().equals(member));
        if (alreadyExists) {
            System.out.println("Member is already registered in this class.");
            return false;
        }

        // Add new member
        GroupFitnessClassMembers classMember = new GroupFitnessClassMembers(this, member);
        members.add(classMember);
        return true;
    }

    public boolean removeMember(Member member) {
        GroupFitnessClassMembers toRemove = members.stream()
                .filter(m -> m.getMember().equals(member))
                .findFirst()
                .orElse(null);

        if (toRemove != null) {
            members.remove(toRemove);
            decrementMembers();
            System.out.println("Member removed from class: " + className);
            return true;
        }

        System.out.println("Member not found in this class.");
        return false;
    }

    public Set<GroupFitnessClassMembers> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        return "ID: " + getClassId() + " | Name: " + getClassName() + " | Trainer: " + trainer.getName();
    }

}
