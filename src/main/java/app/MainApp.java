package app;

import app.HibernateUtil;
import app.Options.ViewFunctionEnum;
//import models.Member;
import org.hibernate.cfg.Configuration;

import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class MainApp {

    /***************************************************************
     * MainApp
     ***************************************************************/
    public static void main(String[] args) {
        // Load configuration and build SessionFactory

        Options.runView(ViewFunctionEnum.class);

        HibernateUtil.shutdown();

    }

}
