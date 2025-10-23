package models;

import jakarta.persistence.*;


@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;

    public Admin() {}

    public Admin(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getters and setters
    public Long getId() { return adminId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String toString() {
        return adminId + "\t" + name + "\t" + email;
    }

}
