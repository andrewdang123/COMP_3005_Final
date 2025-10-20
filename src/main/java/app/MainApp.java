package app;

import app.Options.AdminFunctionEnum;
import app.Options.MemberFunctionEnum;
import app.Options.TrainerFunctionEnum;
//import models.Member;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class MainApp {


    /***************************************************************
     * MainApp
     ***************************************************************/
    public static void main(String[] args) {
        // Load configuration and build SessionFactory
        SessionFactory factory = new Configuration().configure().buildSessionFactory();

        // Open a Hibernate session
        Session session = factory.openSession();
        session.beginTransaction();
        Options.runView(AdminFunctionEnum.class);

        // Create and persist a new member
        //Member member = new Member("Andrew", "Male", "andrewmtdang@gmail.com");
        //Member member = new Member("Andrew", "Male", "andrewmtdang@gmail.com", 14, 6, 2005);
        //session.persist(member);

        // Commit and close
        session.getTransaction().commit();
        session.close();
        factory.close();

        //System.out.println("Member saved: " + member.getName());

    }

}
