package models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * Member stores basic user info.
 * - memberId is the PK (indexed) and email has a UNIQUE index.
 * - dateOfBirth is embedded into this table.
 * - targetWeight/targetBmi store fitness goals.
 * - Linked to HealthMetric through a 1–many FK (indexed), which is used for
 * queries like showing a member’s latest health metric.
 */

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String gender;

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "day", column = @Column(name = "birth_day", nullable = false)),
            @AttributeOverride(name = "month", column = @Column(name = "birth_month", nullable = false)),
            @AttributeOverride(name = "year", column = @Column(name = "birth_year", nullable = false))
    })
    private DateOfBirth dateOfBirth;

    @Column
    private int targetWeight;

    @Column
    private int targetBmi;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HealthMetric> healthMetrics = new ArrayList<>();

    public Member() {
    }

    public Member(String name, String email, String gender, int day, int month, int year) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = new DateOfBirth(day, month, year);
        this.targetWeight = 0;
        this.targetBmi = 0;
    }

    // --- Getters and setters ---
    public Long getMemberId() {
        return memberId;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public DateOfBirth getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(int day, int month, int year) {
        this.dateOfBirth = new DateOfBirth(day, month, year);
    }

    public int getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(int targetWeight) {
        this.targetWeight = targetWeight;
    }

    public int getTargetBmi() {
        return targetBmi;
    }

    public void setTargetBmi(int targetBmi) {
        this.targetBmi = targetBmi;
    }

    public List<HealthMetric> getHealthMetrics() {
        return healthMetrics;
    }

    public void addHealthMetric(HealthMetric metric) {
        healthMetrics.add(metric);
        metric.setMember(this);
    }

    public void addHealthMetric(int currentWeight, int currentBmi) {
        HealthMetric metric = new HealthMetric(this, currentWeight, currentBmi);
        healthMetrics.add(metric);
        metric.setMember(this);
    }

    public void removeHealthMetric(HealthMetric metric) {
        healthMetrics.remove(metric);
        metric.setMember(null);
    }

    public void memberPrint() {
        System.out.println("\n--- Current Profile ---");
        System.out.println("Member ID: " + this.getMemberId());
        System.out.println("Name: " + this.getName());
        System.out.println("Email: " + this.getEmail());
        System.out.println("Gender: " + this.getGender());
        System.out.println("Date of Birth: " + this.getDateOfBirth().getDay() + "/"
                + this.getDateOfBirth().getMonth() + "/"
                + this.getDateOfBirth().getYear());
        System.out.println("Health Metrics Count: " + this.getHealthMetrics().size());
        System.out.println("-----------------------");
    }

    @Override
    public String toString() {
        return "ID: " + getMemberId() + " | Name: " + getName() + " | Email: " + getEmail();
    }
}
