package app;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;

import models.Admin;
import models.ClassSchedule;
import models.ClassScheduleDetails;
import models.EquipmentManagement;
import models.EquipmentManagementDetails;
import models.GroupFitnessClass;
import models.Schedule;
import models.Trainer;

public class FunctionsAdmin {

    /***************************************************************
     * retrieveAdmin
     ***************************************************************/
    public static Admin retrieveAdmin(Session session) {
        Scanner scanner = HibernateUtil.getScanner();

        try {
            System.out.println("\n===================== Existing Admins ===================== ");
            var admins = session.createQuery("from Admin", Admin.class).list();

            if (admins.isEmpty()) {
                System.out.println("No admins found in the system");
                return null;
            }

            for (Admin a : admins) {
                System.out.println(a.toString());
            }
            System.out.println("===========================================================");

            Admin admin = null;
            boolean found = false;

            while (!found) {
                System.out.print("\nEnter the Admin ID: ");
                Long adminId = Long.parseLong(scanner.nextLine().trim());

                admin = session.get(Admin.class, adminId);

                if (admin == null) {
                    System.out.println("\nNo admin found with ID: " + adminId);
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

            return admin;

        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            return null;
        }
    }

    /***************************************************************
     * adminEquipmentMaintenance
     ***************************************************************/
    public static void adminEquipmentMaintenance() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Scanner scanner = HibernateUtil.getScanner();

        try {
            Admin admin = retrieveAdmin(session);
            if (admin == null) {
                return;
            }

            // Submenu loop
            while (true) {
                System.out.println("\nDo you want to:");
                System.out.println("0. Exit");
                System.out.println("1. Update/View reports");
                System.out.println("2. Report another Issue");
                System.out.print("Enter choice (0 - 2): ");

                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1":
                        adminEquipmentMaintenanceUpdate(session, admin);
                        break;
                    case "2":
                        adminEquipmentMaintenanceAdd(session, admin);
                        break;
                    case "0":
                        System.out.println("Returning...");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error updating profile: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    /***************************************************************
     * adminEquipmentMaintenanceUpdate
     ***************************************************************/
    private static void adminEquipmentMaintenanceUpdate(Session session, Admin admin) {
        Scanner scanner = HibernateUtil.getScanner();
        try {
            EquipmentManagement equipmentManagement = FunctionsExtra.retrieveEquipmentManagement(session);
            if (equipmentManagement == null) {
                System.out.println("No equipment found to update.");
                return;
            }

            // Validate room number input
            Integer roomNum = null;
            while (roomNum == null) {
                System.out.print("Room number: ");
                String input = scanner.nextLine().trim();
                try {
                    roomNum = Integer.parseInt(input);
                    if (roomNum <= 0) {
                        System.out.println("Room number must be a positive integer.");
                        roomNum = null;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid integer room number.");
                }
            }

            System.out.print("Issue: ");
            String issue = scanner.nextLine().trim();

            System.out.print("RepairStatus: ");
            String repairStatus = scanner.nextLine().trim();
            if (repairStatus.isEmpty())
                repairStatus = "In progress";

            session.beginTransaction();
            equipmentManagement.setAdmin(admin);

            // Update existing details instead of replacing
            EquipmentManagementDetails details = equipmentManagement.getDetails();
            if (details == null) {
                details = new EquipmentManagementDetails();
                details.setEquipment(equipmentManagement);
                equipmentManagement.setDetails(details);
            }

            details.setRoomNum(roomNum);
            details.setIssue(issue);
            details.setRepairStatus(repairStatus);

            session.merge(equipmentManagement);
            session.getTransaction().commit();

            System.out.println("Equipment details updated successfully!");

        } catch (Exception ex) {
            if (session.getTransaction().isActive())
                session.getTransaction().rollback();
            System.out.println("Failed to update details: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /***************************************************************
     * adminEquipmentMaintenanceAdd
     ***************************************************************/
    private static void adminEquipmentMaintenanceAdd(Session session, Admin admin) {
        Scanner scanner = HibernateUtil.getScanner();
        try {
            System.out.print("Room number: ");
            int room = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Issue: ");
            String issue = scanner.nextLine().trim();

            System.out.print("RepairStatus: ");
            String status = scanner.nextLine().trim();
            if (status.isEmpty())
                status = "In progress";

            session.beginTransaction();

            // parent EquipmentManagement row to satisfy FK
            EquipmentManagement equipmentManagement = new EquipmentManagement(admin);
            equipmentManagement.setDetails(room, issue, status);
            session.persist(equipmentManagement);

            session.getTransaction().commit();

            System.out.println("New issue recorded for equipmentId " + equipmentManagement.getEquipmentId() + ".");
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid room number. Cancelled.");
        } catch (Exception e) {
            if (session.getTransaction().isActive())
                session.getTransaction().rollback();
            System.out.println("Failed to add issue: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /***************************************************************
     * adminClassManagement
     ***************************************************************/
    public static void adminClassManagement() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Scanner scanner = HibernateUtil.getScanner();

        try {
            Admin admin = retrieveAdmin(session);
            if (admin == null) {
                return;
            }

            while (true) {
                System.out.println("\n===================== Class Management =====================");
                System.out.println("0. Exit");
                System.out.println("1. Define a new class");
                System.out.println("2. Assign trainer / room / time to a class");
                System.out.print("Enter choice (0 - 2): ");

                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1":
                        adminClassManagementDefineNewClass(session);
                        break;
                    case "2":
                        adminClassManagementAssignTrainerRoomTime(session, admin);
                        break;
                    case "0":
                        System.out.println("Returning...");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error during class management: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // ====== just for testing ======

    /***************************************************************
     * adminClassManagementDefineNewClass
     ***************************************************************/
    private static void adminClassManagementDefineNewClass(Session session) {
        Scanner scanner = HibernateUtil.getScanner();

        try {
            System.out.print("Enter class name: ");
            String className = scanner.nextLine().trim();

            if (className.isEmpty()) {
                System.out.println("Class name cannot be empty.");
                return;
            }

            Trainer trainer = FunctionsTrainer.retrieveTrainer(session);
            if (trainer == null) {
                System.out.println("No trainer selected. Class will be created without a trainer.");
                return;
            }
            trainer.printAvailabilities();

            session.beginTransaction();

            GroupFitnessClass gfc = new GroupFitnessClass();
            gfc.setClassName(className);
            if (trainer != null) {
                gfc.setTrainer(trainer);
            }
            /*
             * I removed the option to choose whether you want a trainer. Trainer should be
             * mandatory
             * 
             * What you need to change. Create the gfc with this. GroupFitnessClass gfc =
             * new GroupFitnessClass(trainer, className);
             * 
             * You must also update the trainers availability. I would ask for the class
             * time now. Check out what I did in memberPtSessionSchedulingBookPrompt()
             * on how to update the time. I created two functions called
             * trainerCheckAvailability and trainerAdjustAvailability
             * which would be good help for you
             * 
             * This will automatically create set the capacity at the default. However, you
             * should manually ask the user for the capacity.
             * If valid, update the capacity (is an int, and the current members is not
             * greater than capacity (not relevant now, but when they update it))
             * 
             * 
             * 
             * You must also update the schedule entity. This includes the fitnessclass, and
             * admin
             * You also need the sched details, the roomnumber, dayofweek, start and end
             * time
             * Example sched4.setDetails(1, "SUNDAY", 11, 12);
             * 
             * 
             * 
             * 
             */

            session.persist(gfc);
            session.getTransaction().commit();

            System.out.println("New class created with ID " + gfc.getClassId() + ".");

        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            System.out.println("Failed to define new class: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /***************************************************************
     * adminClassManagementAssignTrainerRoomTime
     ***************************************************************/
    private static void adminClassManagementAssignTrainerRoomTime(Session session, Admin admin) {
        Scanner scanner = HibernateUtil.getScanner();

        try {
            GroupFitnessClass gfc = FunctionsExtra.retrieveGroupFitnessClass(session);
            if (gfc == null) {
                return;
            }

            Trainer trainer = FunctionsTrainer.retrieveTrainer(session);
            if (trainer == null) {
                System.out.println("No trainer selected. Aborting.");
                return;
            }

            System.out.print("Room number: ");
            int roomNum = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Day of week (e.g., MONDAY): ");
            String dayInput = scanner.nextLine().trim().toUpperCase();

            DayOfWeek dayOfWeek;
            try {
                dayOfWeek = DayOfWeek.valueOf(dayInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid day of week. Aborting.");
                return;
            }

            System.out.print("Start time (hour 0-23): ");
            int startHour = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("End time (hour 0-23): ");
            int endHour = Integer.parseInt(scanner.nextLine().trim());

            session.beginTransaction();

            gfc.setTrainer(trainer);

            List<ClassSchedule> schedules = session.createQuery(
                    "FROM ClassSchedule cs WHERE cs.groupFitnessClass = :gfc",
                    ClassSchedule.class).setParameter("gfc", gfc)
                    .getResultList();

            ClassSchedule schedule;

            if (schedules.isEmpty()) {
                schedule = new ClassSchedule(gfc, admin);
                schedule.setDetails(roomNum, dayOfWeek, startHour, endHour);
                session.persist(schedule);

            } else {
                schedule = schedules.get(0);
                ClassScheduleDetails details = schedule.getDetails();
                if (details == null) {
                    schedule.setDetails(roomNum, dayOfWeek, startHour, endHour);
                } else {
                    details.setRoomNum(roomNum);
                    Schedule scheduleTime = details.getScheduleTime();
                    if (scheduleTime == null) {
                        scheduleTime = new Schedule(dayOfWeek, startHour, endHour);
                        details.setScheduleTime(scheduleTime);
                    } else {
                        scheduleTime.setDayOfWeek(dayOfWeek);
                        scheduleTime.setStartTime(startHour);
                        scheduleTime.setEndTime(endHour);
                    }
                }
            }

            session.getTransaction().commit();

            System.out.println("Trainer, room, and time assigned for class \"" +
                    gfc.getClassName() + "\" (Class ID: " + gfc.getClassId() + ").");

        } catch (NumberFormatException nfe) {
            System.out.println("Invalid numeric input. Cancelled.");
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            System.out.println("Failed to assign trainer/room/time: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
