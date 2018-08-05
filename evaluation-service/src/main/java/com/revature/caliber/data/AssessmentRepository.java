package com.revature.caliber.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.revature.caliber.beans.Assessment;

/**
 * Spring Data operations for the type {@link Assessment}
 * 
 * @author Emily Higgins
 *
 */
@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Integer> {

	/**
	 * Save an assessment to the database
	 *
	 * @param assessment
	 * @return updated assessment
	 */
	@SuppressWarnings("unchecked")
	public Assessment save(Assessment assessment);

	/**
	 * @return a list of all assessments
	 */
	public List<Assessment> findAll();

	/**
	 * Find assessment with the specified id
	 * 
	 * @param id
	 * @return assessment
	 */
	public Assessment findOne(int id);

	/**
	 * Find all assessments for a batch
	 * 
	 * @param batchId
	 * @return a list of all assessments for the specified batch
	 */
	public List<Assessment> findByBatch(Integer batchId);

	/**
	 * Find all assessments for a batch in a given week
	 * 
	 * @param batchId
	 * @param week
	 * @return a list of all assessments for the specified batch and week
	 */
	@Query(value="FROM Assessment a WHERE a.batch.batchId = ?1 AND a.week = ?2")
	public List<Assessment> findByBatchIdAndWeek(int batchId, short week);

	/**
	 * Remove the specified assessment from the database
	 * 
	 * @param assessment
	 */
	public void delete(Assessment assessment);

}
