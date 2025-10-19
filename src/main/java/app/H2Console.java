package app;

import org.h2.tools.Server;

public class H2Console {
    public static void main(String[] args) throws Exception {
        Server server = Server.createWebServer("-web", "-webPort", "8082").start();
        System.out.println("CTRL + C to exit");
        System.out.println("H2 console running at http://localhost:8082");
    }
}
