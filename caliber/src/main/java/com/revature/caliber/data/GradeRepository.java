package com.revature.caliber.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.revature.caliber.beans.Grade;

/**
 * Spring Data operations for the type {@link Grade}
 * 
 * @author Patrick Walsh
 *
 */
public interface GradeRepository extends JpaRepository<Grade, Long> {

	/**
	 * Saves a grade in the database.
	 * 
	 * @param grade
	 */
	@SuppressWarnings("unchecked")
	public Grade save(Grade grade);

	/**
	 * Returns absolutely all grades for only the most coarsely-grained reporting.
	 * Useful for feeding data into application for statistical analyses, such as
	 * regression analysis, calculating mean, and finding average ;)
	 * 
	 * @param traineeId
	 * @return grades
	 */
	@Query("select g from Grade g inner join g.assessment")
	public List<Grade> findAll();

	/**
	 * Returns grades for all trainees that took a particular assignment. Great for
	 * finding average/median/highest/lowest grades for a test
	 * 
	 * @param assessmentId
	 * @param score
	 * @return grades
	 */
	public List<Grade> findByAssessmentAssessmentIdAndScoreGreaterThan(Long assessmentId, Double score);

	/**
	 * Returns all grades for a trainee. Useful for generating a full-view of
	 * individual trainee performance.
	 * 
	 * @param traineeId
	 * @param score
	 * @return grades
	 */
	public List<Grade> findByTraineeTraineeIdAndScoreGreaterThan(Integer traineeId, Double score);

	/**
	 * Returns all grades for a batch. Useful for calculating coarsely-grained data
	 * for reporting.
	 * 
	 * @param batchId
	 * @param score
	 * @return grades
	 */
	public List<Grade> findByTraineeBatchBatchIdAndScoreGreaterThan(Integer batchId, Double score);

	/**
	 * Returns all grades for a category. Useful for improving performance time of
	 * company-wide reporting
	 * 
	 * @param batchId
	 * @param score
	 * @return grades
	 */
	public List<Grade> findByAssessmentCategoryCategoryIdAndScoreGreaterThan(Integer categoryId, Double score);

	/**
	 * Returns grades for all trainees in the batch on a given week. Used to load
	 * grade data onto the input spreadsheet, as well as tabular/chart reporting.
	 * 
	 * @param batchId
	 * @param week
	 * @param score
	 * @return grades
	 */
	public List<Grade> findByTraineeBatchBatchIdAndAssessmentWeekAndScoreGreaterThan(Integer batchId, Short week, Double score);

}
