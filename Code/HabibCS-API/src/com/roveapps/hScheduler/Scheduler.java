package com.roveapps.hScheduler;

import java.util.*;

public class Scheduler {

    ArrayList<Course> courses;
    public ArrayList<ArrayList<Class>> schedule = new ArrayList<>();

    public Scheduler(ArrayList<Course> courses){
        this.courses = courses;
    }



    public void createSchedule(HashMap<String, ArrayList<Timing>> timings,final int depth,ArrayList<Class> finalClasses){
      if (depth == courses.size()){
            schedule.add(finalClasses);
            return;}


    if (isItADeadEnd(timings,depth+1)){
        System.out.println("DEAD END");
        return;
    }
        Course course = courses.get(depth);


        for (Class classes: course.classes){

            if (checkClashWithSchedule(timings,classes.timings)){
                System.out.println("NOT POSSIBLE");
                continue;
            }


            //Adds it to the schedule
            HashMap<String, ArrayList<Timing>> newTimings = new HashMap<>(timings);
            for (String day : classes.timings.keySet()){
                if (newTimings.containsKey(day)){
                    newTimings.get(day).add(classes.timings.get(day));
                }else{
                    ArrayList<Timing> classTimingsnew = new ArrayList<>();
                    classTimingsnew.add(classes.timings.get(day));
                    newTimings.put(day,classTimingsnew);
                }
            }



            ArrayList<Class> newClasses = new ArrayList<>(finalClasses);
            newClasses.add(classes);

            createSchedule(newTimings,depth+1,newClasses);

        }

    }

    private Boolean isItADeadEnd(HashMap<String, ArrayList<Timing>> timings,int depth){
        if (timings.size() == 0){
            return false;
        }

        for (int x = depth;x<courses.size();x++){
            Course course = courses.get(x);
            if (checkIfPossible(course,timings)){
                continue;
            }
            return true;
        }
        return false;
    }



    //MARK: - Checks if there is a clash current class and schedule
    private  Boolean checkIfPossible(Course course, HashMap<String, ArrayList<Timing>> timings){
        for(Class c : course.classes){

            Boolean called = checkClashWithSchedule(timings,c.timings);


            if (!called){return true;}
        }
        return false;
    }


    private Boolean checkClashWithSchedule( HashMap<String, ArrayList<Timing>> timingA , HashMap<String,Timing> timingB){

        Set<String> classAKeys = new HashSet<>(timingA.keySet());
        Set<String> classBKeys = new HashSet<>(timingB.keySet());

        classAKeys.retainAll(classBKeys);

        for (String key: classAKeys){
            ArrayList<Timing>  classA = timingA.get(key);
            Timing  classB = timingB.get(key);
            for (Timing classTimesA : classA){
            if (checkIfTimingClash(classTimesA,classB)){return true;};

            }
        }
        return false;
    }


    //MARK: - Checks if there is a clash between two classes
   private Boolean checkIfTimingClash(Timing a, Timing b){

                long aStartTime = a.startDate.getTime();
                long bStartTime = b.startDate.getTime();

                long aEndTime = a.endDate.getTime();
                long bEndTime = b.endDate.getTime();

                if (bStartTime < aEndTime){
                    if (aStartTime<=bStartTime){
                        return true;
                    }
                }

                if (aStartTime < bEndTime){
                    if (bStartTime<=aStartTime){
                        return true;
                    }
                }



        return false;
    }

}
