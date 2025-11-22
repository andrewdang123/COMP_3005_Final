package app;

import app.Options.ViewFunctionEnum;

/**
 * MainApp is the entry point of the entire application.
 * - Calls Options.runView() to display the main menu and route the user
 *   to Member/Admin/Trainer functions based on their selection.
 * - Uses HibernateUtil to manage the SessionFactory lifecycle.
 * - When the program finishes, HibernateUtil.shutdown() cleanly closes
 *   the database connection and input scanner.
 * - This class only starts the app; all logic is handled in the other modules.
 */

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
