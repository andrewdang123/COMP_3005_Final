package app;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import models.Admin;
import models.ClassSchedule;
import models.EquipmentManagement;
import models.GroupFitnessClass;
import models.GroupFitnessClassMembers;
import models.HealthMetric;
import models.Member;
import models.PersonalTrainingSession;
import models.Trainer;
import models.TrainerAvailability;

public class V2PopulateDB {

    /***************************************************************
     * V2PopulateDB
     ***************************************************************/
    public static void main(String[] args) {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        session.beginTransaction();

        // ===== Members =====
        Member member1 = new Member("Alice Nguyen", "alice_nguyen@gmail.com", "Female", 12, 5, 2002);
        member1.addHealthMetric(new HealthMetric(member1, 245, 12));
        session.persist(member1);

        Member member2 = new Member("Bruno Castillo", "bruno_castillo@gmail.com", "Male", 8, 9, 2001);
        member2.addHealthMetric(315, 18);
        member2.addHealthMetric(290, 16);
        session.persist(member2);

        Member member3 = new Member("Chloe Rossi", "chloe_rossi@gmail.com", "Female", 21, 3, 1999);
        member3.addHealthMetric(new HealthMetric(member3, 275, 10));
        session.persist(member3);

        Member member4 = new Member("Dev Patel", "dev_patel@gmail.com", "Male", 1, 1, 2000);
        member4.addHealthMetric(new HealthMetric(member4, 265, 11));
        session.persist(member4);

        Member member5 = new Member("Emma Wu", "emma_wu@gmail.com", "Female", 7, 7, 2003);
        member5.addHealthMetric(new HealthMetric(member5, 230, 9));
        session.persist(member5);

        Member member6 = new Member("Farah Ahmed", "farah_ahmed@gmail.com", "Female", 14, 2, 2002);
        member6.addHealthMetric(new HealthMetric(member6, 255, 13));
        session.persist(member6);

        Member member7 = new Member("Gabriel Sousa", "gabriel_sousa@gmail.com", "Male", 19, 6, 2001);
        member7.addHealthMetric(new HealthMetric(member7, 300, 17));
        session.persist(member7);

        Member member8 = new Member("Hana Kim", "hana_kim@gmail.com", "Female", 28, 4, 1998);
        member8.addHealthMetric(new HealthMetric(member8, 240, 12));
        session.persist(member8);

        Member member9 = new Member("Ivan Popov", "ivan_popov@gmail.com", "Male", 5, 12, 1999);
        member9.addHealthMetric(new HealthMetric(member9, 285, 14));
        session.persist(member9);

        Member member10 = new Member("Julia Marquez", "julia_marquez@gmail.com", "Female", 16, 10, 2000);
        member10.addHealthMetric(new HealthMetric(member10, 260, 11));
        session.persist(member10);

        // ===== Trainers =====
        Trainer trainer1 = new Trainer("Taylor Reed", "taylor_reed@gmail.com");
        TrainerAvailability t1a1 = new TrainerAvailability(trainer1, "MONDAY", 9, 12);
        TrainerAvailability t1a2 = new TrainerAvailability(trainer1, "MONDAY", 13, 17);
        TrainerAvailability t1a3 = new TrainerAvailability(trainer1, "WEDNESDAY", 10, 14);
        TrainerAvailability t1a4 = new TrainerAvailability(trainer1, "THURSDAY", 9, 11);
        trainer1.addAvailability(t1a1);
        trainer1.addAvailability(t1a2);
        trainer1.addAvailability(t1a3);
        trainer1.addAvailability(t1a4);
        session.persist(trainer1);

        Trainer trainer2 = new Trainer("Jordan Price", "jordan_price@gmail.com");
        TrainerAvailability t2a1 = new TrainerAvailability(trainer2, "TUESDAY", 8, 12);
        TrainerAvailability t2a2 = new TrainerAvailability(trainer2, "THURSDAY", 14, 18);
        TrainerAvailability t2a3 = new TrainerAvailability(trainer2, "MONDAY", 16, 18);
        trainer2.addAvailability(t2a1);
        trainer2.addAvailability(t2a2);
        trainer2.addAvailability(t2a3);
        session.persist(trainer2);

        Trainer trainer3 = new Trainer("Casey Morgan", "casey_morgan@gmail.com");
        TrainerAvailability t3a1 = new TrainerAvailability(trainer3, "FRIDAY", 9, 13);
        TrainerAvailability t3a2 = new TrainerAvailability(trainer3, "FRIDAY", 14, 17);
        TrainerAvailability t3a3 = new TrainerAvailability(trainer3, "WEDNESDAY", 8, 10);
        trainer3.addAvailability(t3a1);
        trainer3.addAvailability(t3a2);
        trainer3.addAvailability(t3a3);
        session.persist(trainer3);

        Trainer trainer4 = new Trainer("Riley Chen", "riley_chen@gmail.com");
        TrainerAvailability t4a1 = new TrainerAvailability(trainer4, "SATURDAY", 9, 12);
        TrainerAvailability t4a2 = new TrainerAvailability(trainer4, "SATURDAY", 13, 16);
        TrainerAvailability t4a3 = new TrainerAvailability(trainer4, "SUNDAY", 10, 13);
        trainer4.addAvailability(t4a1);
        trainer4.addAvailability(t4a2);
        trainer4.addAvailability(t4a3);
        session.persist(trainer4);

        // ===== Admins =====
        Admin admin1 = new Admin("Morgan Hale", "morgan_hale@gmail.com");
        session.persist(admin1);
        Admin admin2 = new Admin("Alex Rivera", "alex_rivera@gmail.com");
        session.persist(admin2);
        Admin admin3 = new Admin("Jamie Koh", "jamie_koh@gmail.com");
        session.persist(admin3);

        // ===== PersonalTrainingSessions =====
        session.persist(new PersonalTrainingSession(member1, trainer1, 1, "MONDAY", 9, 10));
        session.persist(new PersonalTrainingSession(member2, trainer1, 1, "MONDAY", 10, 11));
        session.persist(new PersonalTrainingSession(member3, trainer1, 1, "MONDAY", 14, 15));
        session.persist(new PersonalTrainingSession(member4, trainer1, 1, "WEDNESDAY", 11, 12));
        session.persist(new PersonalTrainingSession(member5, trainer2, 1, "TUESDAY", 9, 10));
        session.persist(new PersonalTrainingSession(member6, trainer2, 1, "TUESDAY", 10, 11));
        session.persist(new PersonalTrainingSession(member7, trainer3, 1, "FRIDAY", 9, 10));
        session.persist(new PersonalTrainingSession(member8, trainer3, 1, "FRIDAY", 15, 16));
        session.persist(new PersonalTrainingSession(member9, trainer4, 1, "SATURDAY", 10, 11));
        session.persist(new PersonalTrainingSession(member10, trainer4, 1, "SATURDAY", 13, 14));

        // ===== EquipmentManagement =====
        EquipmentManagement eq1 = new EquipmentManagement(admin2);
        EquipmentManagement eq2 = new EquipmentManagement(admin1);
        EquipmentManagement eq3 = new EquipmentManagement(admin3);
        EquipmentManagement eq4 = new EquipmentManagement(admin1);
        EquipmentManagement eq5 = new EquipmentManagement(admin2);
        EquipmentManagement eq6 = new EquipmentManagement(admin3);

        eq1.setDetails(104, "Treadmill #2: Belt alignment noise", "Open");
        eq2.setDetails(219, "Yoga Mat: Minor tear near edge", "In progress");
        eq3.setDetails(327, "Rowing Machine: Cable fraying", "Open");
        eq4.setDetails(451, "Dumbbell: Loose handle", "In progress");
        eq5.setDetails(508, "Elliptical #1: Resistance inconsistent", "Open");
        eq6.setDetails(623, "Cable Machine: Pulley squeak", "In progress");

        session.persist(eq1);
        session.persist(eq2);
        session.persist(eq3);
        session.persist(eq4);
        session.persist(eq5);
        session.persist(eq6);

        // ===== GroupFitnessClass =====
        GroupFitnessClass strength101 = new GroupFitnessClass(trainer1, "Strength 101");
        session.persist(strength101);
        session.persist(new GroupFitnessClassMembers(strength101, member1));
        session.persist(new GroupFitnessClassMembers(strength101, member4));

        ClassSchedule sched1 = new ClassSchedule(strength101, admin1);
        session.persist(sched1);
        sched1.setDetails(1, "MONDAY", 15, 16);

        GroupFitnessClass mobilityFlow = new GroupFitnessClass(trainer2, "Mobility Flow");
        session.persist(mobilityFlow);
        mobilityFlow.addMember(member5);
        mobilityFlow.addMember(member3);

        ClassSchedule sched2 = new ClassSchedule(mobilityFlow, admin2);
        session.persist(sched2);
        sched2.setDetails(1, "THURSDAY", 16, 17);

        GroupFitnessClass coreBlast = new GroupFitnessClass(trainer3, "Core Blast");
        session.persist(coreBlast);
        session.persist(new GroupFitnessClassMembers(coreBlast, member7));
        session.persist(new GroupFitnessClassMembers(coreBlast, member8));
        ClassSchedule sched3 = new ClassSchedule(coreBlast, admin3);
        session.persist(sched3);
        sched3.setDetails(1, "FRIDAY", 11, 12);

        GroupFitnessClass sundayStretch = new GroupFitnessClass(trainer4, "Sunday Stretch");
        session.persist(sundayStretch);
        session.persist(new GroupFitnessClassMembers(sundayStretch, member9));
        session.persist(new GroupFitnessClassMembers(sundayStretch, member10));
        ClassSchedule sched4 = new ClassSchedule(sundayStretch, admin1);
        session.persist(sched4);
        sched4.setDetails(1, "SUNDAY", 11, 12);

        session.getTransaction().commit();
        session.close();
        factory.close();

        System.out.println("V2 database saved.");
    }
}
