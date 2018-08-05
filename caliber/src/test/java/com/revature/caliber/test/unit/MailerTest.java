package com.revature.caliber.test.unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.caliber.CaliberTest;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.data.TrainerRepository;
import com.revature.caliber.email.Mailer;

/**
 * Tests that the correct trainers are selected for emailing.
 * 
 * @author Will Underwood
 *
 */
public class MailerTest extends CaliberTest {

	private Mailer mailer;
	private Set<Trainer> trainersToSubmitGrades;
	@Autowired
	private TrainerRepository trainerRepository;
	private Trainer gray;
	private Trainer patrick;
	private Trainer dan;
	private Trainer orson;
	private Trainer shelby;
	private Trainer walter;
	private Trainer natalie;
	private Trainer archer;
	
	@Autowired
	public void setMailer(Mailer mailer) {
		this.mailer = mailer;
	}

	/**
	 * Finds trainers who need to submit grades
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.trainersToSubmitGrades = this.mailer.getTrainersWhoNeedToSubmitGrades();
		this.gray = this.trainerRepository.findByEmail("grawyne@gmail.com");
		this.patrick = this.trainerRepository.findByEmail("patrick.walsh@revature.com");
		this.dan = this.trainerRepository.findByEmail("pjw6193@hotmail.com");
		this.orson = this.trainerRepository.findByEmail("owallace@mailinator.com");
		this.shelby = this.trainerRepository.findByEmail("slevinson@mailinator.com");
		this.walter = this.trainerRepository.findByEmail("wpayne@mailinator.com");
		this.natalie = this.trainerRepository.findByEmail("nchurch@mailinator.com");
		this.archer = this.trainerRepository.findByEmail("aradcliff@mailinator.com");
	}

	/**
	 * Gray Wynne should be absent from the collection, because he submitted all
	 * his grades
	 */
	@Test
	public void testGrayAbsent() {
		assertFalse(this.trainersToSubmitGrades.contains(this.gray));
	}

	/**
	 * Patrick Walsh should be in the collection, because he has submitted only
	 * some grades
	 */
	@Test
	public void testPatrickPresent() {
		assertTrue(this.trainersToSubmitGrades.contains(this.patrick));
	}

	/**
	 * Dan Pickles should be in the collection, because he has not submitted any
	 * grades
	 */
	@Test
	public void testDanPresent() {
		assertTrue(this.trainersToSubmitGrades.contains(this.dan));
	}

	/**
	 * Orson Wallace should be in the collection, because he has submitted all
	 * but one grade
	 */
	@Test
	public void testOrsonPresent() {
		assertTrue(this.trainersToSubmitGrades.contains(this.orson));
	}

	/**
	 * Shelby Levinson should be in the collection, because she submitted only
	 * one grade
	 */
	@Test
	public void testShelbyPresent() {
		assertTrue(this.trainersToSubmitGrades.contains(this.shelby));
	}
	
	/**
	 * Walter Payne should be in the collection, because his current batch has no assessments
	 */
	@Test
	public void testWalterPresent() {
		assertTrue(this.trainersToSubmitGrades.contains(this.walter));
	}
	
	/**
	 * Natalie Church should be in the collection, because her past batch has no assessments
	 */
	@Test
	public void testNataliePresent() {
		assertTrue(this.trainersToSubmitGrades.contains(this.natalie));
	}
	
	/**
	 * Archer Radcliff should be absent from the collection, because even 
	 * though he is missing a grade, the trainee associated with 
	 * that grade was dropped and should be ignored.
	 */
	@Test
	public void testArcherAbsent() {
		assertFalse(this.trainersToSubmitGrades.contains(this.archer));
	}

}
