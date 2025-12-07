package com.hotel.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static final String PROPS_FILE = "db.properties";
    private static String url;
    private static String username;
    private static String password;

    static {
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream(PROPS_FILE)) {
            Properties props = new Properties();
            if (in != null) {
                props.load(in);
                url = props.getProperty("db.url");
                username = props.getProperty("db.username");
                password = props.getProperty("db.password");
                
                // Try alternate property names if not found
                if (url == null) url = props.getProperty("url");
                if (username == null) username = props.getProperty("user");
                if (password == null) password = props.getProperty("password");
                
                System.out.println("DB properties loaded from classpath");
            } else {
                System.err.println("Could not find " + PROPS_FILE + " on classpath. Will attempt to load from ../resources/" + PROPS_FILE);
                // Fallback: try to load from the sibling resources directory relative to working dir
                try {
                    File fallback = new File(".." + File.separator + "resources" + File.separator + PROPS_FILE);
                    if (fallback.exists()) {
                        try (InputStream fis = new FileInputStream(fallback)) {
                            props.load(fis);
                            url = props.getProperty("db.url");
                            username = props.getProperty("db.username");
                            password = props.getProperty("db.password");
                            
                            // Try alternate property names if not found
                            if (url == null) url = props.getProperty("url");
                            if (username == null) username = props.getProperty("user");
                            if (password == null) password = props.getProperty("password");
                            
                            System.err.println("Loaded DB properties from: " + fallback.getAbsolutePath());
                        }
                    } else {
                        System.err.println("Fallback properties file not found at: " + fallback.getAbsolutePath());
                    }
                } catch (IOException ex) {
                    System.err.println("Error loading fallback DB properties: " + ex.getMessage());
                }
            }
            
            if (url == null) {
                System.err.println("WARNING: DB URL not found in properties file!");
            }
        } catch (IOException e) {
            System.err.println("Error loading DB properties: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        if (url == null) throw new SQLException("DB url is not configured");
        // Set connection timeout to 3 seconds to fail fast
        DriverManager.setLoginTimeout(3);
        return DriverManager.getConnection(url, username, password);
    }
}
