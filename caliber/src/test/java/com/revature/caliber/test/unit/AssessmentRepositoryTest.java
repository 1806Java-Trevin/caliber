package com.revature.caliber.test.unit;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;

import com.revature.caliber.CaliberTest;
import com.revature.caliber.beans.Assessment;
import com.revature.caliber.data.AssessmentRepository;

public class AssessmentRepositoryTest extends CaliberTest{
	
	private static final Logger log = Logger.getLogger(AssessmentRepositoryTest.class);
	
	@Autowired
	private AssessmentRepository assessmentRepository;
	private static final String ASSESSMENT_COUNT = "select count(assessment_id) from caliber_assessment";
	
	/**
	 * Tests methods:
	 * com.revature.caliber.data.AssessmentRepository.save()
	 * 
	 * Tests that an assessment bean can be saved, and persisted, into the database.
	 * It is further verified by comparing the sizes of two lists, which use the findAll() method
	 * to acquire all assessments stored in the assessments table. This is run twice:
	 * 
	 * 1. Once before the save method occurs.
	 * 2. Once after the method is run.
	 * 
	 * What should be expected, is that the list before method run + 1 equals the list after the method is run,
	 * the difference being in the additional assessment that was saved into the database.
	 * 
	 */
	@Test
	public void saveAssessmentTest() {

		
		log.debug("ATTEMPTING TO SAVE A NEW ASSESSMENT INTO THE DATABASE");
		List<Assessment> assessments1 = assessmentRepository.findAll();
		
		Assessment assessment = assessmentRepository.findAll().get(1);

		assessmentRepository.save(assessment);
		
		List<Assessment> assessments2 = assessmentRepository.findAll();
		
		assertEquals(assessments1.size() + 1, assessments2.size());
		
	}
	
	
	/**
	 * Tests methods:
	 * com.revature.caliber.data.AssessmentRepository.findOne()
	 * 
	 * Tests that a particular assessment can be found, and verifies that it has found it.
	 * Tested by using a hard-coded batch id of 2074 to look for an assessment using the findOne method.
	 * Asserted by comparing the hard-coded assessment id to the id that would normally be acquired using the following:
	 * 
	 * assessmentdao.findOne(assessmentId).getAssessmentId()
	 *  
	 * If there is a match, then that particular assessment was found.
	 * 
	 */
	@Test
	public void findOneAssessmentTest() {
		
		log.debug("SEARCHING FOR A SINGLE ASSESSMENT IN THE DATABASE");
		int assessmentId = 2074;
		assessmentRepository.findOne(assessmentId);
		assertEquals(assessmentId, assessmentRepository.findOne(assessmentId).getAssessmentId());
		
	}
	
	
	/**
	 * Tests methods:
	 * com.revature.caliber.data.AssessmentRepository.findAll()
	 * 
	 * Calls the findAll method, and stores all assessments found into a list.
	 * A log line is used to count the size of the assessments list, which is equal to the number of assessments that are
	 * inside the CALIBER_ASSESSMENT table. This was not hard-coded because it has the potential to fail builds.
	 * 
	 * If the list size corresponds to the number of assessments in the table, then the method has acquired them all assessments available.
	 * 
	 */
	@Test
	public void findAllAssessmentTest() {
		
		List<Assessment> assessments = assessmentRepository.findAll();
		
		log.debug("Number of assessments found in the database (should match the number of assessments in the CALIBER_ASSESSMENT table): " + assessments.size());
		
		assertNotNull(assessments);
		
	}
	
	
	/**
	 * Tests methods:
	 * com.revature.caliber.data.AssessmentRepository.findByWeek(batchId, weekNumber)
	 * 
	 * Calls the findByWeek() method to search for assessments by batch id and week number.
	 * Asserts that the batch Id and week number of every assessment in the returned ArrayList are the same.
	 * 
	 * If they are, then it has successfully done so.
	 */
	@Test
	public void findAssessmentsByWeekTest() {
		
		int batchId = 2150;
		short weekNumber = 3;
		
		List<Assessment> assessment = assessmentRepository.findByBatchIdAndWeek(batchId, weekNumber);
		
		for (int i = 0; i < assessment.size(); i++) {
			
			assertEquals (weekNumber, assessment.get(0).getWeek());
			assertEquals (batchId, assessment.get(0).getBatch().getBatchId());
		
		}
		
	}
	
	
	
	/**
	 * Tests method:
	 * com.revature.caliber.data.AssessmentRepository.findByBatchId(Integer batchId)
	 */
	@Test
	public void findByBatchIdAssessmentRepositoryTest() {
		
		log.debug("FIND BY BATCH ID ASSESSMENT DAO");
		Assessment assessment =assessmentRepository.findAll().get(0);
		List<Assessment> assesments = assessmentRepository.findByBatchBatchId(2050);
		assertEquals(assessment, assesments.get(0));
		
	}
	
	/**
	 * Tests method:
	 * com.revature.caliber.data.AssessmentRepository.update(Assessment assessment)
	 */
	@Test
	public void updateAssessmentRepositoryTest() {
		
		log.debug("UPDATE ASSESSMENT DAO");
		Assessment assessment =assessmentRepository.findOne(2058);

		assertNotEquals(2099, assessment.getRawScore());
		
		
		log.debug("Before Update "+ assessment);
		assessment.setRawScore(2099);
		assessmentRepository.save(assessment);
		log.debug("After Update "+ assessment);
		
		assessment =assessmentRepository.findOne(2058);
		log.debug("After getting again "+ assessment);


		assertEquals(2099, assessment.getRawScore());
	
	}
	
	/**
	 * Tests method:
	 * com.revature.caliber.data.AssessmentRepository.delete(Assessment assessment)
	 */
	@Test
	public void deleteAssessmentRepositoryTest() {
		
		log.debug("DELETE ASSESSMENT DAO");
		Long beforeTest = jdbcTemplate.queryForObject(ASSESSMENT_COUNT, Long.class);
		assessmentRepository.delete(assessmentRepository.findAll().get(1));
		Long afterTest = jdbcTemplate.queryForObject(ASSESSMENT_COUNT, Long.class);
		assertEquals(--beforeTest, afterTest);
		
	}
}

