package edu.upenn.cit594;


import edu.upenn.cit594.datamanagement.*;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.Processor;
import edu.upenn.cit594.processor.ArgsParser;
import edu.upenn.cit594.ui.UserInterface;

import java.util.*;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {

        // check whether the arguments are valid
        ArgsParser argsP = new ArgsParser(args);
        TreeMap<String,String> seen = argsP.run();

        // log runtime arguments
        String out = Long.toString(System.currentTimeMillis());
        Logger logger = Logger.getInstance();

        String logFilename = null;
        if (seen.containsKey("log")) {
            logFilename = seen.get("log");
        }

        String covidFilename = null;
        if (seen.containsKey("covid")) {
            covidFilename = seen.get("covid");
        }

        String propertyDataFilename = null;
        if (seen.containsKey("properties")) {
            propertyDataFilename = seen.get("properties");
        }

        String populationFilename = null;
        if (seen.containsKey("population")) {
            populationFilename = seen.get("population");
        }

        logger.init(logFilename);

        // logs runtime arguments
        for(int i =0 ; i < args.length; i++){
            out = out + " " + args[i];
        }
        logger.logString(out);

        CovidReader covidReader = null;
        if (covidFilename == null) {
            // throw new Exception("No covid file input");
        } else {
            if(covidFilename.substring(covidFilename.length()-4).toLowerCase().equals(".csv")) {
                covidReader = new CSVCovidReader(covidFilename);
            } else if(covidFilename.substring(covidFilename.length()-5).toLowerCase().equals(".json")) {
                covidReader = new JSONCovidReader(covidFilename);
            } else {
                throw new Exception("Error: Covid data file should have .csv or .json extension");
            }
        }

        CSVPopulationReader popReader = null;
        if (populationFilename == null) {
            // throw new Exception("No population file input");
        } else {
            if(populationFilename.substring(populationFilename.length()-4).toLowerCase().equals(".csv")) {
                popReader = new CSVPopulationReader(populationFilename);
            } else {
                throw new Exception("Error: Population data file should have .csv extension");
            }
        }


        CSVPropertyReader propertyReader = null;
        if (propertyDataFilename == null) {
            // throw new Exception("No population file input");
        } else {
            if(propertyDataFilename.substring(propertyDataFilename.length()-4).toLowerCase().equals(".csv")) {
                propertyReader = new CSVPropertyReader(propertyDataFilename);
            } else {
                throw new Exception("Error: Property data file should have .csv extension");
            }
        }

        Processor processor = null;
        processor = new Processor(covidReader, popReader, propertyReader, seen);
        UserInterface ui = new UserInterface(processor);
        ui.start();
    }
}

