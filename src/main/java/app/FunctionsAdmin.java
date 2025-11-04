package app;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import org.hibernate.Session;

import models.Admin;
import models.EquipmentManagement;
import models.EquipmentManagementDetails;

public class FunctionsAdmin {

    /***************************************************************
     * retrieveAdmin
     ***************************************************************/
    public static Admin retrieveAdmin(Session session) {
        Scanner scanner = HibernateUtil.getScanner();

        try {
            System.out.println("\n===================== Existing Admins ===================== ");
            var admins = session.createQuery("from Admin", Admin.class).list();

            if (admins.isEmpty()) {
                System.out.println("No admins found in the system");
                return null;
            }

            for (Admin a : admins) {
                System.out.println(a.toString());
            }
            System.out.println("===========================================================");

            Admin admin = null;
            boolean found = false;

            while (!found) {
                System.out.print("\nEnter the Admin ID: ");
                Long adminId = Long.parseLong(scanner.nextLine().trim());

                admin = session.get(Admin.class, adminId);

                if (admin == null) {
                    System.out.println("\nNo admin found with ID: " + adminId);
                    System.out.println("1. Retry");
                    System.out.println("2. Quit");
                    System.out.print("Enter your choice: ");
                    int choice = Integer.parseInt(scanner.nextLine().trim());

                    if (choice == 2) {
                        System.out.println("Returning to main menu...");
                        return null;
                    }
                } else {
                    found = true;
                }
            }

            return admin;

        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            return null;
        }
    }

    /***************************************************************
     * adminEquipmentMaintenance
     ***************************************************************/
    public static void adminEquipmentMaintenance() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Scanner scanner = HibernateUtil.getScanner();

        try {
            Admin admin = retrieveAdmin(session);
            if (admin == null) {return;}
    
            // Submenu loop
            while (true) {
                System.out.println("\nDo you want to:");
                System.out.println("0. Exit");
                System.out.println("1. Update/View details");
                System.out.println("2. Add New Issue");
                System.out.print("Enter choice (0 - 2): ");

                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1":
                    EquipmentManagement equipmentManagement = FunctionsExtra.retrieveEquipmentManagement(session);
                    if (equipmentManagement == null) {break;}
                        equipViewAndUpdate(session, scanner);
                        break;

                    case "2":
                        equipAddIssue(session, scanner, admin);
                        break;

                    case "0":
                        System.out.println("Returning...");
                        return;

                    default:
                        System.out.println("Invalid choice.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error updating profile: " + e.getMessage());
        } finally {
            session.close();
        }
    }
    /***************************************************************
     * update/view equipment details
     ***************************************************************/
    private static void equipViewAndUpdate(Session session, Scanner scanner) {
        while (true) {
            // any details not resolved
            List<EquipmentManagementDetails> list = session.createQuery(
                "FROM EquipmentManagementDetails d WHERE d.repairStatus IS NULL OR LOWER(d.repairStatus) <> 'resolved'",
                EquipmentManagementDetails.class
            ).list();

            System.out.println("\n=== Equipment with Current Issues (from details) ===");
            if (list.isEmpty()) {
                System.out.println("No equipment currently flagged with issues.");
                return;
            }

            System.out.printf("%-12s %-8s %-40s %-18s%n", "equipmentId", "Room", "Issue", "RepairStatus");
            System.out.println("--------------------------------------------------------------------------------------");
            for (EquipmentManagementDetails d : list) {
                Long eqId = (d.getEquipment() == null) ? null : d.getEquipment().getEquipmentId();
                System.out.printf("%-12s %-8s %-40s %-18s%n",
                    eqId == null ? "-" : String.valueOf(eqId),
                    String.valueOf(d.getRoomNum()),
                    Objects.toString(d.getIssue()),
                    Objects.toString(d.getRepairStatus()));
            }

            System.out.println("\nEnter an equipmentId to update, or 0 to go back.");
            System.out.print("Choice: ");

            String raw = scanner.nextLine().trim();
            long equipmentId;
            try {
                equipmentId = Long.parseLong(raw);
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid input.");
                continue;
            }
            if (equipmentId == 0) return;

            // Find details row by equipmentId (join via the relation)
            EquipmentManagementDetails selected = session.createQuery(
                "FROM EquipmentManagementDetails d WHERE d.equipment.equipmentId = :eid",
                EquipmentManagementDetails.class
            ).setParameter("eid", equipmentId).uniqueResult();

            if (selected == null) {
                System.out.println("No details found for equipmentId " + equipmentId + ".");
                continue;
            }

            // ----- collect updates -----
            try {
                session.refresh(selected);

                System.out.println("\nUpdating equipmentId " + equipmentId +
                    " – current Issue: " + Objects.toString(selected.getIssue()) +
                    ", Status: " + Objects.toString(selected.getRepairStatus()));
                System.out.println("(Press Enter to skip any field)");

                System.out.print("New RepairStatus (e.g., In progress / Out of service / Resolved): ");
                String newStatus = scanner.nextLine().trim();

                System.out.print("New/Updated Issue description: ");
                String newIssue = scanner.nextLine().trim();

                System.out.print("Room number (int): ");
                String roomRaw = scanner.nextLine().trim();

                session.beginTransaction();

                if (!newStatus.isEmpty()) selected.setRepairStatus(newStatus);
                if (!newIssue.isEmpty())  selected.setIssue(newIssue);
                if (!roomRaw.isEmpty()) {
                    try {
                        selected.setRoomNum(Integer.parseInt(roomRaw));
                    } catch (NumberFormatException bad) {
                        System.out.println("  (Skipped invalid room number: " + roomRaw + ")");
                    }
                }

                session.merge(selected);
                session.getTransaction().commit();
                System.out.println("Update complete.");
                // loop re-lists with fresh data
            } catch (Exception ex) {
                if (session.getTransaction().isActive()) session.getTransaction().rollback();
                System.out.println("Failed to update details: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    /***************************************************************
     * add equipment issues
     ***************************************************************/
    private static void equipAddIssue(Session session, Scanner scanner, Admin admin) {
        try {
            System.out.print("Room number (int): ");
            int room = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Issue/Description: ");
            String issue = scanner.nextLine().trim();

            System.out.print("RepairStatus (default: In progress): ");
            String status = scanner.nextLine().trim();
            if (status.isEmpty()) status = "In progress";

            session.beginTransaction();

            // parent EquipmentManagement row to satisfy FK
            EquipmentManagement parent = new EquipmentManagement(admin);
            session.persist(parent);

            EquipmentManagementDetails det = new EquipmentManagementDetails(parent, room, issue, status);
            session.persist(det);

            session.getTransaction().commit();

            System.out.println("New issue recorded for equipmentId " + parent.getEquipmentId() + ".");
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid room number. Cancelled.");
        } catch (Exception e) {
            if (session.getTransaction().isActive()) session.getTransaction().rollback();
            System.out.println("Failed to add issue: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /***************************************************************
     * adminClassManagement
     ***************************************************************/
    public static void adminClassManagement() {
        System.out.println("Classes Managed!");
    }

}
