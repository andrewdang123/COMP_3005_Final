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
                    System.out.println("Failed to register user (possibly non-unique or invalid data). Please try again.");
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
        System.out.println("Updated Profile!");
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
