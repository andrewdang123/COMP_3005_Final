package models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * EquipmentManagement is the main equipment issue record.
 * - equipmentId is the PK (indexed).
 * - admin_id is a @ManyToOne FK to Admin (indexed for fast filtering by admin).
 * - details is a @OneToOne child row storing room, issue text, and repair status.
 * - setDetails(...) creates and links the details entry to this equipment record.
 */

@Entity
@Table(name = "equipment_management")
public class EquipmentManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long equipmentId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "admin_id", referencedColumnName = "adminId", foreignKey = @ForeignKey(name = "FK_equipmentManagement_admin"))
    private Admin admin;

    @OneToOne(mappedBy = "equipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private EquipmentManagementDetails details;

    public EquipmentManagement() {
    }

    public EquipmentManagement(Admin admin) {
        this.admin = admin;
    }

    // Getters and setters
    public Long getEquipmentId() {
        return equipmentId;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public EquipmentManagementDetails getDetails() {
        return details;
    }

    public void setDetails(int roomNum, String issue, String repairStatus) {
        EquipmentManagementDetails newDetails = new EquipmentManagementDetails(this, roomNum, issue, repairStatus);
        this.details = newDetails;
    }
    public void setDetails(EquipmentManagementDetails details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "ID: " + equipmentId + " | Last updated by Admin: " + admin.getName() + " | Room Num: "
                + details.getRoomNum()
                + " | Issue: " + details.getIssue() + " | Status: " + details.getRepairStatus();
    }
}
