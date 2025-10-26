package app;

import java.util.*;

import org.hibernate.Session;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import models.LatestHealthMetricDTO;
import services.MemberService;

public class FunctionsTrainer {
    /***************************************************************
     * trainerSetAvailability
     ***************************************************************/
    public static void trainerSetAvailability() {
        System.out.println("Availability Set!");
    }

    /***************************************************************
     * trainerScheduleView
     ***************************************************************/
    public static void trainerScheduleView() {
        System.out.println("Viewed Schedule!");
    }

    /***************************************************************
     * trainerMemberLookup
     ***************************************************************/
    public static void trainerMemberLookup() {
        System.out.println("Member Viewed!");
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            MemberService memberService = new MemberService(session);

            // Begin transaction
            session.beginTransaction();

            // Fetch latest health metrics
            List<LatestHealthMetricDTO> latestMetrics = memberService.getLatestHealthMetrics();

            // Display results
            for (LatestHealthMetricDTO metric : latestMetrics) {
                System.out.println(metric.getMemberName() +
                        " | Weight: " + metric.getCurrentWeight() +
                        " | BMI: " + metric.getCurrentBmi() +
                        " | Timestamp: " + metric.getTimestamp());
            }

            // Commit transaction
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

}
