package edu.upenn.cit594.processor;

import java.util.TreeMap;
import java.util.regex.Pattern;

public class ArgsParser {
    String[] args;
    TreeMap<String,String> result = null;
    public ArgsParser(String[] args) {
        this.args = args;
    }

    public TreeMap<String,String> run() throws Exception {
        // check whether the arguments are valid
        String pattern = "^--(?<name>.+?)=(?<value>.+)$";
        boolean matches;
        TreeMap<String,String> seen = new TreeMap<String,String>();
        // parsing args
        for (int idx=0; idx<args.length;idx++) {
            String arg = args[idx];
            matches =  Pattern.matches(pattern, arg);
            if (matches) {
                arg = arg.substring(2);
                int splitIdx = arg.indexOf("=");
                // arg name
                String arg1 = arg.substring(0,splitIdx);
                // arg value
                String arg2 = arg.substring(splitIdx+1);
                if (!arg1.equals("covid") && !arg1.equals("properties") && !arg1.equals("population")
                        && !arg1.equals("log")) {
                    throw new Exception("Unknown args");
                } else {
                    if (seen.containsKey(arg1)) {
                        throw new Exception("Duplicated args");
                    } else {
                        seen.put(arg1,arg2);
                    }
                }
            } else {
                throw new Exception("Args format is not correct");
            }
        }
        return seen;
    }
}
