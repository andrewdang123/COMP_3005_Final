package app;

import java.util.Scanner;

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

                Member newMember = new Member(name, email, gender, day, month, year);

                try {
                    session.persist(newMember);
                    session.getTransaction().commit();
                    success = true;
                    session.close();
                    System.out.println("User Registered!");
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
            System.out.println("\n--- Current Profile ---");
            System.out.println("Member ID: " + member.getMemberId());
            System.out.println("Name: " + member.getName());
            System.out.println("Email: " + member.getEmail());
            System.out.println("Gender: " + member.getGender());
            System.out.println("Date of Birth: " + member.getDateOfBirth().getDay() + "/"
                    + member.getDateOfBirth().getMonth() + "/"
                    + member.getDateOfBirth().getYear());
            System.out.println("-----------------------");

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

            // Update fitness goals
            System.out.print("Enter your target weight (kg): ");
            int targetWeight = scanner.nextInt();

            System.out.print("Enter your target BMI: ");
            int targetBMI = scanner.nextInt();

            // Save updates
            session.beginTransaction();
            // assuming targetWeight and targetBMI are added as fields in Member class
            member.setTargetWeight(targetWeight);
            member.setTargetBmi(targetBMI);
            session.merge(member);
            session.getTransaction().commit();

            System.out.println("\n✅ Profile updated successfully!");
            System.out.println("Member ID: " + member.getMemberId());
            System.out.println("Target Weight: " + targetWeight);
            System.out.println("Target BMI: " + targetBMI);

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
        System.out.println("Health Metric Logged!");
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
