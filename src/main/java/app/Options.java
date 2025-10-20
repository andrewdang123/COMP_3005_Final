package app;

import models.Member;
import java.util.Scanner;
import org.hibernate.cfg.Configuration;
import org.h2.command.Command;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class Options {

    public static void test(){
        System.out.println("TEST");
    }
    public static void exit(){
        System.out.println("exit");
    }

    /***************************************************************
     * FunctionEnum
     ***************************************************************/
    private interface FunctionEnum {
        int getCode();
        String getDescription();
        void execute();

        static <E extends Enum<E> & FunctionEnum> E fromCode(Class<E> enumClass, int code) {
            for (E e : enumClass.getEnumConstants()) {
                if (e.getCode() == code) {
                    return e;
                }
            }
            return null;
        }

        static <E extends Enum<E> & FunctionEnum> void printMenu(Class<E> enumClass) {
            System.out.println("Available options:");
            for (E e : enumClass.getEnumConstants()) {
                System.out.println(e.getCode() + " - " + e.getDescription());
            }
        }
    }
    


    /***************************************************************
     * MemberFunctionEnum
     ***************************************************************/
    public enum MemberFunctionEnum implements FunctionEnum {
        EXIT(0, "EXIT", () -> exit()),
        MEMBER_USER_REGISTRATION(1, "Register as a new user", () -> test()),
        MEMBER_PROFILE_MANAGEMENT(2, "Manage your profile", () -> test()),
        //MEMBER_HEALTH_HISTORY,
        //MEMBER_DASHBOARD,
        MEMBER_PT_SESSION_SCHEDULING(3, "Book or reschedule with a trainer", () -> test()),
        MEMBER_GROUP_CLASS_REGISTRATION(4, "Register for a scheduled class", () -> test());

        private final int code;
        private final String description;
        private final Runnable action;

        MemberFunctionEnum(int code, String description, Runnable action) {
            this.code = code;
            this.description = description;
            this.action = action;
        }
        @Override
        public int getCode() { return code; }
        @Override
        public String getDescription() { return description; }
        @Override
        public void execute() { action.run(); }

    }

    /***************************************************************
     * TrainerFunctionEnum
     ***************************************************************/
    public enum TrainerFunctionEnum implements FunctionEnum {
        EXIT(0, "EXIT", () -> exit()),
        TRAINER_SET_AVAILABILITY(1, "Define time when available for sessions or classes", () -> test()),
        TRAINER_SCHEDULE_VIEW(2, "See assigned PT sessions and classes", () -> test());
        //TRAINER_MEMBER_LOOKUP,

        private final int code;
        private final String description;
        private final Runnable action;

        TrainerFunctionEnum(int code, String description, Runnable action) {
            this.code = code;
            this.description = description;
            this.action = action;
        }
        @Override
        public int getCode() { return code; }
        @Override
        public String getDescription() { return description; }
        @Override
        public void execute() { action.run(); }

    }

    /***************************************************************
     * AdminFunctionEnum
     ***************************************************************/
    public enum AdminFunctionEnum implements FunctionEnum {
        EXIT(0, "EXIT", () -> exit()),
        //ADMIN_ROOM_BOOKING,
        ADMIN_EQUIPMENT_MAINTENANCE(1, "Log issues, track repair status, associated with equipment", () -> test()),
        ADMIN_CLASS_MANAGEMENT(2, "Define new classes, assign trainers room/time, update schedules", () -> test());
        //ADMIN_BILLING_AND_PAYMENT,

        private final int code;
        private final String description;
        private final Runnable action;

        AdminFunctionEnum(int code, String description, Runnable action) {
            this.code = code;
            this.description = description;
            this.action = action;
        }
        @Override
        public int getCode() { return code; }
        @Override
        public String getDescription() { return description; }
        @Override
        public void execute() { action.run(); }

    }
    

    



    /***************************************************************
     * runView
     ***************************************************************/
    public static <E extends Enum<E> & FunctionEnum> void runView(Class<E> enumClass) {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            FunctionEnum.printMenu(enumClass);
            System.out.print("Enter command number: ");
            while (!scanner.hasNextInt()) {

                scanner.next();
                System.out.print("Enter valid command number: ");
            }
            int code = scanner.nextInt();

            E function = FunctionEnum.fromCode(enumClass, code);
            if (function == null) {
                System.out.println("Invalid command.");
                continue;
            }

            function.execute();

            try {
                E exitEnum = Enum.valueOf(enumClass, "EXIT");
                if (function == exitEnum) break;
            } catch (IllegalArgumentException ignored) {}
        }

        scanner.close();
    }

        
}

