package models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * EquipmentManagementDetails holds the detailed info for an equipment issue.
 * - detailsId is the PK (indexed).
 * - equipment_id is a @OneToOne FK back to EquipmentManagement (indexed for fast joins).
 * - Stores roomNum, issue, and repairStatus for the equipment report.
 */

@Entity
@Table(name = "equipment_management_details")
public class EquipmentManagementDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailsId;

    @OneToOne(optional = false)
    @JoinColumn(name = "equipment_id", referencedColumnName = "equipmentId", foreignKey = @ForeignKey(name = "FK_equipmentDetails_equipment"))
    private EquipmentManagement equipment;

    @NotNull
    @Column(nullable = false)
    private int roomNum;

    @Column
    private String issue;

    @NotNull
    @Column(nullable = false)
    private String repairStatus;

    public EquipmentManagementDetails() {
    }

    public EquipmentManagementDetails(EquipmentManagement equipment, int roomNum, String issue, String repairStatus) {
        this.equipment = equipment;
        this.roomNum = roomNum;
        this.issue = issue;
        this.repairStatus = repairStatus;
    }

    // Getters and setters
    public Long getDetailsId() {
        return detailsId;
    }

    public EquipmentManagement getEquipment() {
        return equipment;
    }

    public void setEquipment(EquipmentManagement equipment) {
        this.equipment = equipment;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getRepairStatus() {
        return repairStatus;
    }

    public void setRepairStatus(String repairStatus) {
        this.repairStatus = repairStatus;
    }

    @Override
    public String toString() {
        return "EquipmentManagementDetails [equipmentId=" + equipment.getEquipmentId() + ", roomNum=" + roomNum
                + ", issue=" + issue + ", repairStatus=" + repairStatus + "]";
    }
}
