package app;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import models.Admin;
import models.EquipmentManagement;
import models.Member;
import models.PersonalTrainingSession;
import models.Trainer;
import models.HealthMetric;

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

        // Create and persist a new member
        Member member1 = new Member("Andrew", "andrewmtdang@gmail.com", "Male", 14, 6, 2005);
        session.persist(member1);
        Member member2 = new Member("Valorant", "valorant@gmail.com", "Male", 14, 6, 2005);
        session.persist(member2);

        Trainer trainer1 = new Trainer("trainer1", "trainer1@gmail.com", "tuesday", 8, 17);
        session.persist(trainer1);

        Admin admin1 = new Admin("admin1", "admin1@gmail.com");
        session.persist(admin1);

        // personaltraining
        PersonalTrainingSession personalTrainingSession1 = new PersonalTrainingSession(134L, 223L, 3, 54);
        session.persist(personalTrainingSession1);

        // EquipmentManagement
        EquipmentManagement equipmentManagement1 = new EquipmentManagement(1L, 444, "tre", "half");
        session.persist(equipmentManagement1);

        // HealthMetric
        HealthMetric metric1 = new HealthMetric(1L, 285, 5);
        session.persist(metric1);

        // Commit and close
        session.getTransaction().commit();
        session.close();
        factory.close();

        System.out.println("Database saved");

    }
}
