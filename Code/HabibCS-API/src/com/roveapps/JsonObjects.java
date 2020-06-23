package com.roveapps;

import java.util.*;

import org.jsoup.nodes.Document;

import com.roveapps.hScheduler.Class;
import com.roveapps.hScheduler.Course;
import com.roveapps.hScheduler.HTMLParser;
import com.roveapps.hScheduler.NetworkingService;
import com.roveapps.hScheduler.Subroutines;
import com.roveapps.hScheduler.*;

public class JsonObjects {

	static class CourseObject {
		String name;
		String code;

		CourseObject(Course course) {
			name = course.name;
			code = course.code;
		}

	}

	public static class Major {
		String name;
		ArrayList<CourseObject> courses;

		Major(String name) {

			HTMLParser parser = new HTMLParser();

			ArrayList<JsonObjects.CourseObject> coursesObjects = new ArrayList<>();

			Document htmlCourses = NetworkingService.shared.getCourses(name);
			ArrayList<Course> courses = parser.htmlToCourses(htmlCourses);

			for (Course course : courses) {
				JsonObjects.CourseObject cO = new JsonObjects.CourseObject(course);
				coursesObjects.add(cO);

			}
			this.courses = coursesObjects;
			this.name = name;

		}
	}

	static class ClassDayObject {
		String code;
		String startTime;
		String endTime;
		String instructor;

		ClassDayObject(Class clas, String day) {
			code = clas.code;
			instructor = Subroutines.arrayToString(clas.instructor);

			Timing timing = clas.timings.get(day);

			
			startTime = Subroutines.dateFormatter.format(timing.startDate);
			endTime = Subroutines.dateFormatter.format(timing.endDate);
		}
	}

	public static class Schedule extends HashMap<String, ArrayList<JsonObjects.ClassDayObject>> {

		private static final long serialVersionUID = 1L;

		public Schedule() {
			super();
		}
	}

}
