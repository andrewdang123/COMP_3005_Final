package app;

import models.Member;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class MainApp {
    public static void main(String[] args) {
        // Load configuration and build SessionFactory
        SessionFactory factory = new Configuration().configure().buildSessionFactory();

        // Open a Hibernate session
        Session session = factory.openSession();
        session.beginTransaction();

        // Create and persist a new member
        Member member = new Member("Alice");
        session.persist(member);

        // Commit and close
        session.getTransaction().commit();
        session.close();
        factory.close();

        System.out.println("Member saved: " + member.getName());
    }
}
