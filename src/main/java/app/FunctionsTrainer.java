package app;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;

import models.ClassSchedule;
import models.ClassScheduleDetails;
import models.GroupFitnessClass;
import models.LatestHealthMetricDTO;
import models.Member;
import models.PersonalTrainingSession;
import models.Schedule;
import models.Trainer;
import models.TrainerAvailability;
import services.MemberService;

public class FunctionsTrainer {

    /***************************************************************
     * trainerCheckAvailability
     ***************************************************************/
    /**
     * Checks if a trainer is available in a given time window:
     * - Scans the trainer's in-memory availability list (no DB call).
     * - Matches on dayOfWeek and ensures the requested start/end hours
     * are fully inside an existing availability slot.
     * - Returns true if at least one slot can cover that requested time.
     */

    public static boolean trainerCheckAvailability(Trainer trainer, String dayInput, int startHour,
            int endHour) {
        return trainer.getAvailabilities().stream()
                .anyMatch(a -> a.getDayOfWeek().toString().equalsIgnoreCase(dayInput)
                        && startHour >= a.getStartTime().getHour()
                        && endHour <= a.getEndTime().getHour());
    }

    /***************************************************************
     * trainerAdjustAvailability
     ***************************************************************/
    /**
     * Adjusts a trainer's availability in memory after booking a time:
     * - Iterates through the trainer's existing availability slots.
     * - For the matching day, splits or trims the slot around the
     * booked [startHour, endHour] window, so the booked time is removed.
     * - Rebuilds the trainer.getAvailabilities() list with updated slots.
     * - This method only changes the object graph; it does not touch
     * the database or use any indexes/triggers directly.
     * - The caller is responsible for persisting the updated availability.
     */

    public static void trainerAdjustAvailability(Trainer trainer, String dayInput, int startHour,
            int endHour) {
        List<TrainerAvailability> updatedAvailabilities = new ArrayList<>();
        for (TrainerAvailability a : trainer.getAvailabilities()) {
            if (a.getDayOfWeek().equals(java.time.DayOfWeek.valueOf(dayInput))) {
                LocalTime availStart = a.getStartTime();
                LocalTime availEnd = a.getEndTime();
                if (availStart.getHour() <= startHour && availEnd.getHour() >= endHour) {
                    if (availStart.getHour() < startHour) {
                        updatedAvailabilities.add(new TrainerAvailability(
                                trainer,
                                a.getDayOfWeek().toString(),
                                availStart.getHour(),
                                startHour));
                    }
                    if (availEnd.getHour() > endHour) {
                        updatedAvailabilities.add(new TrainerAvailability(
                                trainer,
                                a.getDayOfWeek().toString(),
                                endHour,
                                availEnd.getHour()));
                    }
                } else {
                    updatedAvailabilities.add(a);
                }
            } else {
                updatedAvailabilities.add(a);
            }
        }
        trainer.getAvailabilities().clear();
        trainer.getAvailabilities().addAll(updatedAvailabilities);
    }

    /***************************************************************
     * trainerRestoreAvailability
     ***************************************************************/
    /**
     * Restores a trainer's availability after a session is cancelled:
     * - Takes a Session (with an active transaction expected from caller),
     * the Trainer, and the cancelled Schedule.
     * - Looks for overlapping TrainerAvailability slots on the same day,
     * removes the old slot, and persists new split slots that include
     * the cancelled time back into the trainer's availability set.
     * - If no overlapping slot is found, inserts a new TrainerAvailability
     * that exactly matches the cancelled time window.
     * - Uses session.persist() and trainer.removeAvailability(), but does
     * not start or commit the transaction itself; the caller controls that.
     */

    public static void trainerRestoreAvailability(Session session, Trainer trainer, Schedule canceledTime) {
        boolean restored = false;
        for (TrainerAvailability avail : trainer.getAvailabilities()) {
            if (avail.getDayOfWeek().equals(canceledTime.getDayOfWeek())) {
                LocalTime availStart = avail.getStartTime();
                LocalTime availEnd = avail.getEndTime();

                if (availEnd.getHour() <= canceledTime.getStartTime().getHour() ||
                        availStart.getHour() >= canceledTime.getEndTime().getHour())
                    continue;

                List<TrainerAvailability> newAvailabilities = new ArrayList<>();
                if (availStart.getHour() < canceledTime.getStartTime().getHour()) {
                    newAvailabilities.add(new TrainerAvailability(trainer,
                            avail.getDayOfWeek().toString(),
                            availStart.getHour(),
                            canceledTime.getStartTime().getHour()));
                }
                if (availEnd.getHour() > canceledTime.getEndTime().getHour()) {
                    newAvailabilities.add(new TrainerAvailability(trainer,
                            avail.getDayOfWeek().toString(),
                            canceledTime.getEndTime().getHour(),
                            availEnd.getHour()));
                }

                trainer.removeAvailability(session, avail);
                for (TrainerAvailability t : newAvailabilities) {
                    session.persist(t);
                    trainer.getAvailabilities().add(t);
                }

                restored = true;
                break;
            }
        }

        if (!restored) {
            TrainerAvailability restoredSlot = new TrainerAvailability(trainer,
                    canceledTime.getDayOfWeek().toString(),
                    canceledTime.getStartTime().getHour(),
                    canceledTime.getEndTime().getHour());
            session.persist(restoredSlot);
            trainer.getAvailabilities().add(restoredSlot);
        }
    }

