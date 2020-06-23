package com.roveapps.hScheduler;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class HTMLParser {


    public ArrayList<Course> htmlToCourses(Document doc){
        ArrayList<Course> courses = new ArrayList<>();
        Elements att = doc.select("table[width=679]");

        int spanCounter = 0;
        for (int x = 0;x<att.size();x++){

        Element element = att.get(x);
        String nameAndCodeString = element.select("a[name=SSR_CLSRSLT_WRK_GROUPBOX2$"+x+"]").first().attr("title");
        nameAndCodeString = nameAndCodeString.replace("Collapse section","").trim();
        String[] nameAndCode = nameAndCodeString.split("-");
        String code = nameAndCode[0].trim();
        String name = nameAndCode[1].trim();



        Elements classes = element.select("table[class=PSLEVEL1GRIDNBONBO]");
        ArrayList<Class> myclasses = new ArrayList<>();

        for (int y= 0;y<classes.size();y++){
            Class myc = toClass(classes.get(y),spanCounter);
            spanCounter += 1;
            myclasses.add(myc);

        }
        Course course = new Course(myclasses,name,code);
            courses.add(course);
        }

        return courses;
    }

    private Class toClass(Element e, int num){

        //Instructor
        Element instuctorName = e.select("span[id=MTG_INSTR$"+num+"]").first();
        String instructorsString = instuctorName.toString().replace("<span class=\"PSLONGEDITBOX\" id=\"MTG_INSTR$"+num+"\">", "").replace("</span>", "");
        String[] instructors = instructorsString.split("<br>");

        //Date and timings
        Element timing = e.select("span[id=MTG_DAYTIME$"+num+"]").first();
        String timingsString = timing.toString().replace("<span class=\"PSLONGEDITBOX\" id=\"MTG_DAYTIME$"+num+"\">", "").replace("</span>", "");
        String[] timings = timingsString.split("<br>");

        //Room
        Element room = e.select("span[id=MTG_ROOM$"+num+"]").first();
        String roomString = room.toString().replace("<span class=\"PSLONGEDITBOX\" id=\"MTG_ROOM$"+num+"\">", "").replace("</span>", "");
        String[] rooms = roomString.split("<br>");

        //Class Code
        Element codes = e.select("span[id=MTG_CLASS_NBR$span$"+num+"]").first();
        String codeString = codes.text();

        return new Class(instructors, rooms, timings,codeString);






}



    public HashMap<String, Timing> htmlToDateTime(String[] dateTimes) {
         HashMap<String, Timing> timings = new HashMap<>();
        for (String dateTime : dateTimes) {

            if (dateTime.contains("TBA")) {
                return timings;
            }
            dateTime = dateTime.trim();

            String[] timingSplit = dateTime.split("-");

            String startTime = timingSplit[0].split(" ")[1].trim();
            String endTime = timingSplit[1].trim();
            String days = timingSplit[0].split(" ")[0].trim();


            Timing timing = new Timing(timeIntoDate(startTime), timeIntoDate(endTime));


            for (int x = 0; x < days.length(); x = x + 2) {
                String day1 = days.substring(x, x + 2);
                timings.put(day1, timing);
            }


        }

        return timings;
    }


    private Date timeIntoDate(String timeString) {
        timeString = timeString.trim();
        timeString = timeString.substring(0, timeString.length() - 2) + " " + timeString.substring(timeString.length() - 2);
        try {

            SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
            return df.parse(timeString.trim());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }



}

