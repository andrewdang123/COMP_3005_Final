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


// DB INDEX NOTE:
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
     * ENTITY “TRIGGER” NOTE (PrePersist):
     * - Runs automatically right before INSERT of a ClassSchedule.
     * - Reads the trainer + day + time from the associated GroupFitnessClass and details.
     * - Uses FunctionsTrainer.trainerCheckAvailability(...) to validate that the trainer
     *   is free for that slot; if not, throws RuntimeException and cancels the insert
     *   (the transaction will roll back).
     * - Calls FunctionsTrainer.trainerAdjustAvailability(...) to update the trainer’s
     *   in-memory availability so the booked time is removed, keeping availability consistent.
     * - This acts like an application-level trigger enforcing “no double-booking” for trainers.
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
     * ENTITY “TRIGGER” NOTE (PreUpdate):
     * - Runs automatically right before UPDATE of an existing ClassSchedule.
     * - Re-validates the trainer’s availability for the (possibly new) day/time.
     * - If the trainer is not available, throws RuntimeException so the update is blocked.
     * - Calls trainerAdjustAvailability(...) again to adjust the trainer availability model
     *   after a reschedule, keeping the trainer’s calendar in sync with this schedule.
     * - Just like PrePersist, this is an application-level trigger on UPDATE.
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
