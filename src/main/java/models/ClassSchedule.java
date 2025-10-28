package models;

import java.time.DayOfWeek;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

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

    @NotNull
    @Column(nullable = false)
    private int roomNum;

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dayOfWeek", column = @Column(name = "schedule_Day", nullable = false)),
            @AttributeOverride(name = "startTime", column = @Column(name = "schedule_StartTime", nullable = false)),
            @AttributeOverride(name = "endTime", column = @Column(name = "schedule_EndTime", nullable = false))
    })
    private Schedule scheduleTime;

    @NotNull
    @Column(nullable = false)
    private int capacity;

    public ClassSchedule() {
    }

    public ClassSchedule(GroupFitnessClass groupFitnessClass, Admin admin, int roomNum, DayOfWeek dayOfWeek,
            int startTime, int endTime, int capacity) {
        this.groupFitnessClass = groupFitnessClass;
        this.admin = admin;
        this.roomNum = roomNum;
        this.scheduleTime = new Schedule(dayOfWeek, startTime, endTime);
        this.capacity = capacity;
    }

    public ClassSchedule(GroupFitnessClass groupFitnessClass, Admin admin, int roomNum, String dayOfWeek,
            int startTime, int endTime, int capacity) {
        this.groupFitnessClass = groupFitnessClass;
        this.admin = admin;
        this.roomNum = roomNum;
        this.scheduleTime = new Schedule(dayOfWeek, startTime, endTime);
        this.capacity = capacity;
    }

    // --- Getters and Setters ---
    public Long getClassMemberId() {
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

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public Schedule getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String dayOfWeek, int startTime, int endTime) {
        this.scheduleTime = new Schedule(dayOfWeek, startTime, endTime);
    }

    public void setScheduleTime(DayOfWeek dayOfWeek, int startTime, int endTime) {
        this.scheduleTime = new Schedule(dayOfWeek, startTime, endTime);
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
