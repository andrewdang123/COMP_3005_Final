package app;

import java.util.Scanner;

import models.HealthMetric;
import models.Member;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class FunctionsMember {
    /***************************************************************
     * memberUserRegistration
     ***************************************************************/
    public static void memberUserRegistration() {
        Scanner scanner = new Scanner(System.in);
        boolean success = false;

        while (!success) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                session.beginTransaction();

                System.out.print("Enter name: ");
                String name = scanner.nextLine().trim();

                System.out.print("Enter email: ");
                String email = scanner.nextLine().trim();

                System.out.print("Enter gender: ");
                String gender = scanner.nextLine().trim();

                System.out.print("Enter birth day (1-31): ");
                int day = scanner.nextInt();
                System.out.print("Enter birth month (1-12): ");
                int month = scanner.nextInt();
                System.out.print("Enter birth year (e.g., 2005): ");
                int year = scanner.nextInt();
                scanner.nextLine();

                Member member = new Member(name, email, gender, day, month, year);

                try {
                    session.persist(member);
                    session.getTransaction().commit();
                    success = true;
                    session.close();
                    System.out.println("User Registered!");

                    // Display current details
                    member.memberPrint();
                } catch (Exception e) {
                    session.getTransaction().rollback();
                    System.out.println(
                            "Failed to register user (possibly non-unique or invalid data). Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    /***************************************************************
     * memberProfileManagement
     ***************************************************************/
    public static void memberProfileManagement() {
        Scanner scanner = new Scanner(System.in);
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            boolean found = false;
            Member member = null;

            // Ask for ID until found or user quits
            while (!found) {
                System.out.print("\nEnter your Member ID to access your profile: ");
                Long memberId = scanner.nextLong();
                scanner.nextLine(); // consume newline

                member = session.get(Member.class, memberId);

                if (member == null) {
                    System.out.println("\nNo member found with ID: " + memberId);
                    System.out.println("1. Retry");
                    System.out.println("2. Quit");
                    System.out.print("Enter your choice: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline

                    if (choice == 2) {
                        System.out.println("Returning to main menu...");
                        session.close();
                        return;
                    }
                } else {
                    found = true;
                }
            }

            // Display current details
            member.memberPrint();

            // Update personal details
            System.out.print("Enter new name (or press Enter to keep current): ");
            String newName = scanner.nextLine().trim();
            if (!newName.isEmpty()) {
                member.setName(newName);
            }

            System.out.print("Enter new email (or press Enter to keep current): ");
            String newEmail = scanner.nextLine().trim();
            if (!newEmail.isEmpty()) {
                member.setEmail(newEmail);
            }

            System.out.print("Enter new gender (or press Enter to keep current): ");
            String newGender = scanner.nextLine().trim();
            if (!newGender.isEmpty()) {
                member.setGender(newGender);
            }

            // Update fitness goals
            System.out.print("Enter your target weight (kg): ");
            int targetWeight = scanner.nextInt();

            System.out.print("Enter your target BMI: ");
            int targetBMI = scanner.nextInt();

            // Save updates
            session.beginTransaction();
            member.setTargetWeight(targetWeight);
            member.setTargetBmi(targetBMI);
            session.merge(member);
            session.getTransaction().commit();

            System.out.println("Profile updated successfully!");
            System.out.println("Member ID: " + member.getMemberId());
            System.out.println("Target Weight: " + targetWeight);
            System.out.println("Target BMI: " + targetBMI);

            System.out.println("\nRedirecting to Health Metrics");
            memberHealthHistory(member);

        } catch (Exception e) {
            System.out.println("Error updating profile: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    /***************************************************************
     * memberHealthHistory
     ***************************************************************/
    public static void memberHealthHistory() {
        Scanner scanner = new Scanner(System.in);
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            boolean found = false;
            Member member = null;

            while (!found) {
                System.out.print("\nEnter your Member ID to log health metrics: ");
                Long memberId = scanner.nextLong();
                scanner.nextLine(); // consume newline

                member = session.get(Member.class, memberId);

                if (member == null) {
                    System.out.println("\nNo member found with ID: " + memberId);
                    System.out.println("1. Retry");
                    System.out.println("2. Quit");
                    System.out.print("Enter your choice: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    if (choice == 2) {
                        System.out.println("Returning to main menu...");
                        session.close();
                        return;
                    }
                } else {
                    found = true;
                }
            }
            memberHealthHistory(member);
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    /***************************************************************
     * memberHealthHistory
     ***************************************************************/
    public static void memberHealthHistory(Member member) {
        Scanner scanner = new Scanner(System.in);
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            // Prompt for metrics only, no ID
            System.out.print("\nEnter current weight (kg, integer): ");
            int currentWeight = scanner.nextInt();

            System.out.print("Enter current BMI (integer): ");
            int currentBMI = scanner.nextInt();
            scanner.nextLine(); // consume newline

            try {
                session.beginTransaction();
                HealthMetric metric = new HealthMetric(
                        member,
                        currentWeight,
                        currentBMI);
                session.persist(metric);
                session.getTransaction().commit();

                System.out.println("Health metric logged successfully!");
                System.out.println("Weight: " + currentWeight + ", BMI: " + currentBMI);

            } catch (Exception e) {
                session.getTransaction().rollback();
                System.out.println(
                        "Failed to log health metric (invalid data?). Please try again.");
            }

        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    /***************************************************************
     * memberPtSessionScheduling
     ***************************************************************/
    public static void memberPtSessionScheduling() {
        System.out.println("PT Session Scheduled!");
    }

    /***************************************************************
     * memberGroupClassRegistration
     ***************************************************************/
    public static void memberGroupClassRegistration() {
        System.out.println("Group Class Registered!");
    }

}
