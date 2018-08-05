package com.revature.caliber.test.unit;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.caliber.CaliberTest;
import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.TrainingStatus;
import com.revature.caliber.data.TraineeRepository;

public class TraineeDAOTest extends CaliberTest {

	private static final Logger log = Logger.getLogger(TraineeDAOTest.class);

	@Autowired
	private TraineeRepository traineeRepository;
	private static final String TRAINEE_COUNT = "select count(trainee_id) from caliber_trainee";

	/**
	 * 
	 * TraineeDAO.save(Trainee trainee)
	 * 	Create trainees:
	 * 		1 fully valid trainee
	 * 		1 trainee with invalid name (none)
	 * 		1 trainee with invalid email (none)
	 * 		1 trainee with invalid batch (none)
	 * 		run save method with each trainee
	 * 		test to see total number of trainers only increases by one since only one is fully valid
	 * 
	 * */
	@Test
	public void testSave() {
		log.debug("CREATE TRAINEE");
		Batch batch = new Batch();
		batch.setBatchId(2200);
		String name = "Danny McQuack";
		String email = "test@anotherDomain.com";
		Trainee trainee = new Trainee(name, null, email, batch);
		Long before = jdbcTemplate.queryForObject(TRAINEE_COUNT, Long.class);
		traineeRepository.save(trainee);

		try {
			Trainee nullName = new Trainee(null, null, email, batch);
			traineeRepository.save(nullName);

		} catch (ConstraintViolationException e) {
			log.debug(e);
		}
		try {
			Trainee nullEmail = new Trainee(name, null, null, batch);
			traineeRepository.save(nullEmail);
		} catch (ConstraintViolationException e) {
			log.debug(e);
		}
		try {
			Trainee nullBatch = new Trainee(name, null, email, null);
			traineeRepository.save(nullBatch);
		} catch (ConstraintViolationException e) {
			log.debug(e);
		}
		Long after = jdbcTemplate.queryForObject(TRAINEE_COUNT, Long.class);
		assertEquals(++before, after);
	}

	/**
	 * 
	 * TraineeDAO.findAll()
	 * 	Use JDBCtemplate to save actual size of Trainee table
	 * 	Use findAll() to save expected list of trainees and size of the trainee list
	 * 	compare actual to expected to make sure the same number of trainees are being returned
	 * 
	 * */
	@Test
	public void testFindAll() {
		log.debug("FIND ALL TRAINEES");
		Long sizeActual = jdbcTemplate.queryForObject(TRAINEE_COUNT, Long.class);
		List<Trainee> trainees = traineeRepository.findAll();
		Long sizeExpected = (long) trainees.size();
		assertEquals(sizeExpected, sizeActual);
	}
	
	/**
	 * 
	 * TraineeDAO.findAllNotDropped()
	 * 	Use JDBCtemplate to save the number of trainees without status 'Dropped'
	 * 	Use findAllNotDropped() to save expected list of trainees and size of the trainee list
	 * 	compare actual to expected to make sure the same number of trainees are being returned
	 * 
	 * */
	@Test
	public void testFindAllNotDropped() {
		log.debug("FIND ALL TRAINEES NOT DROPPED");
		Long sizeActual = jdbcTemplate.queryForObject(TRAINEE_COUNT + " WHERE training_status != 'Dropped'", Long.class);
		List<Trainee> trainees = traineeRepository.findByTrainingStatusNot(TrainingStatus.Dropped);
		Long sizeExpected = (long) trainees.size();
		assertEquals(sizeExpected, sizeActual);
	}

	/**
	 * 
	 * TraineeDAO.findAllByBatch()
	 * 	(note) method only returns trainees whose training_status value is not 'Dropped'
	 * 	1:
	 * 	Use JDBCtemplate to save actual list of Trainees found by known Batch of trainees
	 * 	Use findALlByBatch() to find expected list of Trainees found by same Batch
	 * 	compare size of actual to size of expected lists
	 * 	2:
	 * 	Use findAllByBatch() to find list of trainees from batch that DOES NOT EXIST
	 * 	Compare size of list to 0 since list should be empty
	 * 
	 * */
	@Test
	public void testFindAllByBatch() {
		log.debug("FIND ALL TRAINEES BY BATCH");
		Batch batch = new Batch();
		batch.setBatchId(2200);
		String traineeCountByBatch = TRAINEE_COUNT + " where batch_id = " + batch.getBatchId()
				+ " and training_status != 'Dropped'";
		List<Trainee> trainees = traineeRepository.findByBatchBatchIdAndTrainingStatus(batch.getBatchId(), TrainingStatus.Dropped);
		Long actualBatchSize = jdbcTemplate.queryForObject(traineeCountByBatch, Long.class);
		Long expectedBatchSize = (long) trainees.size();
		assertEquals(expectedBatchSize, actualBatchSize);
		int badBatchId = 5; // no batch with this Id exists
		List<Trainee> badBatchCall = traineeRepository.findByBatchBatchIdAndTrainingStatus(badBatchId, TrainingStatus.Dropped);
		int badBatchCallSize = badBatchCall.size();
		assertEquals(0, badBatchCallSize);
	}
	
