package com.revature.caliber.revpro.models;

import java.util.List;

/**
 * DTO used to import grades from RevaturePro quizzes. Format will look as
 * below:
 * 
 * { "quizzes": [ { "title" : "Core Java Week 1", "category": "Java", "grades" :
 * [ { "trainee": "salesforceResourceId", "grade": 75 }, { "trainee":
 * "jau1j18wjs9aj198", "grade": 87 } ] }, { "title" : "JUnit", "category":
 * "JUnit", "grades" : [ { "trainee": "salesforceResourceId", "grade": 81 }, {
 * "trainee": "jau1j18wjs9aj198", "grade": 65 } ] } ] }
 * 
 * @author Patrick Walsh
 *
 */
public class Quizzes {

	private List<Quiz> quizzes;

	public Quizzes() {
		super();
	}

	public List<Quiz> getQuizzes() {
		return quizzes;
	}

	public void setQuizzes(List<Quiz> quizzes) {
		this.quizzes = quizzes;
	}
	
}
