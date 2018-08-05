package com.revature.caliber.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * The type Grade.
 */
public class Grade implements Serializable {
	
	private static final long serialVersionUID = -2031135710502844800L;

	private long gradeId;

	/**
	 * Assessment - The specified assessment taken by the Trainee
	 */
	private Assessment assessment;

	/**
	 * Trainee- the trainee that receives this Grade
	 */
	private Trainee trainee;

	/**
	 * dateReceived- date this Grade was earned
	 */
	private Date dateReceived;

	/**
	 * score - points earned. should be based on raw score of Assessment.
	 * Example: Assessment is worth 200 points, and Trainee made a 75% thus
	 * score is 150
	 */
	private double score;

	public Grade() {
		super();
	}

	public Grade(Assessment assessment, Trainee trainee, Date dateReceived, double score) {
		super();
		this.assessment = assessment;
		this.trainee = trainee;
		this.dateReceived = dateReceived;
		this.score = score;
	}

	public long getGradeId() {
		return gradeId;
	}

	public void setGradeId(long gradeId) {
		this.gradeId = gradeId;
	}

	public Assessment getAssessment() {
		return assessment;
	}

	public void setAssessment(Assessment assessment) {
		this.assessment = assessment;
	}

	public Trainee getTrainee() {
		return trainee;
	}

	public void setTrainee(Trainee trainee) {
		this.trainee = trainee;
	}

	public Date getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assessment == null) ? 0 : assessment.hashCode());
		result = prime * result + ((dateReceived == null) ? 0 : dateReceived.hashCode());
		long temp;
		temp = Double.doubleToLongBits(score);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((trainee == null) ? 0 : trainee.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Grade other = (Grade) obj;
		if (assessment == null) {
			if (other.assessment != null)
				return false;
		} else if (!assessment.equals(other.assessment))
			return false;
		if (dateReceived == null) {
			if (other.dateReceived != null)
				return false;
		} else if (!dateReceived.equals(other.dateReceived))
			return false;
		if (Double.doubleToLongBits(score) != Double.doubleToLongBits(other.score))
			return false;
		if (trainee == null) {
			if (other.trainee != null)
				return false;
		} else if (!trainee.equals(other.trainee))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Grade [gradeId=" + gradeId + ", assessment=" + assessment + ", trainee=" + trainee + ", dateReceived="
				+ dateReceived + ", score=" + score + "]";
	}
}