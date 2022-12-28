package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.PropertyData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CSVPropertyReader extends CSVParser implements PropertyReader{

    protected String filename;
    protected Logger logger = Logger.getInstance();

    public CSVPropertyReader(String filename) {
        this.filename = filename;
    }


    public HashMap<String, ArrayList<PropertyData>> getAllInfo() throws Exception {

        String time = Long.toString(System.currentTimeMillis());
        time += " " + this.filename;
        logger.logString(time);

        // HashMap: Key: zipcode, Values: ArrayList of zipcode, totallivable, market_value
        HashMap<String, ArrayList<PropertyData>> df = new HashMap<String, ArrayList<PropertyData>>();
        int totalLivableAreaIdx = -1;
        int zipcodeIdx = -1;
        int marketValueIdx = -1;

        Double totalLivableArea = null;
        String zipcode = null;
        Double marketValue = null;

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
                        if (line.get(i).equals("total_livable_area")) {
                            totalLivableAreaIdx = i;
                        } else if (line.get(i).equals("zip_code")) {
                            zipcodeIdx = i;
                        } else if (line.get(i).equals("market_value")) {
                            marketValueIdx = i;
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
                    if (marketValueIdx!=-1 && line.get(marketValueIdx)!="") { marketValue = Double.parseDouble(line.get(marketValueIdx)); }
                    else { marketValue = null; }
                    if (totalLivableAreaIdx!=-1 && line.get(totalLivableAreaIdx)!="") { totalLivableArea = Double.parseDouble(line.get(totalLivableAreaIdx)); }
                    else { totalLivableArea = null; }
                    PropertyData record = new PropertyData(zipcode, marketValue, totalLivableArea);
                    ArrayList<PropertyData> list;
                    if (df.containsKey(zipcode)){
                        list = df.get(zipcode);
                    }
                    else{
                        list = new ArrayList<PropertyData>();
                    }
                    list.add(record);
                    df.put(zipcode, list);
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
