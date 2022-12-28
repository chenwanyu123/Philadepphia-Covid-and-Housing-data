package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.PropertyData;

import java.util.ArrayList;
import java.util.HashMap;

public interface PropertyReader {
    public HashMap<String, ArrayList<PropertyData>> getAllInfo() throws Exception;
}
