package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public Member() {}

    public Member(String name, String gender, String email, int day, int month, int year) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.dateOfBirth = new DateOfBirth(day, month, year);
    }

    // Getters and setters
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public DateOfBirth getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(int day, int month, int year) { 
        this.dateOfBirth = new DateOfBirth(day, month, year);
    }

}
