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
    
    @Column(nullable = false)
    private String gender;

    @Column(nullable = false, unique = true)
    private String email;

    // @NotNull
    // @Embedded
    // private DateOfBirth dateOfBirth;

    public Member() {}

    public Member(String name, String gender, String email) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        //this.dateOfBirth = new DateOfBirth(day, month, year);
    }

    // Getters and setters
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // public DateOfBirth getDateOfBirth() { return dateOfBirth; }
    // public void setDateOfBirth(int day, int month, int year) { 
    //     this.dateOfBirth.setDay(day);
    //     this.dateOfBirth.setMonth(month);
    //     this.dateOfBirth.setYear(year);
    // }

}
