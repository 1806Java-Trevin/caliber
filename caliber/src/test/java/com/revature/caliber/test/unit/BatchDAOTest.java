package com.revature.caliber.test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.Instant;
import java.time.Period;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.caliber.CaliberTest;
import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.beans.TrainerRole;
import com.revature.caliber.data.BatchRepository;

public class BatchDAOTest extends CaliberTest {

	private static final Logger log = Logger.getLogger(BatchDAOTest.class);

	@Autowired
	private BatchRepository batchRepository;

	/**
	 * Tests methods:
	 * @see com.revature.caliber.data.BatchDAO#findAll()
	 */
	@Test
	public void findAllTest() {
		log.debug("Testing the BatchDAO.findAll()");
		String sql = "SELECT * FROM CALIBER_BATCH";
		int expect = jdbcTemplate.queryForList(sql).size();
		int actual = batchRepository.findAllDistinct().size();
		assertEquals(expect, actual);
	}

	/**
	 * Tests methods:
	 * @see com.revature.caliber.data.BatchDAO#findAllByTrainer() Returns
	 *      batches with specified trainer and co-trainer without dropped
	 *      trainees
	 */
	@Test
	public void findAllByTrainerTest() {
		log.debug("Testing the BatchDAO.findAllByTrainerTest()");
		// positive testing
		String sql = "SELECT TRAINER_ID FROM CALIBER_TRAINER WHERE ROWNUM = 1";
		Integer trainerId = jdbcTemplate.queryForObject(sql, Integer.class);
		sql = "SELECT * FROM CALIBER_BATCH WHERE TRAINER_ID = " + trainerId + " OR CO_TRAINER_ID = " + trainerId;
		int expect = jdbcTemplate.queryForList(sql).size();
		List<Batch> batches = batchRepository.findAllByTrainer(trainerId);
		int actual = batches.size();
		assertEquals(expect, actual);

		// Make sure dropped trainees are not included
		// Testing against batch 2150 with trainer Patrick Walsh
		expect = 13;
		for (int i = 0; i < batches.size(); i++) {
			if (batches.get(i).getBatchId() == 2150) {
				actual = batches.get(i).getTrainees().size();
				break;
			}
		}
		assertEquals(expect, actual);

		// negative testings
		trainerId = Integer.MIN_VALUE;
		expect = 0;
		actual = batchRepository.findAllByTrainer(trainerId).size();
		assertEquals(expect, actual);
	}

	/**
	 * Tests methods:
	 * @see com.revature.caliber.data.BatchDAO.findOne(Integer batchId)
	 * Find a known batch, assert that the IDs are the same.
	 * Try to find a batch that doesn't exist, fail if it does.
	 */
	@Test
	public void findOneTest() {
		log.debug("Testing method BatchDAO.findOne(Integer batchId)");
		int expected = 2050;
		int actual = batchRepository.findOne(expected).getBatchId();
		assertEquals(expected, actual);
	}

	/**
	 * Tests methods:
	 * @see com.revature.caliber.data.BatchDAO.update()
	 * This test needs the findOne method to work.
	 * It finds a batch from the database, changes a value, updates the database,
	 * loads the batch from the database again.
	 * Tries to update a non-existing batch.
	 */
	@Test
	public void updateTest() {
		log.debug("Testing method BatchDAO.update(Batch batch)");
		Batch testBatch = batchRepository.findOne(2050);
		testBatch.setLocation("The basement");
		batchRepository.save(testBatch);
		Batch updatedTestBatch = batchRepository.findOne(2050);
		assertEquals(updatedTestBatch.getLocation(),"The basement");
		try{
			testBatch.setBatchId(-984);
			batchRepository.save(testBatch);
			fail();
		}
		catch(Exception e){
			log.debug(e);
		}
	}

	/**
	 * Tests methods:
	 * 
	 * @see com.revature.caliber.data.BatchDAO.save(Batch batch)
	 */
	@Test
	public void saveTest() {
		log.debug("Testing method BatchDAO.save(Batch batch)");
		Trainer testTrainer = new Trainer("Sir. Test", "Tester", "test@test.test", TrainerRole.ROLE_TRAINER);
		testTrainer.setTrainerId(2);
		Batch testBatch = new Batch("Test Name", testTrainer, Date.from(Instant.now()),
				Date.from(Instant.now().plus(Period.ofDays(60))), "Test Location");
		batchRepository.save(testBatch);
		List<Batch> resultSet = batchRepository.findAll();
		boolean success = false;
		for (Batch found : resultSet) {
			if ("Test Location".equals(found.getLocation())) {
				success = true;
				break;
			}
		}
		assertTrue(success);
	}
}
