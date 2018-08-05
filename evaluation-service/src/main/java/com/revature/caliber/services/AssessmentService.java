package com.revature.caliber.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.caliber.beans.Assessment;
import com.revature.caliber.data.AssessmentRepository;

/**
 * Used for assessment CRUD operations. Includes both Trainer and QC
 * assessments. Application logic has no business being in a DAO nor in a
 * Controller. This is the ideal place for calculations
 * 
 * @author Patrick Walsh
 * @author Emily Higgins
 *
 */
@Service
public class AssessmentService {

	private static final Logger log = Logger.getLogger(AssessmentService.class);
	
	@Autowired
	private AssessmentRepository assessmentRepository;

	/*
	 *******************************************************
	 *	ASSESSMENT SERVICES
	 *
	 *******************************************************
	 */
	
	/**
	 * SAVE ASSESSMENT
	 * @param assessment
	 */
	public void save(Assessment assessment) {
		log.debug("Saving assessment " + assessment);
		assessmentRepository.save(assessment);
	}

	/**
	 * FIND ASSESSMENT BY ID
	 * @param assessmentId
	 * @return
	 */
	public Assessment findAssessment(int assessmentId) {
		log.debug("Finding one assessment " + assessmentId);
		return assessmentRepository.findOne(assessmentId);
	}

	/**
	 * FIND ALL ASSESSMENTS
	 * @return
	 */
	public List<Assessment> findAllAssessments() {
		log.debug("Find all assessments");
		return assessmentRepository.findAll();
	}

	/**
	 * FIND ASSESSMENT BY BATCH AND WEEK
	 * @param batchId
	 * @param week
	 * @return all assessments for the specified batch and week
	 */
	public List<Assessment> findAssessmentByWeek(Integer batchId, Integer week) {
		log.debug("Find assessment by week number " + week + " for batch " + batchId + " ");
		return assessmentRepository.findByBatchIdAndWeek(batchId, week.shortValue());
	}
	
	/**
	 * FIND ASSESSMENT BY BATCHID
	 * @param batchId
	 * @return all assessments for the specified batch
	 */
	public List<Assessment> findAssessmentByBatchId(Integer batchId) {
		log.debug("Find assessment by batchId " + batchId + " ");
		return assessmentRepository.findByBatchBatchId(batchId);
	}

	/**
	 * UPDATE ASSESSMENT
	 * @param assessment
	 * @return updated assessment
	 */
	public Assessment update(Assessment assessment) {
		log.debug("Updating assessment " + assessment);
		assessmentRepository.save(assessment);
		return assessment;
	}

	/**
	 * DELETE ASSESSMENT
	 * @param assessment
	 */
	public void delete(Assessment assessment) {
		log.debug("Deleting assessment " + assessment);
		assessmentRepository.delete(assessment);
	}

}
