package main.java.ua.nure.bogun.apiproject.database;


import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import static main.java.ua.nure.bogun.apiproject.database.DBManager.close;

/**
 * Tool to run database scripts
 */
public class ScriptRunner {
    private static final Logger logger = Logger.getLogger(ScriptRunner.class);

    private ScriptRunner() {
    }

    public static void run(Connection con, File inputFile) {

        logger.info("Database initiating started...");
        // Delimiter
        String delimiter = ";";

        // Create scanner
        Scanner scanner;
        try {
            scanner = new Scanner(inputFile);
            scanner.useDelimiter(delimiter);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            logger.error("Database initiating error:", e1);
            return;
        }
        StringBuilder script = new StringBuilder();
        while(scanner.hasNext()){
            script.append(scanner.next()).append(delimiter);
        }
        String[] commands = script.toString().split(delimiter);
        for (String rawStatement: commands) {
            rawStatement+=";";
            Statement stmt = null;
            logger.info("Executing: "+rawStatement+"\n");
            try {
                stmt = con.createStatement();
                stmt.execute(rawStatement);

            } catch (SQLException ex) {
                ex.printStackTrace();
                logger.error("===\n"+rawStatement+"\n===\nDatabase initiating error:", ex);
            } finally {
                close(stmt);
            }
        }

        close(con);
        scanner.close();
        logger.info("Database initiating finished.");

    }
}