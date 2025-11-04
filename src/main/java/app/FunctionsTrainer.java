package app;

import java.util.*;

import org.hibernate.Session;

import models.LatestHealthMetricDTO;
import models.Member;
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
        Session session = HibernateUtil.getSessionFactory().openSession();
        Scanner scanner = HibernateUtil.getScanner();
        try {
            Trainer trainer = retrieveTrainer(session);
            if (trainer == null) {
                return;
            }

            session.beginTransaction();

            List<Member> members = session.createQuery(
                    "SELECT DISTINCT p.member FROM PersonalTrainingSession p WHERE p.trainer = :trainer",
                    Member.class)
                    .setParameter("trainer", trainer)
                    .getResultList();

            if (members.isEmpty()) {
                System.out.println("\nNo members have a personal training session with trainer: " + trainer.getName());
                session.getTransaction().commit();
                return;
            }

            System.out.println("\nMembers who have a personal training session with trainer: " + trainer.getName());
            System.out.println("=============================================================");
            for (Member m : members) {
                System.out.println("ID: " + m.getMemberId() + " | Name: " + m.getName());
            }
            System.out.println("=============================================================");

            Member selectedMember = null;
            boolean found = false;

            while (!found) {
                try {
                    System.out.print("\nEnter the Member ID to view their latest health metric: ");
                    Long memberId = Long.parseLong(scanner.nextLine().trim());
                    for (Member m : members) {
                        if (m.getMemberId().equals(memberId)) {
                            selectedMember = m;
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        System.out.println("No member found with ID: " + memberId);
                        System.out.println("1. Retry");
                        System.out.println("2. Quit");
                        System.out.print("Enter your choice: ");
                        int choice = Integer.parseInt(scanner.nextLine().trim());
                        if (choice == 2) {
                            System.out.println("Returning to main menu...");
                            session.getTransaction().commit();
                            return;
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }

            // Retrieve latest health metric for the selected member
            MemberService memberService = new MemberService(session);
            List<LatestHealthMetricDTO> latestMetrics = memberService.getLatestHealthMetrics();

            boolean metricFound = false;
            for (LatestHealthMetricDTO metric : latestMetrics) {
                if (metric.getMemberName().equalsIgnoreCase(selectedMember.getName())) {
                    System.out.println("\nLatest Health Metric for " + selectedMember.getName() + ":");
                    System.out.println("-------------------------------------------------------------");
                    System.out.println("Weight: " + metric.getCurrentWeight());
                    System.out.println("BMI: " + metric.getCurrentBmi());
                    System.out.println("Timestamp: " + metric.getTimestamp());
                    System.out.println("-------------------------------------------------------------");
                    metricFound = true;
                    break;
                }
            }

            if (!metricFound) {
                System.out.println("\nNo health metrics found for " + selectedMember.getName());
            }

            session.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error during member lookup: " + e.getMessage());
            e.printStackTrace();
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        } finally {
            session.close();
        }
    }

}
