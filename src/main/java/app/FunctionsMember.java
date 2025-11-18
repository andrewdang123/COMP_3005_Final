package app;

import models.GroupFitnessClass;
import models.GroupFitnessClassMembers;
import models.HealthMetric;
import models.Member;
import models.PersonalTrainingSession;
import models.PersonalTrainingSessionDetails;
import models.Trainer;
import models.TrainerAvailability;
import models.Schedule;

import java.util.*;
import java.time.LocalTime;

import org.hibernate.Session;

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
        Scanner scanner = HibernateUtil.getScanner();
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Member member = retrieveMember(session);
            if (member == null)
                return;

            Trainer trainer = FunctionsTrainer.retrieveTrainer(session);
            if (trainer == null)
                return;

            // Ask user if they want to reschedule first
            while (true) {
                System.out.println("\nDo you want to:");
                System.out.println("1. Book a new PT session");
                System.out.println("2. Reschedule an existing session");
                System.out.print("Enter choice (1 or 2): ");
                String choiceInput = scanner.nextLine().trim();

                if (choiceInput.equals("2")) {
                    memberPtSessionSchedulingReschedule(session, member, trainer);
                    break;
                } else if (choiceInput.equals("1")) {
                    memberPtSessionSchedulingBookPrompt(session, member, trainer);
                    break;
                } else {
                    System.out.println("Invalid choice.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error scheduling PT session: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    /***************************************************************
     * memberPtSessionSchedulingBook
     ***************************************************************/
    public static void memberPtSessionSchedulingBook(Session session, Member member, Trainer trainer,
            String dayInput, int startHour, int endHour, int roomNum) {
        try {
            session.beginTransaction();

            PersonalTrainingSession ptSession = new PersonalTrainingSession();
            ptSession.setMember(member);
            ptSession.setTrainer(trainer);

            PersonalTrainingSessionDetails details = new PersonalTrainingSessionDetails();
            details.setRoomNum(roomNum);
            details.setSessionTime(new Schedule(dayInput, startHour, endHour));

            // link both sides
            ptSession.setSessionDetails(details);

            session.persist(ptSession);

            // Update trainer availability (split or adjust slots)
            FunctionsTrainer.trainerAdjustAvailability(trainer, dayInput, startHour, endHour);

            session.merge(trainer);
            session.getTransaction().commit();

            ptSession.print();

        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            if (session.getTransaction().isActive())
                session.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    private static void memberPtSessionSchedulingBookPrompt(Session session, Member member, Trainer trainer) {
        Scanner scanner = HibernateUtil.getScanner();
        try {
            trainer.printAvailabilities();

            System.out.print("Enter training day (e.g., MONDAY): ");
            String dayInput = scanner.nextLine().trim().toUpperCase();
            try {
                java.time.DayOfWeek.valueOf(dayInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid day entered. Cancelling booking.");
                return;
            }

            int startHour, endHour, roomNum;
            try {
                System.out.print("Enter start time (hour 0-23): ");
                startHour = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("Enter end time (hour 0-23): ");
                endHour = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("Enter room number: ");
                roomNum = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Cancelling booking.");
                return;
            }

            // --- Check trainer availability ---
            boolean available = FunctionsTrainer.trainerCheckAvailability(trainer, dayInput, startHour, endHour);

            if (!available) {
                System.out.println("Trainer is not available on that day/time. Cancelling booking.");
                return;
            }

            // --- Check room conflicts ---
            boolean roomConflict = session.createQuery(
                    "SELECT COUNT(p) FROM PersonalTrainingSessionDetails p " +
                            "WHERE p.roomNum = :roomNum " +
                            "AND p.sessionTime.dayOfWeek = :day " +
                            "AND ((:startHour BETWEEN p.sessionTime.startTime AND p.sessionTime.endTime) " +
                            "OR (:endHour BETWEEN p.sessionTime.startTime AND p.sessionTime.endTime))",
                    Long.class)
                    .setParameter("roomNum", roomNum)
                    .setParameter("day", java.time.DayOfWeek.valueOf(dayInput))
                    .setParameter("startHour", LocalTime.of(startHour, 0))
                    .setParameter("endHour", LocalTime.of(endHour, 0))
                    .uniqueResult() > 0;

            if (roomConflict) {
                System.out.println("Room conflict detected. Cancelling booking.");
                return;
            }

            // --- Book the session ---
            memberPtSessionSchedulingBook(session, member, trainer, dayInput, startHour, endHour, roomNum);

        } catch (Exception e) {
            System.out.println("Error during booking: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /***************************************************************
     * memberPtSessionSchedulingReschedule
     ***************************************************************/
    public static void memberPtSessionSchedulingReschedule(Session session, Member member, Trainer trainer) {
        Scanner scanner = HibernateUtil.getScanner();

        try {
            // --- Retrieve existing sessions for this member with this trainer ---
            List<PersonalTrainingSession> sessions = session.createQuery(
                    "FROM PersonalTrainingSession s WHERE s.member = :member AND s.trainer = :trainer",
                    PersonalTrainingSession.class)
                    .setParameter("member", member)
                    .setParameter("trainer", trainer)
                    .list();

            if (sessions.isEmpty()) {
                System.out.println("No existing sessions found with this trainer. Booking a new session instead.");
                memberPtSessionSchedulingBookPrompt(session, member, trainer);
                return;
            }

            // --- Display sessions ---
            System.out.println("\nExisting sessions with " + trainer.getName() + ":");
            for (int i = 0; i < sessions.size(); i++) {
                PersonalTrainingSession s = sessions.get(i);
                Schedule time = s.getSessionDetails().getSessionTime();
                System.out.println((i + 1) + ". " + time.getDayOfWeek() + " " +
                        time.getStartTime().getHour() + "-" + time.getEndTime().getHour() +
                        " in Room " + s.getSessionDetails().getRoomNum());
            }

            // --- Choose session to cancel ---
            System.out.print("Enter the number of the session to reschedule (or 0 to cancel): ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Cancelling operation.");
                return;
            }

            if (choice == 0) {
                System.out.println("Rescheduling cancelled.");
                return;
            }

            if (choice < 1 || choice > sessions.size()) {
                System.out.println("Invalid selection. Cancelling operation.");
                return;
            }

            PersonalTrainingSession toCancel = sessions.get(choice - 1);
            Schedule canceledTime = toCancel.getSessionDetails().getSessionTime();

            session.beginTransaction();

            session.remove(toCancel);
            FunctionsTrainer.trainerRestoreAvailability(session, trainer, canceledTime);

            session.merge(trainer);
            session.getTransaction().commit();

            System.out.println("Session cancelled and trainer availability restored.");
            System.out.println("Please enter new session details to reschedule.");

            // --- Prompt for new session booking ---
            memberPtSessionSchedulingBookPrompt(session, member, trainer);

        } catch (Exception e) {
            if (session.getTransaction().isActive())
                session.getTransaction().rollback();
            System.out.println("Error rescheduling session: " + e.getMessage());
            e.printStackTrace();
        }
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
