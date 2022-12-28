package edu.upenn.cit594.datamanagement;

import java.util.HashMap;

public interface PopulationReader {
    public HashMap<String, Integer> getAllInfo() throws Exception;
}