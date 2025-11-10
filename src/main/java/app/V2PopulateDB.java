package app;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import models.Admin;
import models.ClassSchedule;
import models.EquipmentManagement;
import models.GroupFitnessClass;
import models.HealthMetric;
import models.Member;
import models.PersonalTrainingSession;
import models.Trainer;
import models.TrainerAvailability;

public class V2PopulateDB {

    public static void main(String[] args) {
        // If you already have a HibernateUtil, you can use that instead of new Configuration()
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();

        try {
            session.beginTransaction();

            // === put your v2 seed data below (change names/emails/days, etc.) ===

            Member m1 = new Member("Alice", "alice@example.com", "Female", 2, 3, 2001);
            session.persist(m1);

            HealthMetric hm1 = new HealthMetric(m1, 250, 10);
            session.persist(hm1);

            Member m2 = new Member("Bob", "bob@example.com", "Male", 5, 8, 1999);
            m2.addHealthMetric(315, 20);
            m2.addHealthMetric(290, 18);
            session.persist(m2);

            Trainer t1 = new Trainer("trainerV2", "trainerv2@example.com");
            t1.addAvailability(new TrainerAvailability(t1, "Tuesday", 2, 6));
            t1.addAvailability(new TrainerAvailability(t1, "Thursday", 1, 4));
            session.persist(t1);

            Admin admin = new Admin("adminV2", "adminv2@example.com");
            session.persist(admin);

            PersonalTrainingSession pts = new PersonalTrainingSession(m1, t1, 2, "TUESDAY", 2, 3);
            session.persist(pts);

            EquipmentManagement eq1 = new EquipmentManagement(admin);
            eq1.setDetails(101, "Bike: Squeaky pedal", "Open");
            session.persist(eq1);

            GroupFitnessClass spin = new GroupFitnessClass(t1, "Spin");
            session.persist(spin);
            spin.addMember(m1);
            spin.addMember(m2);

            ClassSchedule schedule = new ClassSchedule(spin, admin);
            session.persist(schedule);
            schedule.setDetails(2, "TUESDAY", 6, 7);

            session.getTransaction().commit();
            System.out.println("V2 seed complete.");
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
            factory.close();
        }
    }
}