    /***************************************************************
     * trainerSetAvailability
     ***************************************************************/
    /**
     * Top-level menu for a trainer to manage availability:
     * - Opens a Hibernate session and retrieves a Trainer via FunctionsRetrieve.
     * - Prints current availability slots.
     * - Lets the user:
     * 1) Update an existing availability (trainerSetAvailabilityUpdate), or
     * 2) Add a new availability slot (trainerSetAvailabilityAdd).
     * - The child methods handle their own transactions for DB writes.
     */

    public static void trainerSetAvailability() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Scanner scanner = HibernateUtil.getScanner();
        try {
            Trainer trainer = FunctionsRetrieve.retrieveTrainer(session);
            if (trainer == null) {
                return;
            }
            trainer.printAvailabilities();
            while (true) {
                System.out.println("\nDo you want to:");
                System.out.println("0. Exit");
                System.out.println("1. Update a prexisting availability");
                System.out.println("2. Add time when available for sessions or classes");
                System.out.print("Enter choice (0 - 2): ");

                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1":
                        trainerSetAvailabilityUpdate(session, trainer);
                        break;
                    case "2":
                        trainerSetAvailabilityAdd(session, trainer);
                        break;
                    case "0":
                        System.out.println("Returning...");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    /***************************************************************
     * trainerSetAvailabilityUpdate
     ***************************************************************/
    /**
     * Updates an existing TrainerAvailability record:
     * - Shows all existing availability slots for the trainer.
     * - Prompts for an Availability ID and finds that object in the
     * trainer's in-memory list (no extra DB query for it).
     * - Allows updating:
     * - Day of week,
     * - Start time, and
     * - End time (keeping old values if input is blank or invalid).
     * - Starts a transaction, merges the updated TrainerAvailability,
     * and commits.
     */

    public static void trainerSetAvailabilityUpdate(Session session, Trainer trainer) {
        Scanner scanner = HibernateUtil.getScanner();

        if (trainer.getAvailabilities().isEmpty()) {
            System.out.println("\nNo existing availabilities to update.");
            return;
        }

        trainer.printAvailabilities();

        System.out.print("\nEnter the Availability ID you wish to update: ");
        Long availabilityId = null;
        try {
            availabilityId = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID entered.");
            return;
        }

        TrainerAvailability selectedAvailability = null;
        for (TrainerAvailability avail : trainer.getAvailabilities()) {
            if (avail.getTrainerAvailabilityId().equals(availabilityId)) {
                selectedAvailability = avail;
                break;
            }
        }

        if (selectedAvailability == null) {
            System.out.println("No availability found with ID: " + availabilityId);
            return;
        }

        System.out.println("\nSelected Availability: " + selectedAvailability.toString());

        try {
            System.out.print(
                    "Enter new training day (press Enter to keep " + selectedAvailability.getDayOfWeek() + "): ");
            String dayInput = scanner.nextLine().trim();
            if (!dayInput.isEmpty()) {
                dayInput = dayInput.toUpperCase();
                try {
                    java.time.DayOfWeek.valueOf(dayInput);
                    selectedAvailability.setDayOfWeek(dayInput);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid day entered. Keeping previous day.");
                }
            }

            System.out.print("Enter new start time (hour 0-23, press Enter to keep "
                    + selectedAvailability.getStartTime() + "): ");
            String startInput = scanner.nextLine().trim();
            if (!startInput.isEmpty()) {
                try {
                    int startHour = Integer.parseInt(startInput);
                    selectedAvailability.setStartTime(startHour);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid start time. Keeping previous value.");
                }
            }

            System.out.print(
                    "Enter new end time (hour 0-23, press Enter to keep " + selectedAvailability.getEndTime() + "): ");
            String endInput = scanner.nextLine().trim();
            if (!endInput.isEmpty()) {
                try {
                    int endHour = Integer.parseInt(endInput);
                    selectedAvailability.setEndTime(endHour);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid end time. Keeping previous value.");
                }
            }

            session.beginTransaction();
            session.merge(selectedAvailability);
            session.getTransaction().commit();

            System.out.println("Successfully updated availability: " + selectedAvailability.toString());

        } catch (Exception e) {
            e.printStackTrace();
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        }
    }

    /***************************************************************
     * trainerSetAvailabilityAdd
     ***************************************************************/
    /**
     * Adds a new availability slot for a trainer:
     * - Prompts for a valid day of week, start hour, and end hour.
     * - Starts a transaction and creates a new TrainerAvailability
     * linked to the trainer.
     * - Calls trainer.addAvailability(), which should persist the new
     * slot (often with session.persist() inside that method).
     * - Commits the transaction on success.
     */

    public static void trainerSetAvailabilityAdd(Session session, Trainer trainer) {
        Scanner scanner = HibernateUtil.getScanner();
        System.out.print("Enter training day (e.g., MONDAY): ");
        String dayInput = scanner.nextLine().trim().toUpperCase();
        try {
            java.time.DayOfWeek.valueOf(dayInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid day entered.");
            return;
        }

        int startHour, endHour;
        try {
            System.out.print("Enter start time (hour 0-23): ");
            startHour = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter end time (hour 0-23): ");
            endHour = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        try {
            session.beginTransaction();
            TrainerAvailability trainerAvailability = new TrainerAvailability(trainer, dayInput, startHour, endHour);
            trainer.addAvailability(trainerAvailability);
            session.getTransaction().commit();
            System.out.println("Successfully Added: " + trainerAvailability.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /***************************************************************
     * trainerScheduleView
     ***************************************************************/
    /**
     * Shows the full schedule (PT sessions + group classes) for a trainer:
     * - Retrieves a Trainer via FunctionsRetrieve.
     * - Queries PersonalTrainingSession where p.trainer = :trainer and prints them.
     * → These lookups benefit from an index on the trainer_id foreign key
     * in the PersonalTrainingSession table.
     * - Queries GroupFitnessClass where g.trainer = :trainer.
     * → Also benefits from an index on trainer_id in the GroupFitnessClass table.
     * - For each GroupFitnessClass, queries ClassSchedule where
     * cs.groupFitnessClass = :gfc.
     * → This uses an index on the group_fitness_class_id foreign key in
     * ClassSchedule
     * (that’s the “THIS USES THAT INDEX” comment).
     * - Prints schedule details (room, day, start, end) via ClassScheduleDetails
     * and its embedded Schedule.
     */

    public static void trainerScheduleView() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Trainer trainer = FunctionsRetrieve.retrieveTrainer(session);
            if (trainer == null) {
                return;
            }

            List<PersonalTrainingSession> sessions = session.createQuery(
                    "FROM PersonalTrainingSession p WHERE p.trainer = :trainer",
                    PersonalTrainingSession.class)
                    .setParameter("trainer", trainer)
                    .getResultList();

            if (sessions.isEmpty()) {
                System.out.println("\nNo scheduled sessions found for trainer: " + trainer.getName());
            }
            System.out.println("\n===================== Personal Trainer Schedule =====================");
            for (PersonalTrainingSession s : sessions) {
                System.out.println(s.toString());
            }
            System.out.println("=====================================================================");

            List<GroupFitnessClass> classes = session.createQuery(
                    "FROM GroupFitnessClass g WHERE g.trainer = :trainer",
                    GroupFitnessClass.class)
                    .setParameter("trainer", trainer)
                    .getResultList();

            if (classes.isEmpty()) {
                System.out.println("No classes found for trainer: " + trainer.getName());
                return;
            }

            System.out.println("\n======================== Group Trainer Schedule ========================");
            for (GroupFitnessClass gfc : classes) {
                List<ClassSchedule> schedules = session.createQuery(
                        "FROM ClassSchedule cs WHERE cs.groupFitnessClass = :gfc",
                        ClassSchedule.class)
                        .setParameter("gfc", gfc)
                        .getResultList();

                if (schedules.isEmpty()) {
                    System.out.println("No schedule found for class: " + gfc.getClassName());
                    continue;
                }

                for (ClassSchedule cs : schedules) {
                    ClassScheduleDetails details = cs.getDetails();
                    if (details != null) {
                        System.out.println("Class: " + gfc.getClassName() +
                                ", ScheduleId: " + cs.getScheduleId() +
                                ", Room: " + details.getRoomNum() +
                                ", Day: " + details.getScheduleTime().getDayOfWeek() +
                                ", Start: " + details.getScheduleTime().getStartTime() +
                                ", End: " + details.getScheduleTime().getEndTime());
                    } else {
                        System.out.println("Class: " + gfc.getClassName() + " has no schedule details.");
                    }
                }
            }

            System.out.println("========================================================================");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    /***************************************************************
     * trainerMemberLookup
     ***************************************************************/
    /**
     * Lets a trainer see their PT members and view each member's latest health
     * metric:
     * - Opens a session and starts a transaction (read-focused).
     * - Retrieves a Trainer via FunctionsRetrieve.
     * - Runs a DISTINCT query on PersonalTrainingSession to get all Members
     * who have at least one session with this trainer:
     * SELECT DISTINCT p.member FROM PersonalTrainingSession p WHERE p.trainer =
     * :trainer
     * → This query benefits from an index on trainer_id in PersonalTrainingSession,
     * and on member_id (FK) when joining to Member.
     * - Displays these members and prompts the trainer to select a Member ID.
     * - Uses MemberService.getLatestHealthMetrics() to fetch
     * LatestHealthMetricDTOs:
     * this View represents a pre-aggregated "latest metric per member" view of the
     * HealthMetric table (implemented as a query or a logical view in the service).
     * - Searches the View list for the selected member's latest metric and prints
     * weight, BMI, and timestamp.
     * - Commits the transaction after the lookup; rolls back on error.
     */

    public static void trainerMemberLookup() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Scanner scanner = HibernateUtil.getScanner();
        try {
            Trainer trainer = FunctionsRetrieve.retrieveTrainer(session);
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
