package app;

import org.h2.tools.Server;

/**
 * This class simply launches the H2 database web console.
 * - Starts an H2 web server on port 8082.
 * - Lets me open http://localhost:8082 in a browser to view tables, indexes, and data.
 * - Used only for debugging and inspecting the database during development.
 */

public class H2Console {
    public static void main(String[] args) throws Exception {
        Server server = Server.createWebServer("-web", "-webPort", "8082").start();
        System.out.println("CTRL + C to exit");
        System.out.println("H2 console running at http://localhost:8082");
    }
}
