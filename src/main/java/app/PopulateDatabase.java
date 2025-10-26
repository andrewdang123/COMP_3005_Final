package app;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import models.Admin;
import models.EquipmentManagement;
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
        Member member2 = new Member("Valorant", "valorant@gmail.com", "Male", 14, 6, 2005);
        session.persist(member2);

        // Trainer
        Trainer trainer1 = new Trainer("trainer1", "trainer1@gmail.com");
        TrainerAvailability trainerAvailability = new TrainerAvailability(trainer1, "Sunday", 1, 5);
        trainer1.addAvailability(trainerAvailability);
        session.persist(trainer1);
        session.persist(trainerAvailability);

        // Admin
        Admin admin1 = new Admin("admin1", "admin1@gmail.com");
        session.persist(admin1);

        // PersonalTrainingSession
        PersonalTrainingSession personalTrainingSession1 = new PersonalTrainingSession(member1, trainer1, 3, "MONDAY",
                3, 4);
        session.persist(personalTrainingSession1);

        // EquipmentManagement
        EquipmentManagement equipmentManagement1 = new EquipmentManagement(admin1, 444, "Water Leak", "In progress");
        session.persist(equipmentManagement1);

        // HealthMetric
        HealthMetric metric1 = new HealthMetric(member1, 285, 5);
        session.persist(metric1);

        // Commit and close
        session.getTransaction().commit();
        session.close();
        factory.close();

        System.out.println("Database saved");

    }
}
