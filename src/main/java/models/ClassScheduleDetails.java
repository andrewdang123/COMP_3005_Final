package models;

import java.time.DayOfWeek;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "class_schedule_details")
public class ClassScheduleDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailsId;

    @OneToOne(optional = false)
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

    @NotNull
    @Column(nullable = false)
    private int capacity;

    public ClassScheduleDetails() {
    }

    public ClassScheduleDetails(ClassSchedule classSchedule, int roomNum, String dayOfWeek, int startTime, int endTime,
            int capacity) {
        this.classSchedule = classSchedule;
        this.roomNum = roomNum;
        this.scheduleTime = new Schedule(dayOfWeek, startTime, endTime);
        this.capacity = capacity;
    }

    public ClassScheduleDetails(ClassSchedule classSchedule, int roomNum, DayOfWeek dayOfWeek, int startTime,
            int endTime,
            int capacity) {
        this.classSchedule = classSchedule;
        this.roomNum = roomNum;
        this.scheduleTime = new Schedule(dayOfWeek, startTime, endTime);
        this.capacity = capacity;
    }

    // Getters and setters
    public Long getDetailsId() {
        return detailsId;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "ClassScheduleDetails [scheduleId=" + classSchedule.getScheduleId() + ", roomNum=" + roomNum
                + ", scheduleTime=" + scheduleTime + ", capacity=" + capacity + "]";
    }
}
