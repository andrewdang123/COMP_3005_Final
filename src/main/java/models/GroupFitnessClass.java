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
        System.out.println("Member added successfully to class: " + className);
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
        return "ID: " + getClassId() + " | Name: " + getClassName();
    }

}
