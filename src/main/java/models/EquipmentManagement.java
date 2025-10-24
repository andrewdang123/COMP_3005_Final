package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "equipment_management")
public class EquipmentManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long equipmentId;

    @ManyToOne(optional = false)
    @JoinColumn(
        name = "admin_id", 
        referencedColumnName = "adminId",
        foreignKey = @ForeignKey(name = "FK_equipmentManagement_admin")
    )
    private Admin admin;

    @NotNull
    @Column(nullable = false)
    private int roomNum;

    @Column(nullable = true)
    private String issue;

    @NotNull
    @Column(nullable = false)
    private String repairStatus;

    public EquipmentManagement() {
    }

    public EquipmentManagement(Admin admin, int roomNum, String issue, String repairStatus) {
        this.admin = admin;
        this.roomNum = roomNum;
        this.issue = issue;
        this.repairStatus = repairStatus;
    }

    // Getters and Setters
    public Long getEquipmentId() { return equipmentId; }

    public Admin getAdmin() { return admin; }
    public void setAdmin(Admin admin) { this.admin = admin; }

    public int getRoomNum() { return roomNum; }
    public void setRoomNum(int roomNum) { this.roomNum = roomNum; }

    public String getIssue() { return issue; }
    public void setIssue(String issue) { this.issue = issue; }

    public String getRepairStatus() { return repairStatus; }
    public void setRepairStatus(String repairStatus) { this.repairStatus = repairStatus; }

    @Override
    public String toString() {
        return equipmentId + "\t" + admin.getAdminId() + "\t" + roomNum + "\t" + issue + "\t" + repairStatus;
    }
}
