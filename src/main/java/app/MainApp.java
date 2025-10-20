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
        Options.runView(MemberFunctionEnum.class);
        //Options.runView(TrainerFunctionEnum.class);
        //Options.runView(AdminFunctionEnum.class);



        // Commit and close
        session.getTransaction().commit();
        session.close();
        factory.close();

        //System.out.println("Member saved: " + member.getName());

    }

}
