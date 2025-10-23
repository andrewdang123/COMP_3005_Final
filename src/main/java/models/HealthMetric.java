package models;

import java.time.LocalDateTime;

import jakarta.persistence.*;


@Entity
@Table(name = "health_metrics")
public class HealthMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long metricId;

    @Column(nullable = false)
    private Long memberId;
    
    @Column(nullable = false)
    private int currentWeight;

    @Column(nullable = false)
    private int currentBmi;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public HealthMetric() {}

    public HealthMetric(Long memberId, int currentWeight, int currentBmi) {
        this.memberId = memberId;
        this.currentWeight = currentWeight;
        this.currentBmi = currentBmi;
        timestamp = LocalDateTime.now();
    }

    // Getters and setters
    /* 
    public Long getId() { return memberId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public DateOfBirth getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(int day, int month, int year) { 
        this.dateOfBirth = new DateOfBirth(day, month, year);
    }

    public int getTargetWeight() { return targetWeight; }
    public void setTargetWeight(int targetWeight) { this.targetWeight = targetWeight; }

    public int getTargetBmi() { return targetBmi; }
    public void setTargetBmi(int targetBmi) { this.targetBmi = targetBmi; }

    public String toString() {
        return memberId + "\t" + name + "\t" + email + "\t" + gender + "\t" + dateOfBirth.getDay() + "\t" + dateOfBirth.getMonth() + "\t" + dateOfBirth.getYear();
    }
        */

}
