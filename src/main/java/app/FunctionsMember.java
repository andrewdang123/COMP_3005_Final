package app;

import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;

import models.GroupFitnessClass;
import models.GroupFitnessClassMembers;
import models.HealthMetric;
import models.Member;
import models.PersonalTrainingSession;
import models.PersonalTrainingSessionDetails;
import models.Schedule;
import models.Trainer;

public class FunctionsMember {

    /***************************************************************
     * memberUserRegistration
     ***************************************************************/
    /**
     * Handles new member registration:
     * - Opens a Hibernate session and starts a transaction.
     * - Collects basic info (name, email, gender, date of birth) from input.
     * - Creates a Member entity and persists it to the database.
     * - Commits the transaction on success, rolls back on error.
     * - Loops until registration succeeds and prints the member details.
     */
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
    /**
     * Manages a member's profile:
     * - Retrieves the logged-in member from the database.
     * - Displays current details and allows updating name, email, and gender.
     * - Prompts for target weight and target BMI.
     * - Starts a transaction, updates the member entity, merges it, and commits.
     * - Prints updated targets and then redirects to health metric logging.
     */
    public static void memberProfileManagement() {
        Scanner scanner = HibernateUtil.getScanner();
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Member member = FunctionsRetrieve.retrieveMember(session);
            if (member == null) {
                return;
            }

            member.memberPrint();

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

            System.out.print("Enter your target weight (kg): ");
            int targetWeight = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter your target BMI: ");
            int targetBMI = Integer.parseInt(scanner.nextLine().trim());

            session.beginTransaction();
            try {
                member.setTargetWeight(targetWeight);
                member.setTargetBmi(targetBMI);
                session.merge(member);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                System.out.println(
                        "Failed to register user (possibly non-unique or invalid data). Please try again.");
                return;
            }

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
    /**
     * Entry point for logging health metrics:
     * - Opens a session and retrieves the logged-in member.
     * - Delegates to the overloaded memberHealthHistory(Member) method
     * to collect and persist the health metrics for that member.
     */
    public static void memberHealthHistory() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Member member = FunctionsRetrieve.retrieveMember(session);
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
    /**
     * Logs a single health metric for the given member:
     * - Opens a session and prompts for current weight and BMI.
     * - Starts a transaction, creates a HealthMetric entity linked
     * to the member, persists it, and commits the transaction.
     * - Prints the recorded values and rolls back if logging fails.
     */

    public static void memberHealthHistory(Member member) {
        Scanner scanner = HibernateUtil.getScanner();
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
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
    /**
     * Manages personal training session scheduling for a member:
     * - Opens a session and retrieves the logged-in member and a trainer.
     * - Prompts the user to choose between booking a new session
     * or rescheduling an existing one.
     * - Delegates to memberPtSessionSchedulingBookPrompt or
     * memberPtSessionSchedulingReschedule based on the choice.
     */

    public static void memberPtSessionScheduling() {
        Scanner scanner = HibernateUtil.getScanner();
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Member member = FunctionsRetrieve.retrieveMember(session);
            if (member == null)
                return;

            Trainer trainer = FunctionsRetrieve.retrieveTrainer(session);
            if (trainer == null)
                return;

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
    /**
     * Books a personal training session:
     * - Starts a transaction on the provided session.
     * - Creates a PersonalTrainingSession linked to the member and trainer.
     * - Creates PersonalTrainingSessionDetails with room and time (Schedule).
     * - Links details to the session, persists the session, merges trainer,
     * and commits the transaction.
     * - Prints the session details after successful booking.
     */

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

            ptSession.setSessionDetails(details);

            session.persist(ptSession);

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

    /***************************************************************
     * memberPtSessionSchedulingBookPrompt
     ***************************************************************/
    /**
     * Prompts a member for PT booking details and validates them:
     * - Prints trainer availability.
     * - Collects day, start time, end time, and room number from input.
     * - Uses FunctionsTrainer.trainerCheckAvailability to ensure the trainer
     * is free in the requested slot.
     * - Runs an HQL query to count overlapping PersonalTrainingSessionDetails
     * in the same room and time window to detect room conflicts.
     * - Calls memberPtSessionSchedulingBook to perform the actual booking
     * if both trainer and room are free.
     */

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

            boolean available = FunctionsTrainer.trainerCheckAvailability(trainer, dayInput, startHour, endHour);

            if (!available) {
                System.out.println("Trainer is not available on that day/time. Cancelling booking.");
                return;
            }

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

            memberPtSessionSchedulingBook(session, member, trainer, dayInput, startHour, endHour, roomNum);

        } catch (Exception e) {
            System.out.println("Error during booking: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /***************************************************************
     * memberPtSessionSchedulingReschedule
     ***************************************************************/
    /**
     * Reschedules a personal training session:
     * - Queries all existing PersonalTrainingSession rows for the member
     * with the selected trainer and displays them.
     * - Lets the user pick a session to cancel.
     * - Starts a transaction, removes the selected session from the database,
     * and restores the trainer's availability by updating TrainerAvailability
     * slots based on the cancelled time.
     * - Commits the transaction after availability is restored.
     * - Prompts the member to book a new session using the booking prompt.
     */

    public static void memberPtSessionSchedulingReschedule(Session session, Member member, Trainer trainer) {
        Scanner scanner = HibernateUtil.getScanner();

        try {
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

            System.out.println("\nExisting sessions with " + trainer.getName() + ":");
            for (int i = 0; i < sessions.size(); i++) {
                PersonalTrainingSession s = sessions.get(i);
                Schedule time = s.getSessionDetails().getSessionTime();
                System.out.println((i + 1) + ". " + time.getDayOfWeek() + " " +
                        time.getStartTime().getHour() + "-" + time.getEndTime().getHour() +
                        " in Room " + s.getSessionDetails().getRoomNum());
            }

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

            session.getTransaction().commit();

            System.out.println("Session cancelled and trainer availability restored.");
            System.out.println("Please enter new session details to reschedule.");

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
    /**
     * Registers a member for a group fitness class:
     * - Opens a session and retrieves the logged-in member and a GroupFitnessClass.
     * - Starts a transaction, creates a GroupFitnessClassMembers link entity
     * between the class and the member, and persists it.
     * - Relies on a database trigger on the GroupFitnessClassMembers insert
     * to automatically update the current member count for the class.
     * - Commits the transaction on success and rolls back on error.
     */
    public static void memberGroupClassRegistration() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Member member = FunctionsRetrieve.retrieveMember(session);
            if (member == null) {
                return;
            }
            GroupFitnessClass groupFitnessClass = FunctionsRetrieve.retrieveGroupFitnessClass(session);
            if (groupFitnessClass == null) {
                return;
            }
            System.out.println(groupFitnessClass.toString());
            try {
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
