package app;

import java.util.Scanner;
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;

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
