package com.roveapps.hScheduler;

import java.text.SimpleDateFormat;

public class Subroutines {

    public static SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm aa");


    public static void formCopyPasteToDic(String text){
        String[] lines = text.split("\n");
        for (String line :lines){
            String[] mainSplit = line.split(":");
            String paramName =  mainSplit[0].trim();
            String paramVal = mainSplit[1].trim();
            System.out.println("postData.put("+"\""+paramName+"\""+","+"\""+paramVal+"\""+");");
        }
    }
    public static String arrayToString(String[] strings){
        String returns = "";

        for (String s:strings){
            returns += " "+s;
        }
        return returns;
    }
}


