package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.logging.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CSVPopulationReader extends CSVParser implements PopulationReader{

    protected String filename;
    protected Logger logger = Logger.getInstance();

    public CSVPopulationReader(String filename) {
        this.filename = filename;
    }


    public HashMap<String, Integer> getAllInfo() throws Exception {

        String time = Long.toString(System.currentTimeMillis());
        time += " " + this.filename;
        logger.logString(time);

        // HashMap: Key: zipcode, Values: ArrayList of zipcode, totallivable, market_value
        HashMap<String, Integer> df = new HashMap<String, Integer>();
        int zipcodeIdx = -1;
        int populationIdx = -1;

        String zipcode = null;
        Integer population = null;

        try (FileReader fr = new FileReader(this.filename);
             BufferedReader br = new BufferedReader(fr)) {
            // Declaring a string variable
            String row;
            ArrayList<String> line = null;
            // Condition holds true till
            // there is character in a string
            while ((row = br.readLine()) != null) {
                // check the header
                line = super.ParserMultiLines(row,line);
                int lineLen = line.size();
                // end of line
                if (line.get(lineLen-1) == "F") {
                    // the last index is flag of openQuota
                    for (int i=0;i<lineLen-1;i++) {
                        // save headers index
                        if (line.get(i).equals("population")) {
                            populationIdx = i;
                        } else if (line.get(i).equals("zip_code")) {
                            zipcodeIdx = i;
                        }
                    }
                    // reset line
                    line = null;
                    break;
                }
            }

            // scan the rest of the file and save the data into the map
            while ((row = br.readLine()) != null) {
                line = super.ParserMultiLines(row, line);
                // end of the data record
                if (line.get(line.size() - 1) == "F") {
                    if (zipcodeIdx!=-1) { zipcode = line.get(zipcodeIdx); }
                    if (populationIdx!=-1 && line.get(populationIdx)!="") { population = Integer.parseInt(line.get(populationIdx)); }
                    else { population = null; }

                    df.put(zipcode, population);
                    // reset line
                    line = null;
                }
            }
        }

        catch (FileNotFoundException e) {
            throw new Exception("Can not find the cvs file" + e);
        }

        catch (IOException e) {
            throw new Exception("Do not have the permission to read the csv file" + e);
        }
        return df;
    }

}
