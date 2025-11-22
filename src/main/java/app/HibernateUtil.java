package app;

import java.util.Scanner;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


/**
 * HibernateUtil is the central setup class for Hibernate.
 * - Builds the SessionFactory once at startup using hibernate.cfg.xml.
 * - Provides a shared Scanner for all user input.
 * - getSessionFactory() is used across the app to open sessions for DB work.
 * - shutdown() closes both the SessionFactory and the input scanner when the app ends.
 * - This class handles all Hibernate configuration so other classes can focus on logic.
 */

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();
    private static final Scanner scanner = new Scanner(System.in);

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Scanner getScanner() {
        return scanner;
    }

    public static void shutdown() {
        getSessionFactory().close();
        scanner.close();
    }

}
