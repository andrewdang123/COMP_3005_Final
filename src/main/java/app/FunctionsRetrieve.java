package app;

import java.util.Scanner;

import org.hibernate.Session;

import models.Admin;
import models.EquipmentManagement;
import models.GroupFitnessClass;
import models.Member;
import models.Trainer;

public class FunctionsRetrieve {

    /***************************************************************
     * retrieveMember
     ***************************************************************/
    /**
     * Retrieves a Member from the database:
     * - Runs an HQL query "from Member" to list all members.
     * - Prints each member so the user can see available IDs.
     * - Prompts for a Member ID and uses session.get(Member, id)
     *   to load the selected member.
     * - Allows retry or quit if the ID is not found.
     * - Returns the chosen Member or null if the user quits or an error occurs.
     * - session.get(Member, id) uses the Member table's primary key index for fast lookup.
     */

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
    /**
     * Retrieves a Trainer from the database:
     * - Executes "from Trainer" to load and display all trainers.
     * - Prints each trainer so the user can see valid trainer IDs.
     * - Prompts for a Trainer ID and uses session.get(Trainer, id)
     *   to fetch the selected trainer.
     * - Lets the user retry or quit when an invalid ID is entered.
     * - Returns the selected Trainer or null if the user quits or an error occurs.
     * - session.get(Trainer, id) uses the Trainer table's primary key index for fast lookup.
     */


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
    /**
     * Retrieves an Admin from the database:
     * - Runs "from Admin" to fetch and list all admins.
     * - Displays each admin so the user can see valid IDs.
     * - Prompts for an Admin ID and uses session.get(Admin, id)
     *   to load the chosen admin.
     * - Offers retry or quit when the ID is invalid.
     * - Returns the selected Admin or null if the user quits or an error occurs.
     * - session.get(Admin, id) uses the Admin table's primary key index for fast lookup.
     */

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
    /**
     * Retrieves a GroupFitnessClass from the database:
     * - Executes "from GroupFitnessClass" to list all classes.
     * - Prints each class so the user can see available class IDs.
     * - Prompts for a class ID and uses session.get(GroupFitnessClass, id)
     *   to retrieve that class.
     * - Allows retrying or quitting if the ID is not found.
     * - Returns the chosen GroupFitnessClass or null on quit or error.
     * - session.get(GroupFitnessClass, id) uses the class table's primary key index for fast lookup.
     */

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
    /**
     * Retrieves an EquipmentManagement record from the database:
     * - Runs "from EquipmentManagement" to load and display all equipment entries.
     * - Shows each entry so the user can see valid equipment IDs.
     * - Prompts for an EquipmentManagement ID and uses session.get(EquipmentManagement, id)
     *   to load the selected record.
     * - Lets the user retry or quit if the ID is invalid.
     * - Returns the chosen EquipmentManagement or null if the user quits or an error occurs.
     * - session.get(EquipmentManagement, id) uses the EquipmentManagement table's primary key index.
     */

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
