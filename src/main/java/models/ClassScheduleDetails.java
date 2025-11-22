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
 * ClassScheduleDetails adds room + time info to a ClassSchedule.
 * - @MapsId: scheduleId is both the PK and an FK to ClassSchedule (shared 1–1).
 *   → Creates a PK index on schedule_id, making joins very fast.
 * - Embeds a Schedule (day + start/end time) as concrete columns.
 * - Stores the room number and the exact timing for the class session.
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
