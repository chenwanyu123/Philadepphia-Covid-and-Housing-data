package edu.upenn.cit594.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

public class Logger {
    //declare the constructor private to prevent clients from instantiating..
    private Logger(){}
    private static Logger instance = new Logger();
    private String path;
    private FileWriter fw = null;

    //the static method to get instance.
    public static Logger getInstance() {
        return instance;
    }

    public void init(String path) throws Exception {
        if (path!=null) {
            this.path = path;
        } else {
            System.err.println("the logfile name is not specified");
        }
        File log;
        try {
            log = new File(this.path);
            log.createNewFile(); // if file already exists will do nothing
            // if previous destination is a valid file, it will be closed
            if (fw!=null) {
                fw.close();
            }
        } catch (Exception e) {
            fw = null;
            throw new Exception("The logger cannot be correctly initialized");
        }
    }

    public void logString(String msg) throws Exception {
        // Try block to check for exceptions
        try {
            // Open given file in append mode by creating an
            // object of BufferedWriter class
            fw = new FileWriter(this.path, true);
            // Writing on output stream
            fw.write(msg);
            fw.flush();
            // Closing the connection
            // fw.close();
        }

        // Catch block to handle the exceptions
        catch (IOException e) {
            // Display message when exception occurs
            throw new Exception("No permission for the file" + e);
        }
    }
}
