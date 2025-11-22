package app;

import java.util.Scanner;

public class Options {

    /**
     * Options defines the entire text-based menu system for the app.
     * - FunctionEnum is a small “command” interface that every menu enum implements.
     * - MemberFunctionEnum / TrainerFunctionEnum / AdminFunctionEnum each represent
     *   a role-specific menu: each constant maps a code + description to a Runnable
     *   that calls the corresponding Functions* method.
     * - ViewFunctionEnum is the top-level menu (choose Member / Trainer / Admin);
     *   MainApp calls runView(ViewFunctionEnum.class) to start here.
     * - runView(...) is a generic menu loop: it prints options, reads a code,
     *   finds the matching enum constant, and executes its action.
     * - This file is all application logic: it does not use any database views,
     *   triggers, or indexes directly – it just routes to the functions that do.
     */


    public static void placeholder() {
        System.out.println("FUBAR");
    }

    public static void exit() {
        System.out.println("Exiting...");
        return;
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
            String enumName = enumClass.getSimpleName()
                    .replace("FunctionEnum", "")
                    .toUpperCase();
            System.out.println("\n=== " + enumName + " MENU OPTIONS ===");
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
        MEMBER_USER_REGISTRATION(1, "Register as a new user", () -> FunctionsMember.memberUserRegistration()),
        MEMBER_PROFILE_MANAGEMENT(2, "Manage your profile", () -> FunctionsMember.memberProfileManagement()),
        MEMBER_HEALTH_HISTORY(3, "Log current health metrics", () -> FunctionsMember.memberHealthHistory()),
        MEMBER_PT_SESSION_SCHEDULING(4, "Book or reschedule with a trainer",
                () -> FunctionsMember.memberPtSessionScheduling()),
        MEMBER_GROUP_CLASS_REGISTRATION(5, "Register for a scheduled class",
                () -> FunctionsMember.memberGroupClassRegistration());

        private final int code;
        private final String description;
        private final Runnable action;

        MemberFunctionEnum(int code, String description, Runnable action) {
            this.code = code;
            this.description = description;
            this.action = action;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public void execute() {
            action.run();
        }

    }

    /***************************************************************
     * TrainerFunctionEnum
     ***************************************************************/
    public enum TrainerFunctionEnum implements FunctionEnum {
        EXIT(0, "EXIT", () -> exit()),
        TRAINER_SET_AVAILABILITY(1, "Define time when available for sessions or classes",
                () -> FunctionsTrainer.trainerSetAvailability()),
        TRAINER_SCHEDULE_VIEW(2, "See assigned PT sessions and classes", () -> FunctionsTrainer.trainerScheduleView()),
        TRAINER_MEMBER_LOOKUP(3, "Lookup member goals and metrics by name",
                () -> FunctionsTrainer.trainerMemberLookup());

        private final int code;
        private final String description;
        private final Runnable action;

        TrainerFunctionEnum(int code, String description, Runnable action) {
            this.code = code;
            this.description = description;
            this.action = action;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public void execute() {
            action.run();
        }

    }

    /***************************************************************
     * AdminFunctionEnum
     ***************************************************************/
    public enum AdminFunctionEnum implements FunctionEnum {
        EXIT(0, "EXIT", () -> exit()),
        ADMIN_EQUIPMENT_MAINTENANCE(1, "Log issues, track repair status, associated with equipment",
                () -> FunctionsAdmin.adminEquipmentMaintenance()),
        ADMIN_CLASS_MANAGEMENT(2, "Define new classes, assign trainers room/time, update schedules",
                () -> FunctionsAdmin.adminClassManagement());

        private final int code;
        private final String description;
        private final Runnable action;

        AdminFunctionEnum(int code, String description, Runnable action) {
            this.code = code;
            this.description = description;
            this.action = action;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public void execute() {
            action.run();
        }

    }

    /***************************************************************
     * ViewFunctionEnum
     ***************************************************************/
    public enum ViewFunctionEnum implements FunctionEnum {
        EXIT(0, "EXIT", () -> exit()),
        VIEW_MEMBER_FUNCTIONS(1, "Member Functions", () -> runView(MemberFunctionEnum.class)),
        VIEW_TRAINER_FUNCTIONS(2, "Trainer Functions", () -> runView(TrainerFunctionEnum.class)),
        VIEW_ADMIN_FUNCTIONS(3, "Administrative Staff Functions", () -> runView(AdminFunctionEnum.class));

        private final int code;
        private final String description;
        private final Runnable action;

        ViewFunctionEnum(int code, String description, Runnable action) {
            this.code = code;
            this.description = description;
            this.action = action;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public void execute() {
            action.run();
        }

    }

    /***************************************************************
     * runView
     ***************************************************************/
    public static <E extends Enum<E> & FunctionEnum> void runView(Class<E> enumClass) {
        Scanner scanner = HibernateUtil.getScanner();

        while (true) {
            FunctionEnum.printMenu(enumClass);
            System.out.print("Enter command number: ");

            String input = scanner.nextLine().trim();
            int code;

            try {
                code = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid command number.");
                continue;
            }

            E function = FunctionEnum.fromCode(enumClass, code);
            if (function == null) {
                System.out.println("Invalid command.");
                continue;
            }

            function.execute();

            try {
                E exitEnum = Enum.valueOf(enumClass, "EXIT");
                if (function == exitEnum)
                    break;
            } catch (IllegalArgumentException ignored) {
            }
        }
    }


}
