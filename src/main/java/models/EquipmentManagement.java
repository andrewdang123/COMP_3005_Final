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
 * EquipmentManagement represents a high-level equipment issue record:
 * - equipmentId is the primary key (auto-generated); the DB will create a PK index
 *   on this column so lookups by ID are fast.
 * - admin is a @ManyToOne link to Admin, stored as admin_id with a foreign key
 *   constraint; queries that filter or join on admin_id can use the FK index.
 * - details is a @OneToOne to EquipmentManagementDetails, which stores the room,
 *   issue description, and repair status as a separate, normalized row.
 * - setDetails(...) is a helper that builds the details object and ties it back
 *   to this EquipmentManagement.
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
