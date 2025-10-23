package app;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import models.Admin;
import models.Member;
import models.PersonalTrainingSession;

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

        // Trainer trainer1 = new Trainer("trainer1", "trainer1@gmail.com");
        // session.persist(trainer1);
        Admin admin1 = new Admin("admin1", "admin1@gmail.com");
        session.persist(admin1);

        // personaltraining
        PersonalTrainingSession tsession1 = new PersonalTrainingSession(134L, 223L, 3, 54);
        session.persist(tsession1);
        // Commit and close
        session.getTransaction().commit();
        session.close();
        factory.close();

        System.out.println("Database saved");

    }
}
