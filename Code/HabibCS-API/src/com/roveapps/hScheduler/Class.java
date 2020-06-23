package com.roveapps.hScheduler;


import java.util.Date;
import java.util.HashMap;

public class Class {

    public final String[] instructor;
    final String[] room;
    public HashMap<String, Timing> timings = new HashMap<>();
    public final String code;

    public Class(String[] instructor, String[] room, String[] timings, String code) {
        
        HTMLParser htmlParser = new HTMLParser();

    	this.instructor = instructor;
        this.room = room;
        this.timings = htmlParser.htmlToDateTime(timings);
        this.code = code;


    }

    public String[] getInstructor() {
        return instructor;
    }


    public String[] getRoom() {
        return room;
    }


    public HashMap<String, Timing> getTimings() {
        return timings;
    }


    public String toString() {
        return code + " " + timings;
    }


}







