package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.CovidData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CSVCovidReader extends CSVParser implements CovidReader{

    protected String filename;
    protected Logger logger = Logger.getInstance();

    public CSVCovidReader(String filename) {
        this.filename = filename;
    }


    public HashMap<String, HashMap<String,CovidData>> getAllInfo() throws Exception {

        String time1 = Long.toString(System.currentTimeMillis());
        time1 += " " + this.filename;
        logger.logString(time1);

        // HashMap: Key1: zipcode, Key2: timestamp, Values: CovidData
        HashMap<String, HashMap<String,CovidData>> df = new HashMap<String, HashMap<String,CovidData>>();
        int etlTimestampIdx = -1;
        int zipcodeIdx = -1;
        int NEGIdx = -1;
        int POSIdx = -1;
        int deathsIdx = -1;
        int hospitalizedIdx = -1;
        int partialIdx = -1;
        int fullyIdx =  -1;
        int boostedIdx = -1;

        String etlTimestamp = null;
        String zipcode = null;
        Integer NEG = null;
        Integer POS = null;
        Integer deaths = null;
        Integer hospitalized = null;
        Integer partiallyVaccinated = null;
        Integer fullyVaccinated = null;
        Integer boosted = null;

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
                        if (line.get(i).equals("etl_timestamp")) {
                            etlTimestampIdx = i;
                        } else if (line.get(i).equals("zip_code")) {
                            zipcodeIdx = i;
                        } else if (line.get(i).equals("NEG")) {
                            NEGIdx = i;
                        } else if (line.get(i).equals("POS")) {
                            POSIdx = i;
                        } else if (line.get(i).equals("deaths")) {
                            deathsIdx = i;
                        } else if (line.get(i).equals("hospitalized")) {
                            hospitalizedIdx = i;
                        } else if (line.get(i).equals("partially_vaccinated")) {
                            partialIdx = i;
                        } else if (line.get(i).equals("fully_vaccinated")) {
                            fullyIdx = i;
                        } else if (line.get(i).equals("boosted")) {
                            boostedIdx = i;
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
                    if (etlTimestampIdx!=-1) { etlTimestamp = line.get(etlTimestampIdx).substring(0, 10); }
                    if (zipcodeIdx!=-1) { zipcode = line.get(zipcodeIdx); }
                    if (NEGIdx!=-1 && line.get(NEGIdx)!="") { NEG = Integer.parseInt(line.get(NEGIdx)); }
                    else { NEG = null; }
                    if (POSIdx!=-1 && line.get(POSIdx)!="") { POS = Integer.parseInt(line.get(POSIdx)); }
                    else { POS = null; }
                    if (deathsIdx!=-1 && line.get(deathsIdx)!="") { deaths = Integer.parseInt(line.get(deathsIdx)); }
                    else { deaths = null; }
                    if (hospitalizedIdx!=-1 && line.get(hospitalizedIdx)!="") { hospitalized = Integer.parseInt(line.get(hospitalizedIdx)); }
                    else { hospitalized = null; }
                    if (partialIdx!=-1 && line.get(partialIdx)!="") { partiallyVaccinated = Integer.parseInt(line.get(partialIdx)); }
                    else { partiallyVaccinated = null; }
                    if (fullyIdx!=-1 && line.get(fullyIdx)!="") { fullyVaccinated = Integer.parseInt(line.get(fullyIdx)); }
                    else { fullyVaccinated = null; }
                    if (boostedIdx!=-1 && line.get(boostedIdx)!="") { boosted = Integer.parseInt(line.get(boostedIdx)); }
                    else { boosted = null; }

                    CovidData record = new CovidData(etlTimestamp, zipcode, NEG, POS, deaths,
                                                hospitalized, partiallyVaccinated, fullyVaccinated, boosted);


                    HashMap<String,CovidData> time;
                    if (df.containsKey(zipcode)){
                        time = df.get(zipcode);
                        time.put(etlTimestamp, record);
                        df.put(zipcode, time);
                    }
                    else{
                        time = new HashMap<String,CovidData>();
                        time.put(etlTimestamp, record);
                        df.put(zipcode, time);
                    }

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
