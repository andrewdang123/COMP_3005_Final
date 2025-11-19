package app;

import java.time.LocalTime;
import java.util.*;

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
     * trainerCheckAvailability
     ***************************************************************/
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
    public static void trainerRestoreAvailability(Session session, Trainer trainer, Schedule canceledTime) {
        // --- Restore trainer availability ---
        boolean restored = false;
        for (TrainerAvailability avail : trainer.getAvailabilities()) {
            if (avail.getDayOfWeek().equals(canceledTime.getDayOfWeek())) {
                LocalTime availStart = avail.getStartTime();
                LocalTime availEnd = avail.getEndTime();

                if (availEnd.getHour() <= canceledTime.getStartTime().getHour() ||
                        availStart.getHour() >= canceledTime.getEndTime().getHour())
                    continue;

                // Split availability
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
    public static void trainerSetAvailability() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Scanner scanner = HibernateUtil.getScanner();
        try {
            Trainer trainer = retrieveTrainer(session);
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

        // Retrieve the specific availability
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
    public static void trainerScheduleView() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Trainer trainer = retrieveTrainer(session);
            if (trainer == null) {
                return;
            }

            // Retrieve all personal training sessions for this trainer
            List<PersonalTrainingSession> sessions = session.createQuery(
                    "FROM PersonalTrainingSession p WHERE p.trainer = :trainer",
                    PersonalTrainingSession.class)
                    .setParameter("trainer", trainer)
                    .getResultList();

            if (sessions.isEmpty()) {
                System.out.println("\nNo scheduled sessions found for trainer: " + trainer.getName());
                // return;
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
            // THIS USES THAT INDEX
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
