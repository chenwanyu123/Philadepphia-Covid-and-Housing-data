package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.CovidData;

import java.util.ArrayList;
import java.util.HashMap;

public interface CovidReader {
    public HashMap<String, HashMap<String,CovidData>> getAllInfo() throws Exception;
}
