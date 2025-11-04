package app;

import app.HibernateUtil;
import models.EquipmentManagement;
import models.GroupFitnessClass;

import java.util.Scanner;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class FunctionsExtra {
    /***************************************************************
     * retrieveGroupFitnessClass
     ***************************************************************/
    public static GroupFitnessClass retrieveGroupFitnessClass(Session session) {
        Scanner scanner = HibernateUtil.getScanner();

        try {
            System.out.println("\n=== Existing Group Fitness Classes ===");
            var groupFitnessClasses = session.createQuery("from GroupFitnessClass", GroupFitnessClass.class).list();

            if (groupFitnessClasses.isEmpty()) {
                System.out.println("No group fitness classes found in the system.");
                return null;
            }

            for (GroupFitnessClass g : groupFitnessClasses) {
                System.out.println(g.toString());
            }
            System.out.println("=========================");

            GroupFitnessClass groupFitnessClass = null;
            boolean found = false;

            while (!found) {
                System.out.print("\nEnter the GroupFitnessClass ID: ");
                Long classId = Long.parseLong(scanner.nextLine().trim());

                groupFitnessClass = session.get(GroupFitnessClass.class, classId);

                if (groupFitnessClass == null) {
                    System.out.println("\nNo Group Fitness Class found with ID: " + classId);
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

            return groupFitnessClass;

        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            return null;
        }
    }

    /***************************************************************
     * retrieveEquipmentManagement
     ***************************************************************/
    public static EquipmentManagement retrieveEquipmentManagement(Session session) {
        Scanner scanner = HibernateUtil.getScanner();

        try {
            System.out.println("\n=== Existing Equipment Management ===");
            var equipmentManagements = session.createQuery("from EquipmentManagement", EquipmentManagement.class)
                    .list();

            if (equipmentManagements.isEmpty()) {
                System.out.println("No equipment found in the system.");
                return null;
            }

            for (EquipmentManagement e : equipmentManagements) {
                System.out.println(e.toString());
            }
            System.out.println("=========================");

            EquipmentManagement equipmentManagement = null;
            boolean found = false;

            while (!found) {
                System.out.print("\nEnter the EquipmentManagement ID: ");
                Long equipmentId = Long.parseLong(scanner.nextLine().trim());

                equipmentManagement = session.get(EquipmentManagement.class, equipmentManagement);

                if (equipmentManagement == null) {
                    System.out.println("\nNo EquipmentManagement found with ID: " + equipmentId);
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

            return equipmentManagement;

        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            return null;
        }
    }
}
