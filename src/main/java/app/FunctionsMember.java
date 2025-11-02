package app;

import java.util.Scanner;

import models.GroupFitnessClass;
import models.GroupFitnessClassMembers;
import models.HealthMetric;
import models.Member;

import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class FunctionsMember {

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
     * memberUserRegistration
     ***************************************************************/
    public static void memberUserRegistration() {
        Scanner scanner = HibernateUtil.getScanner();
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
                int day = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("Enter birth month (1-12): ");
                int month = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("Enter birth year (e.g., 2005): ");
                int year = Integer.parseInt(scanner.nextLine().trim());

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
        Scanner scanner = HibernateUtil.getScanner();
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Member member = retrieveMember(session);
            if (member == null) {
                return;
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
            int targetWeight = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter your target BMI: ");
            int targetBMI = Integer.parseInt(scanner.nextLine().trim());

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
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Member member = retrieveMember(session);
            if (member == null) {
                return;
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
        Scanner scanner = HibernateUtil.getScanner();
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            // Prompt for metrics only, no ID
            System.out.print("\nEnter current weight (kg, integer): ");
            int currentWeight = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter current BMI (integer): ");
            int currentBMI = Integer.parseInt(scanner.nextLine().trim());

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
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Member member = retrieveMember(session);
            if (member == null) {
                return;
            }
            GroupFitnessClass groupFitnessClass = FunctionsExtra.retrieveGroupFitnessClass(session);
            if (groupFitnessClass == null) {
                return;
            }
            System.out.println(groupFitnessClass.toString());
            try {
                /*
                 * Mention that this uses a trigger in it which automatically increments the
                 * current member count
                 */
                session.beginTransaction();
                session.persist(new GroupFitnessClassMembers(groupFitnessClass, member));
                session.getTransaction().commit();
                System.out.println("Member successfully added to Group!");
            } catch (RuntimeException e) {
                session.getTransaction().rollback();
                System.out.println("Error: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        } finally {
            session.close();
        }
    }

}
