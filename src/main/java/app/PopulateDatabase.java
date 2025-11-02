package app;

import javax.swing.GroupLayout.Group;

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

public class PopulateDatabase {

    /***************************************************************
     * PopulateDatabase
     ***************************************************************/
    public static void main(String[] args) {
        // Load configuration and build SessionFactory
        SessionFactory factory = new Configuration().configure().buildSessionFactory();

        // Open a Hibernate session
        Session session = factory.openSession();
        session.beginTransaction();

        // Member
        Member member1 = new Member("Andrew", "andrewmtdang@gmail.com", "Male", 14, 6, 2005);
        session.persist(member1);

        // HealthMetric
        HealthMetric metric1 = new HealthMetric(member1, 285, 5);
        session.persist(metric1);

        Member member2 = new Member("Valorant", "valorant@gmail.com", "Male", 14, 6, 2005);
        member2.addHealthMetric(400, 50);
        member2.addHealthMetric(300, 50);
        session.persist(member2);

        Member member3 = new Member("Dylan", "andrewmtdang1@gmail.com", "Male", 14, 6, 2005);
        Member member4 = new Member("Kenny", "andrewmtdang2@gmail.com", "Male", 14, 6, 2005);
        Member member5 = new Member("David", "andrewmtdang3@gmail.com", "Male", 14, 6, 2005);
        session.persist(member3);
        session.persist(member4);
        session.persist(member5);

        // Trainer
        Trainer trainer1 = new Trainer("trainer1", "trainer1@gmail.com");
        TrainerAvailability trainerAvailability = new TrainerAvailability(trainer1, "Sunday", 1, 5);
        TrainerAvailability trainerAvailability2 = new TrainerAvailability(trainer1, "Monday", 1, 5);
        trainer1.addAvailability(trainerAvailability);
        trainer1.addAvailability(trainerAvailability2);
        session.persist(trainer1);

        // Admin
        Admin admin1 = new Admin("admin1", "admin1@gmail.com");
        session.persist(admin1);

        // PersonalTrainingSession
        PersonalTrainingSession personalTrainingSession1 = new PersonalTrainingSession(member1, trainer1, 3, "MONDAY",
                3, 4);
        session.persist(personalTrainingSession1);

        // EquipmentManagement
        EquipmentManagement equipmentManagement1 = new EquipmentManagement(admin1);
        equipmentManagement1.setDetails(444, "Water Leak", "In progress");
        session.persist(equipmentManagement1);

        // GroupFitnessClass
        GroupFitnessClass yoga = new GroupFitnessClass(trainer1, "Yoga");
        GroupFitnessClassMembers groupFitnessClassMembers1 = new GroupFitnessClassMembers(yoga, member1);

        session.persist(yoga);
        session.persist(groupFitnessClassMembers1);

        // GroupFitnessClass
        GroupFitnessClass calisthenics = new GroupFitnessClass(trainer1, "Calisthenics");
        session.persist(calisthenics);
        calisthenics.addMember(member1);
        calisthenics.addMember(member2);

        ClassSchedule classSchedule = new ClassSchedule(calisthenics, admin1);
        session.persist(classSchedule);
        classSchedule.setDetails(4, "MONDAY", 5, 7);

        // Commit and close
        session.getTransaction().commit();
        session.close();
        factory.close();

        System.out.println("Database saved");

    }
}
