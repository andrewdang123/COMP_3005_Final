package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "equipment_management")
public class EquipmentManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long equipmentId;

    @NotNull
    @Column(nullable = false)
    private Long adminId;

    @NotNull
    @Column(nullable = false)
    private int roomNum;

    @Column(nullable = true)
    private String issues;

    @NotNull
    @Column(nullable = false)
    private String repairStatus;

    public EquipmentManagement() {
    }

    public EquipmentManagement(Long adminId, int roomNum, String issues, String repairStatus) {
        this.adminId = adminId;
        this.roomNum = roomNum;
        this.issues = issues;
        this.repairStatus = repairStatus;
    }

    // Getters and Setters
    public Long getEquipmentId() {
        return equipmentId;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public String getIssues() {
        return issues;
    }

    public void setIssues(String issues) {
        this.issues = issues;
    }

    public String getRepairStatus() {
        return repairStatus;
    }

    public void setRepairStatus(String repairStatus) {
        this.repairStatus = repairStatus;
    }

    @Override
    public String toString() {
        return equipmentId + "\t" + adminId + "\t" + roomNum + "\t" + issues + "\t" + repairStatus;
    }
}
