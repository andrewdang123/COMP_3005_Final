package models;

import java.time.DayOfWeek;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * ClassScheduleDetails stores the extra info for a ClassSchedule:
 * - Uses @MapsId so scheduleId is both:
 *   • the primary key of this table, and
 *   • a foreign key back to ClassSchedule (shared PK 1–1 relationship).
 *   → The DB will have an index on schedule_id as the primary key, which also
 *     makes joins on schedule_id between class_schedule and class_schedule_details fast.
 * - Embeds a Schedule object and maps its fields to concrete columns:
 *   schedule_day, schedule_start_time, schedule_end_time.
 * - Holds the room number and the exact day/time window for the class.
 */

@Entity
@Table(name = "class_schedule_details")
public class ClassScheduleDetails {

    @Id
    private Long scheduleId;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "schedule_id", referencedColumnName = "scheduleId", foreignKey = @ForeignKey(name = "FK_classScheduleDetails_schedule"))
    private ClassSchedule classSchedule;

    @NotNull
    @Column(nullable = false)
    private int roomNum;

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dayOfWeek", column = @Column(name = "schedule_day", nullable = false)),
            @AttributeOverride(name = "startTime", column = @Column(name = "schedule_start_time", nullable = false)),
            @AttributeOverride(name = "endTime", column = @Column(name = "schedule_end_time", nullable = false))
    })
    private Schedule scheduleTime;

    public ClassScheduleDetails() {
    }

    public ClassScheduleDetails(ClassSchedule classSchedule, int roomNum, String dayOfWeek, int startTime,
            int endTime) {
        this.classSchedule = classSchedule;
        this.roomNum = roomNum;
        this.scheduleTime = new Schedule(dayOfWeek, startTime, endTime);
    }

    public ClassScheduleDetails(ClassSchedule classSchedule, int roomNum, DayOfWeek dayOfWeek, int startTime,
            int endTime) {
        this.classSchedule = classSchedule;
        this.roomNum = roomNum;
        this.scheduleTime = new Schedule(dayOfWeek, startTime, endTime);
    }

    // Convenience getter to display the classId
    public Long getClassId() {
        return classSchedule.getGroupFitnessClass().getClassId();
    }

    // Getters and setters
    public Long getScheduleId() {
        return scheduleId;
    }

    public ClassSchedule getClassSchedule() {
        return classSchedule;
    }

    public void setClassSchedule(ClassSchedule classSchedule) {
        this.classSchedule = classSchedule;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public Schedule getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(Schedule scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    @Override
    public String toString() {
        return "ClassScheduleDetails"
                + "classId=" + getClassId()
                + ", roomNum=" + roomNum
                + ", scheduleTime=" + scheduleTime + "]";
    }
}
