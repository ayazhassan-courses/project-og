package com.roveapps.hScheduler;

import java.util.ArrayList;
import java.util.Comparator;

public class Course {
    ArrayList<Class> classes;
    public String name;
	public String code;

    public Course(ArrayList<Class> classes,String name,String code) {
        this.classes = classes;
        this.name = name;
        this.code = code;
    }

    @Override
    public String toString() {
        return "Course{" +
                "classes=" + classes +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    static class CustomComparator implements Comparator<Course> {
        @Override
        public int compare(Course o1, Course o2) {
            return o1.name.compareTo(o2.name);
        }
    }
}

