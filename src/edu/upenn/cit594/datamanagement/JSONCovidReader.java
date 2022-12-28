package edu.upenn.cit594.datamanagement;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.CovidData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class JSONCovidReader implements CovidReader{
    protected String fileName;
    protected Logger logger = Logger.getInstance();

    public JSONCovidReader(String path) {
        fileName = path;
    }

    // convert object to string
    String objString(Object joo) {
        if (joo == null) {
            return null;
        } else {
            return joo.toString();
        }
    }

    // convert object to int
    Integer objInt(Object joo) {
        if (joo == null) {
            return null;
        } else {
            return Integer.parseInt(joo.toString());
        }
    }

    public HashMap<String, HashMap<String,CovidData>> getAllInfo() throws Exception {

        String time1 = Long.toString(System.currentTimeMillis());
        time1 = time1 + " " + this.fileName;
        logger.logString(time1);

        // HashMap: Key: zipcode, Values: ArrayList of CovidData
        HashMap<String, HashMap<String,CovidData>> df = new HashMap<String, HashMap<String,CovidData>>();

        try (FileReader fr = new FileReader(fileName)) {
            // parse JSON file
            Object obj = new JSONParser().parse(fr);
            JSONArray jo = (JSONArray) obj;
            for (int idx=0; idx<jo.size();idx++) {
                JSONObject joo = (JSONObject) jo.get(idx);
                // extract info
                String etlTimestamp = objString(joo.get("etl_timestamp"));
                if (etlTimestamp != null) {
                    etlTimestamp = etlTimestamp.substring(0, 10);
                }
                String zipcode = objString(joo.get("zip_code"));
                Integer NEG = objInt(joo.get("NEG"));
                Integer POS = objInt(joo.get("POS"));
                Integer deaths = objInt(joo.get("deaths"));
                Integer hospitalized = objInt(joo.get("hospitalized"));
                Integer partiallyVaccinated = objInt(joo.get("partially_vaccinated"));
                Integer fullyVaccinated = objInt(joo.get("fully_vaccinated"));
                Integer boosted = objInt(joo.get("boosted"));

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
            }
        }
        catch (FileNotFoundException e) {
            throw new Exception("Can not find the json file" + e);
        } catch (IOException e) {
            throw new Exception("Do not have the permission to read the json file" + e);
        } catch (ParseException e) {
            throw new Exception("Fail to parse the json file" + e);
        }

        return df;
    }
}
