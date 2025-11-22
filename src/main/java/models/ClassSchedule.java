package models;

import java.time.DayOfWeek;

import app.FunctionsTrainer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;


    // - Creates a UNIQUE index on (class_id, schedule_id).
    // - Prevents duplicate schedule rows for the same class/schedule.
    // - Speeds up lookups/join queries that filter by class_id + schedule_id.

@Entity
@Table(name = "class_schedule", indexes = {
        @Index(name = "uq_class_schedule_class_schedule", columnList = "class_id, schedule_id", unique = true)
})
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

    /**
     * PrePersist “trigger” for ClassSchedule:
     * - Fires before INSERT.
     * - Checks trainer availability for the requested day/time.
     * - If unavailable → throws an error and the INSERT is cancelled.
     * - If available → removes that time slot from trainer availability.
     * - Works like an application-level trigger that prevents trainer double-booking.
     */

    @PrePersist
    public void beforeInsert() {
        Trainer trainer = this.getGroupFitnessClass().getTrainer();
        String dayOfWeek = this.getDetails().getScheduleTime().getDayOfWeek().toString();
        int startTime = this.getDetails().getScheduleTime().getStartTime().getHour();
        int endTime = this.getDetails().getScheduleTime().getEndTime().getHour();
        if (!FunctionsTrainer.trainerCheckAvailability(trainer, dayOfWeek, startTime, endTime)) {
            throw new RuntimeException("Trainer " + trainer.getName() + " unavailable at that time");
        }
        FunctionsTrainer.trainerAdjustAvailability(trainer, dayOfWeek, startTime, endTime);
    }

    /**
     * PreUpdate “trigger” for ClassSchedule:
     * - Fires before UPDATE.
     * - Re-checks trainer availability for the new time slot.
     * - Blocks the update if the trainer is unavailable.
     * - Adjusts trainer availability to reflect the updated schedule.
     * - Acts as an application-level trigger preventing double-booking on updates.
     */

    @PreUpdate
    public void beforeUpdate() {
        Trainer trainer = this.getGroupFitnessClass().getTrainer();
        String dayOfWeek = this.getDetails().getScheduleTime().getDayOfWeek().toString();
        int startTime = this.getDetails().getScheduleTime().getStartTime().getHour();
        int endTime = this.getDetails().getScheduleTime().getEndTime().getHour();
        if (!FunctionsTrainer.trainerCheckAvailability(trainer, dayOfWeek, startTime, endTime)) {
            throw new RuntimeException("Trainer " + trainer.getName() + " unavailable at that time");
        }
        FunctionsTrainer.trainerAdjustAvailability(trainer, dayOfWeek, startTime, endTime);
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
