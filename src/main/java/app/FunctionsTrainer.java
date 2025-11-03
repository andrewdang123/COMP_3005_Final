package app;

import java.util.*;

import org.hibernate.Session;

import models.LatestHealthMetricDTO;
import models.Trainer;
import services.MemberService;

public class FunctionsTrainer {

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
     * trainerSetAvailability
     ***************************************************************/
    public static void trainerSetAvailability() {
        System.out.println("Availability Set!");
    }

    /***************************************************************
     * trainerScheduleView
     ***************************************************************/
    public static void trainerScheduleView() {
        System.out.println("Viewed Schedule!");
    }

    /***************************************************************
     * trainerMemberLookup
     ***************************************************************/
    public static void trainerMemberLookup() {
        System.out.println("Member Viewed!");
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            MemberService memberService = new MemberService(session);

            // Begin transaction
            session.beginTransaction();

            // Fetch latest health metrics
            List<LatestHealthMetricDTO> latestMetrics = memberService.getLatestHealthMetrics();

            // Display results
            for (LatestHealthMetricDTO metric : latestMetrics) {
                System.out.println(metric.getMemberName() +
                        " | Weight: " + metric.getCurrentWeight() +
                        " | BMI: " + metric.getCurrentBmi() +
                        " | Timestamp: " + metric.getTimestamp());
            }

            // Commit transaction
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

}
