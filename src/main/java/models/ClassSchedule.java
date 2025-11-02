package models;

import java.time.DayOfWeek;

import jakarta.persistence.*;

@Entity
@Table(name = "class_schedule")
public class ClassSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "class_id", referencedColumnName = "classId", foreignKey = @ForeignKey(name = "FK_classSchedule_class"))
    private GroupFitnessClass groupFitnessClass;

    @ManyToOne(optional = false)
    @JoinColumn(name = "admin_id", referencedColumnName = "adminId", foreignKey = @ForeignKey(name = "FK_classSchedule_admin"))
    private Admin admin;

    @OneToOne(mappedBy = "classSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private ClassScheduleDetails details;

    public ClassSchedule() {
    }

    public ClassSchedule(GroupFitnessClass groupFitnessClass, Admin admin) {
        this.groupFitnessClass = groupFitnessClass;
        this.admin = admin;
    }

    // --- Helper method to set details ---
    public void setDetails(int roomNum, String dayOfWeek, int startTime, int endTime) {
        ClassScheduleDetails newDetails = new ClassScheduleDetails(this, roomNum, dayOfWeek, startTime, endTime);
        this.details = newDetails;
    }

    public void setDetails(int roomNum, DayOfWeek dayOfWeek, int startTime, int endTime) {
        ClassScheduleDetails newDetails = new ClassScheduleDetails(this, roomNum, dayOfWeek, startTime, endTime);
        this.details = newDetails;
    }

    // Getters and setters
    public Long getScheduleId() {
        return scheduleId;
    }

    public GroupFitnessClass getGroupFitnessClass() {
        return groupFitnessClass;
    }

    public void setGroupFitnessClass(GroupFitnessClass groupFitnessClass) {
        this.groupFitnessClass = groupFitnessClass;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public ClassScheduleDetails getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return "ClassSchedule [scheduleId=" + scheduleId + ", classId=" + groupFitnessClass.getClassId()
                + ", adminId=" + admin.getAdminId() + "]";
    }
}
