package main.java.ua.nure.bogun.apiproject.database;

import main.java.ua.nure.bogun.apiproject.service.PropertyWorker;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import static main.java.ua.nure.bogun.apiproject.service.PropertyWorker.readPropertyConnection;


public class DBManager {

    private static final Logger logger = Logger.getLogger(DBManager.class);
    // =================== singleton
    static String CONNECTION_URL;
    // =================== singleton

    private static DBManager instance;

        static Connection getConnection(String url) throws SQLException {
        return DriverManager.getConnection(url);
    }
    public static synchronized DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    DBManager() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        CONNECTION_URL = readPropertyConnection("db.properties");
    }

    public static void createDB(){
        Connection con = null;
        try {

            con = DBManager.getInstance().getConnection(CONNECTION_URL);
            System.out.println(CONNECTION_URL);
            ScriptRunner.run(con, new File(
                    PropertyWorker.readProperty("db.properties", "connection.init.path")
            ));
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close(con);
        }
    }
    static void close(AutoCloseable ac) {
        if (ac != null) {
            try {
                ac.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    static void rollback(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    static void commitAndClose(Connection con) {
        if (con != null) {
            try {
//                con.commit();
                con.close();
            } catch (SQLException ex) {
                logger.error("Cannot commit transaction and close connection", ex);
            }
        }
    }
}

