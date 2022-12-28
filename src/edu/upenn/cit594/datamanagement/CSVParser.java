package edu.upenn.cit594.datamanagement;

import java.lang.reflect.Array;
import java.util.ArrayList;

// use to parse each rows in the csv file
public class CSVParser {
    ArrayList<String> Parser(String input, boolean openQuota) {
        char[] chars = input.toCharArray();
        // openQuota: flag for catching double quotas and CR LF; new line should start with false
        // for saving the string
        String temp = null;
        // the last element in the result is for checking whether we should read next line to complete the data
        ArrayList<String> result = new ArrayList<String>();

        // index for iterating the chars
        int idx = 0;
        int length = chars.length;
        while (idx < length) {
            char chr = chars[idx];
            if (chr == '\"') {
                if (openQuota == false) {
                    openQuota = true;
                    temp = "";
                } else {
                    idx++;
                    if (idx == length) {
                        result.add(temp);
                        temp = null;
                        // end the line
                        openQuota = false;
                    } else {
                        chr = chars[idx];
                        if (chr==',') {
                            openQuota = false;
                            result.add(temp);
                            temp = null;
                        } else {
                            temp = temp + '"' + chr;
                        }
                    }
                }
            } else if (chr == '\n') {
                if (openQuota) {
                    temp = temp + chr;
                } else {
                    // end of line
                    result.add(temp);
                    temp = null;
                    break;
                }
            } else if (chr == '\r') {
                if (openQuota) {
                    temp = temp + chr;
                } else {
                    continue;
                }
            } else if (chr == ',') {
                if (openQuota) {
                    temp = temp + chr;
                } else {
                    if (temp == null) {
                        temp = "";
                    }
                    result.add(temp);
                    temp = "";
                }
            } else {
                if (temp==null) {
                    temp = "";
                }
                temp = temp + chr;
            }

            idx++;
        }
        if (temp != null) {
            result.add(temp);
        }
        // add the flag to check whether this line is completed for the data
        if (openQuota) {
            // T: needs to connected with next line
            result.add("T");
        } else {
            // F: the line is completed
            result.add("F");
        }
        return result;
    }

    ArrayList<String> ParserMultiLines(String row, ArrayList<String> lastLine) {
        boolean openQuota;
        if (lastLine == null) {
            openQuota = false;
        } else {
            openQuota = true;
        }

        ArrayList<String> data = Parser(row, openQuota);
        int CurLineLen = data.size();
        if (lastLine != null) {
            int lastLineLen = lastLine.size();
            // lastLineLen-1: is the flag of openQuota
            String str1 = lastLine.get(lastLineLen-2);
            ArrayList<String> x = new ArrayList<String>(lastLine.subList(0,lastLineLen-2));
            String str2 = data.get(0);
            ArrayList<String> y = new ArrayList<String>(data.subList(1,CurLineLen));
            String str = str1 + str2;
            x.add(str);
            // current openQuota result is saved in the last index
            x.addAll(y);
            return x;
        } else {
            return data;
        }
    }
}
