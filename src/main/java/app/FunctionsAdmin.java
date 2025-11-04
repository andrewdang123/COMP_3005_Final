package app;

import app.HibernateUtil;
import app.FunctionsExtra;
import models.Admin;
import models.EquipmentManagement;
import models.LatestHealthMetricDTO;
import models.Member;
import models.Trainer;
import services.MemberService;

import java.util.Scanner;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

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
        try {
            Admin admin = retrieveAdmin(session);
            if (admin == null) {
                return;
            }
            EquipmentManagement equipmentManagement = FunctionsExtra.retrieveEquipmentManagement(session);
            if (equipmentManagement == null) {
                return;
            }

        } catch (Exception e) {
            System.out.println("Error updating profile: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    /***************************************************************
     * adminClassManagement
     ***************************************************************/
    public static void adminClassManagement() {
        System.out.println("Classes Managed!");
    }

}
