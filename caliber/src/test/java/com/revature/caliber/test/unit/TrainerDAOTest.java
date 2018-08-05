package com.revature.caliber.test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.caliber.CaliberTest;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.beans.TrainerRole;
import com.revature.caliber.data.TrainerRepository;

public class TrainerDAOTest extends CaliberTest {

	private static final Logger log = Logger.getLogger(TrainerDAOTest.class);
	private static final String PAT_MAIL = "patrick.walsh@revature.com";
	private static final List<String> TRAINER_TITLES = Arrays
			.asList(new String[] { "Senior Trainer", "Senior Technical Manager", "Lead Trainer",
					"Vice President of Technology", "Senior Java Developer", "Technology Manager", "Trainer" }); 
	@Autowired
	private TrainerRepository trainerRepository;

	@Test
	public void testFindAllTrainerTitles() {
		log.debug("Find all trainer titles");
		assertTrue(trainerRepository.findAllTrainerTitles().containsAll(TRAINER_TITLES));
	}

	@Test
	public void testFindByEmail() {
		log.debug("Find trainer by email");
		Trainer expected = new Trainer("Patrick Walsh", TRAINER_TITLES.get(2), PAT_MAIL, TrainerRole.ROLE_VP);
		assertEquals(expected, trainerRepository.findByEmail(PAT_MAIL));
	}

	@Test
	public void testFindAll() {
		log.debug("Find all trainers");
		assertEquals(27, trainerRepository.findAll().size());
	}

	@Test
	public void testSave() {
		log.debug("Save a new trainer");
		int before = trainerRepository.findAll().size();
		trainerRepository.save(new Trainer("Alex Cobian", TRAINER_TITLES.get(1), "cobian448@yahoo.com", TrainerRole.ROLE_VP));
		int after = trainerRepository.findAll().size();
		assertEquals(before, --after);
	}

	@Test
	public void testFindOne() {
		log.debug("Find trainer by id");
		Trainer expected = new Trainer("Patrick Walsh", TRAINER_TITLES.get(2), PAT_MAIL, TrainerRole.ROLE_VP);
		assertEquals(expected, trainerRepository.findOne(1));
	}

	@Test
	public void testUpdate() {
		log.debug("Update trainer");
		//Test for name update
		Trainer expected = trainerRepository.findByEmail(PAT_MAIL);
		expected.setName("Success Walsh");
		trainerRepository.save(expected);
		Trainer actual = trainerRepository.findByEmail(PAT_MAIL);
		assertEquals(expected, actual);
		//Test for email update
		String newEmail = "NewEmail@gmail.com";
		expected.setEmail(newEmail);
		trainerRepository.save(expected);
		Trainer actualByEmail = trainerRepository.findByEmail(newEmail);
		assertEquals(expected, actualByEmail);		
	}

}
