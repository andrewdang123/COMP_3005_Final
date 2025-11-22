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
 * EquipmentManagementDetails stores the detailed info for an equipment issue:
 * - detailsId is the primary key (auto-generated); the DB will create a PK index
 *   on this column so lookups by detailsId are efficient.
 * - equipment is a @OneToOne link back to EquipmentManagement via equipment_id
 *   with a foreign key constraint; joins or filters on equipment_id can use that FK index.
 * - roomNum, issue, and repairStatus capture where the equipment is and what’s wrong.
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
