package com.revature.caliber.test.unit;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.caliber.CaliberTest;
import com.revature.caliber.beans.Grade;
import com.revature.caliber.beans.Note;
import com.revature.caliber.beans.NoteType;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.data.NoteRepository;
import com.revature.caliber.data.TraineeRepository;

public class NoteDAOTest extends CaliberTest {

	private static final Logger log = Logger.getLogger(NoteDAOTest.class);

	private static final int TEST_TRAINEE_ID = 5529;
	private static final int TEST_BATCH_ID = 2100;
	private static final int TEST_QCTRAINEE_ID = 5532;
	private static final int TEST_QCBATCH_ID = 2201;

	@Autowired
	private NoteRepository noteRepository;
	@Autowired
	private TraineeRepository traineeRepository;

	/**
	 * Test methods: Positive tests for finding individual notes for trainee
	 * 
	 * @see com.revature.caliber.data.GradeDAO#save(Grade)
	 */
	@Test
	public void findQCIndividualNote() {
		log.trace("GETTING individual notes by Trainee");
		// get trainee comparison
		Trainee trainee = traineeRepository.findOne(TEST_TRAINEE_ID);

		// get notes
		List<Note> notes = noteRepository.findByTraineeTraineeIdAndWeekAndTypeOrderByWeekAsc(trainee.getTraineeId(),
				new Integer(2).shortValue(), NoteType.QC_TRAINEE);

		// compare with expected
		assertEquals(notes.size(), 1);
		assertEquals(notes.get(0).getContent(), "technically weak on SQL.");

	}

}