	/**
	 * 
	 * TraineeDAO.findAllDroppedByBatch()
	 * 	(note) this method is intended to only return trainees whose training_status is 'Dropped'
	 * 	1:
	 * 	Use JDBCtemplate to find actual list of DROPPED trainees from known batch
	 * 	Use findAllDroppedByBatch() to find expected list of trainees from known batch
	 * 	compare the size of expected and actual to make sure they match
	 * 	2:
	 * 	Use findAllDroppedByBatch() to find list of trainees from batch that DOES NOT EXIST
	 * 	Compare size of list to 0 since list should be empty
	 * 
	 * */
	@Test
	public void testFindAllDroppedByBatch() {
		log.debug("FIND ALL TRAINEES DROPPED BY BATCH");

		Batch batch = new Batch();
		batch.setBatchId(2200);
		String traineeCountByBatch = TRAINEE_COUNT + " where batch_id = " + batch.getBatchId()
				+ " and training_status = 'Dropped'";
		Long actualBatchDroppedSize = jdbcTemplate.queryForObject(traineeCountByBatch, Long.class);
		List<Trainee> droppedTrainees = traineeRepository.findByBatchBatchIdAndTrainingStatus(batch.getBatchId(), TrainingStatus.Dropped);
		Long expectedBatchDroppedSize = (long) droppedTrainees.size();
		assertEquals(expectedBatchDroppedSize, actualBatchDroppedSize);

		int badBatchId = 5; // no batch with this Id exists
		List<Trainee> badBatchCall = traineeRepository.findByBatchBatchIdAndTrainingStatus(badBatchId, TrainingStatus.Dropped);
		int badBatchCallSize = badBatchCall.size();
		assertEquals(0, badBatchCallSize);
	}

	/**
	 * Validates the findOne function by finding a trainee by
	 * their unique ID
	 */
	@Test
	public void testFindOne() {
		log.debug("Find trainee by Id Test");
		String actual = "osher.y.cohen@gmail.com";
		assertEquals(actual, traineeRepository.findOne(5503).getEmail());
	}

	/**
	 * Validates the findByEmail function by finding a trainee by
	 * their unique email
	 */
	@Test
	public void testFindByEmail() {
		log.debug("Find trainee by email Test");
		Integer id = 5503;
		assertEquals((int) id, (int) traineeRepository.findByEmailContaining("osher").get(0).getTraineeId());
	}
	
	@Test
	public void testFindByName() {
		log.debug("Find trainee by name Test");
		Integer id = 5511;
		assertEquals((int) id, (int) traineeRepository.findByNameContaining("Lau").get(0).getTraineeId());
	}
	
	@Test
	public void testFindBySkypeId() {
		log.debug("Find trainee by SkypeId Test");
		Integer id = 5504;
		assertEquals((int) id, (int) traineeRepository.findBySkypeIdContaining("kyle.chang").get(0).getTraineeId());
	}

	/**
	 * Validates the update function by updating the trainee's name
	 * and checking against the original in the database
	 */
	@Test
	public void testUpdate() {
		log.debug("Update trainee");
		String updatedName = "Up, Dated";
		Trainee trainee = traineeRepository.findOne(5503);
		trainee.setName(updatedName);
		traineeRepository.save(trainee);
		assertEquals(updatedName, trainee.getName());
	}

	/**
	 * Validates the delete function by comparing the number of rows
	 * in the original database to the number of rows in the altered
	 * database after the delete (there should be one fewer).
	 */
	@Test
	public void testDelete() {
		log.debug("Delete trainee");
		int initialSize = traineeRepository.findAll().size();
		traineeRepository.delete(5503);
		int newSize = traineeRepository.findAll().size();
		assertEquals(--initialSize, newSize);
	}
}
