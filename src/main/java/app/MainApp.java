package app;

import app.HibernateUtil;
import app.Options.AdminFunctionEnum;
import app.Options.MemberFunctionEnum;
import app.Options.TrainerFunctionEnum;
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
        
        Options.runView(MemberFunctionEnum.class);
        //Options.runView(TrainerFunctionEnum.class);
        //Options.runView(AdminFunctionEnum.class);



        HibernateUtil.shutdown();
        Scanner scanner = new Scanner(System.in);
        scanner.close();

    }

}
