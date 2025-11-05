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
            if (admin == null) {
                return;
            }

            // Submenu loop
            while (true) {
                System.out.println("\nDo you want to:");
                System.out.println("0. Exit");
                System.out.println("1. Update/View reports");
                System.out.println("2. Report another Issue");
                System.out.print("Enter choice (0 - 2): ");

                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1":
                        adminEquipmentMaintenanceUpdate(session, admin);
                        break;
                    case "2":
                        adminEquipmentMaintenanceAdd(session, admin);
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
     * adminEquipmentMaintenanceUpdate
     ***************************************************************/
    private static void adminEquipmentMaintenanceUpdate(Session session, Admin admin) {
        Scanner scanner = HibernateUtil.getScanner();
        try {
            EquipmentManagement equipmentManagement = FunctionsExtra.retrieveEquipmentManagement(session);
            if (equipmentManagement == null) {
                System.out.println("No equipment found to update.");
                return;
            }

            // Validate room number input
            Integer roomNum = null;
            while (roomNum == null) {
                System.out.print("Room number: ");
                String input = scanner.nextLine().trim();
                try {
                    roomNum = Integer.parseInt(input);
                    if (roomNum <= 0) {
                        System.out.println("Room number must be a positive integer.");
                        roomNum = null;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid integer room number.");
                }
            }

            System.out.print("Issue: ");
            String issue = scanner.nextLine().trim();

            System.out.print("RepairStatus: ");
            String repairStatus = scanner.nextLine().trim();
            if (repairStatus.isEmpty())
                repairStatus = "In progress";

            session.beginTransaction();
            equipmentManagement.setAdmin(admin);

            // Update existing details instead of replacing
            EquipmentManagementDetails details = equipmentManagement.getDetails();
            if (details == null) {
                details = new EquipmentManagementDetails();
                details.setEquipment(equipmentManagement);
                equipmentManagement.setDetails(details);
            }

            details.setRoomNum(roomNum);
            details.setIssue(issue);
            details.setRepairStatus(repairStatus);

            session.merge(equipmentManagement);
            session.getTransaction().commit();

            System.out.println("Equipment details updated successfully!");

        } catch (Exception ex) {
            if (session.getTransaction().isActive())
                session.getTransaction().rollback();
            System.out.println("Failed to update details: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /***************************************************************
     * adminEquipmentMaintenanceAdd
     ***************************************************************/
    private static void adminEquipmentMaintenanceAdd(Session session, Admin admin) {
        Scanner scanner = HibernateUtil.getScanner();
        try {
            System.out.print("Room number: ");
            int room = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Issue: ");
            String issue = scanner.nextLine().trim();

            System.out.print("RepairStatus: ");
            String status = scanner.nextLine().trim();
            if (status.isEmpty())
                status = "In progress";

            session.beginTransaction();

            // parent EquipmentManagement row to satisfy FK
            EquipmentManagement equipmentManagement = new EquipmentManagement(admin);
            equipmentManagement.setDetails(room, issue, status);
            session.persist(equipmentManagement);

            session.getTransaction().commit();

            System.out.println("New issue recorded for equipmentId " + equipmentManagement.getEquipmentId() + ".");
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid room number. Cancelled.");
        } catch (Exception e) {
            if (session.getTransaction().isActive())
                session.getTransaction().rollback();
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
