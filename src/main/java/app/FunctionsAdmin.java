package app;

import java.util.Scanner;

import org.hibernate.Session;

import models.Admin;

public class FunctionsAdmin {

    /***************************************************************
     * retrieveAdmin
     ***************************************************************/
    public static Admin retrieveAdmin(Session session) {
        Scanner scanner = HibernateUtil.getScanner();

        try {
            System.out.println("\n=== Existing Admins ===");
            var admins = session.createQuery("from Admin", Admin.class).list();

            if (admins.isEmpty()) {
                System.out.println("No admins found in the system");
                return null;
            }

            for (Admin a : admins) {
                System.out.println(a.toString());
            }
            System.out.println("=========================");

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
        System.out.println("Reported on Equipment!");
        Scanner scanner = HibernateUtil.getScanner();

    }

    /***************************************************************
     * adminClassManagement
     ***************************************************************/
    public static void adminClassManagement() {
        System.out.println("Classes Managed!");
    }

}
