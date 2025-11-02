package models;

import jakarta.persistence.*;

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

    @Override
    public String toString() {
        return "EquipmentManagement [equipmentId=" + equipmentId + ", adminId=" + admin.getAdminId() + "]";
    }
}
