package app;

import app.HibernateUtil;
import models.Admin;
import models.EquipmentManagement;
import models.GroupFitnessClass;
import models.Member;
import models.Trainer;

import java.util.Scanner;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class FunctionsRetrieve {

    /***************************************************************
     * retrieveMember
     ***************************************************************/
    public static Member retrieveMember(Session session) {
        Scanner scanner = HibernateUtil.getScanner();

        try {
            System.out.println("\n=== Existing Members ===");
            var members = session.createQuery("from Member", Member.class).list();

            if (members.isEmpty()) {
                System.out.println("No members found in the system");
                return null;
            }

            for (Member m : members) {
                System.out.println(m.toString());
            }
            System.out.println("=========================");

            Member member = null;
            boolean found = false;

            while (!found) {
                System.out.print("\nEnter the Member ID: ");
                Long memberId = Long.parseLong(scanner.nextLine().trim());

                member = session.get(Member.class, memberId);

                if (member == null) {
                    System.out.println("\nNo member found with ID: " + memberId);
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

            return member;

        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            return null;
        }
    }

    /***************************************************************
     * retrieveTrainer
     ***************************************************************/
    public static Trainer retrieveTrainer(Session session) {
        Scanner scanner = HibernateUtil.getScanner();

        try {
            System.out.println("\n================ Existing Trainers ================ ");
            var trainers = session.createQuery("from Trainer", Trainer.class).list();

            if (trainers.isEmpty()) {
                System.out.println("No trainers found in the system");
                return null;
            }

            for (Trainer t : trainers) {
                System.out.println(t.toString());
            }
            System.out.println("=================================================== ");

            Trainer trainer = null;
            boolean found = false;

            while (!found) {
                System.out.print("\nEnter the Trainer ID: ");
                Long trainerId = Long.parseLong(scanner.nextLine().trim());

                trainer = session.get(Trainer.class, trainerId);

                if (trainer == null) {
                    System.out.println("\nNo trainer found with ID: " + trainerId);
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

            return trainer;

        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            return null;
        }
    }

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
            System.out.println("\n========================= Existing Equipment Management =========================");
            var equipmentManagements = session.createQuery("from EquipmentManagement", EquipmentManagement.class)
                    .list();

            if (equipmentManagements.isEmpty()) {
                System.out.println("No equipment found in the system.");
                return null;
            }

            for (EquipmentManagement e : equipmentManagements) {
                System.out.println(e.toString());
            }
            System.out.println("=================================================================================");

            EquipmentManagement equipmentManagement = null;
            boolean found = false;

            while (!found) {
                System.out.print("\nEnter the EquipmentManagement ID: ");
                Long equipmentId = Long.parseLong(scanner.nextLine().trim());

                equipmentManagement = session.get(EquipmentManagement.class, equipmentId);

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
