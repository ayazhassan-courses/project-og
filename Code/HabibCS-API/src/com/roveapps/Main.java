package com.roveapps;

import org.jsoup.nodes.Document;

import java.util.*;
import com.google.gson.Gson;
import com.roveapps.JsonObjects;
import com.roveapps.JsonObjects.ClassDayObject;
import com.roveapps.JsonObjects.Major;
import com.roveapps.JsonObjects.Schedule;
import com.roveapps.hScheduler.*;
import com.roveapps.hScheduler.Class;

public class Main {

	String[] majors = { "CS", "EE", "EE/CS", "MATH", "CORE" };
	HTMLParser parser = new HTMLParser();
	Gson gson = new Gson();

	public Main() {
		NetworkingService.shared.setCredentials(PrivateConstants.username, PrivateConstants.password);
	}

	public String getCourses() {
		ArrayList<JsonObjects.Major> courses = new ArrayList<>();

		for (String major : majors) {
			JsonObjects.Major myMajor = new JsonObjects.Major(major);
			courses.add(myMajor);

		}
		return gson.toJson(courses);
	}

	public String getSchedule(ArrayList<String> myCourseNames) {

		ArrayList<Course> courses = new ArrayList<>();

		for (String major : majors) {
			Document htmlCourses = NetworkingService.shared.getCourses(major);
			courses.addAll(parser.htmlToCourses(htmlCourses));
		}

		ArrayList<Course> myCourses = new ArrayList<>();
		for (Course course : courses) {
			if (myCourseNames.contains(course.code)) {
				myCourses.add(course);
			}
		}

		ArrayList<JsonObjects.Schedule> finalSchedules = new ArrayList<Schedule>();

		Scheduler scheduler = new Scheduler(myCourses);
		scheduler.createSchedule(new HashMap<>(), 0, new ArrayList<Class>());
		ArrayList<ArrayList<Class>> schs = scheduler.schedule;

		for (ArrayList<Class> sch : schs) {
			JsonObjects.Schedule timeTable = toTimeTable(sch);
			finalSchedules.add(timeTable);
		}

		return gson.toJson(finalSchedules);

	}

	// Helper function to display schedule
	private static JsonObjects.Schedule toTimeTable(ArrayList<Class> classes) {
		JsonObjects.Schedule timetable = new JsonObjects.Schedule();

		for (Class clas : classes) {

			for (String key : clas.timings.keySet()) {

				JsonObjects.ClassDayObject classObject = new JsonObjects.ClassDayObject(clas, key);

				if (timetable.get(key) != null) {
					timetable.get(key).add(classObject);
				} else {
					ArrayList<JsonObjects.ClassDayObject> codes = new ArrayList<>();
					codes.add(classObject);
					timetable.put(key, codes);
				}
			}
		}
		return timetable;
	}

}