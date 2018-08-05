package com.revature.caliber.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Grade;
import com.revature.caliber.beans.Note;
import com.revature.caliber.beans.NoteType;
import com.revature.caliber.beans.QCStatus;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.TraineeFlag;
import com.revature.caliber.beans.TrainerRole;
import com.revature.caliber.data.GradeRepository;
import com.revature.caliber.data.NoteRepository;
import com.revature.caliber.data.TraineeRepository;

/**
 * Used to add grades for assessments and input notes Application logic has no
 * business being in a DAO nor in a Controller. This is the ideal place for
 * calculations
 * 
 * @author Patrick Walsh
 *
 */
@Service
public class EvaluationService {

	private static final Logger log = Logger.getLogger(EvaluationService.class);
	
	@Autowired
	private GradeRepository gradeRepository;

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private TraineeRepository traineeRepository;

	private static final String FINDING_WEEK = "Finding week ";
	private static final String WEEK_STATIC_FACTOR = "0";
	
	/**
	 * All grades queried should be greater than 0. 
	 */
	private static final double ZERO = 0.0;
	
	/*
	 *******************************************************
	 * GRADING SERVICES
	 *
	 *******************************************************
	 */
	
	/**
	 * Get all the grades.. ever
	 * @return
	 */
	public List<Grade> findAllGrades() {
		return gradeRepository.findAll();
	}

	/**
	 * SAVE GRADE
	 * 
	 * @param grade
	 */
	public void save(Grade grade) {
		log.debug("Saving grade: " + grade);
		gradeRepository.save(grade);
	}

	/**
	 * UPDATE GRADE
	 * 
	 * @param grade
	 */
	public void update(Grade grade) {
		log.debug("Updating grade: " + grade);
		gradeRepository.save(grade);
	}

	/**
	 * FIND GRADES BY WEEK
	 * 
	 * @param batchId
	 * @param week
	 * @return
	 */
	public Map<Integer, List<Grade>> findGradesByWeek(Integer batchId, Integer week) {
		log.debug(FINDING_WEEK + week + " grades for batch: " + batchId);
		List<Grade> grades = gradeRepository.findByTraineeBatchBatchIdAndAssessmentWeekAndScoreGreaterThan(batchId, week.shortValue(), ZERO);
		Map<Integer, List<Grade>> table = new HashMap<>();
		for (Grade grade : grades) {
			Integer key = grade.getTrainee().getTraineeId();
			if (table.containsKey(grade.getTrainee().getTraineeId())) {
				// eliminate nested records first
				grade.getAssessment().setBatch(null);
				// get the trainee's assessments and add the new assessment
				table.get(key).add(grade);
			} else {
				// eliminate nested records first
				grade.getAssessment().setBatch(null);
				// add the first assessment
				List<Grade> assessments = new ArrayList<>();
				assessments.add(grade);
				table.put(key, assessments);
			}
		}
		return table;
	}
	
	/**
	 * Find all grades for a given batch
	 * 
	 * @param batchId
	 * @return grades
	 */
	public List<Grade> findByBatch(int batchId) {
		return gradeRepository.findByTraineeBatchBatchIdAndScoreGreaterThan(batchId, ZERO); 
	}
	
	/**
	 * Find all grades for a particular trainee
	 * @param traineeId
	 * @return grades
	 */
	public List<Grade> findByTrainee(Integer traineeId) {
		return gradeRepository.findByTraineeTraineeIdAndScoreGreaterThan(traineeId, ZERO);
	}

	/*
	 *******************************************************
	 * NOTE SERVICES
	 *
	 *******************************************************
	 */

	/**
	 * Save a new note
	 * 
	 * @param note
	 */
	public Note save(Note note) {
		log.debug("Saving note: " + note);
		note = noteRepository.save(note);
		checkIfTraineeShouldBeFlagged(note);
		return note;
	}

	/**
	 * Update an existing note
	 * 
	 * @param note
	 */
	public void update(Note note) {
		log.debug("Updating note: " + note);
		noteRepository.save(note);
		checkIfTraineeShouldBeFlagged(note);
	}

	/**
	 * Trainees can be flagged automatically if they have: 2 Red or 2 consecutive
	 * yellow QCs
	 * 
	 * @param note
	 */
	public void checkIfTraineeShouldBeFlagged(Note note) {
		if (note != null && note.getType().equals(NoteType.QC_TRAINEE)) {
			// trainee should not be flagged on week 1 itself
			if (note.getWeek() < 2)
				return;

			// get a list of all notes in week ASC order
			List<Note> notes = noteRepository
					.findByTraineeTraineeIdAndTypeOrderByWeekAsc(note.getTrainee().getTraineeId(), NoteType.QC_TRAINEE);

			// loop over the notes to find if they have 2 or more consecutive yellow or red
			// QCs
			boolean flagged = false;
			// current week
			try {
				flagged = (notes.get(note.getWeek() - 1).getQcStatus().equals(QCStatus.Poor)
						|| notes.get(note.getWeek() - 1).getQcStatus().equals(QCStatus.Average))
						// previous week
						&& (notes.get(note.getWeek() - 2).getQcStatus().equals(QCStatus.Poor)
								|| notes.get(note.getWeek() - 2).getQcStatus().equals(QCStatus.Average)) ? true : false;
				if (flagged) {
					// save the flag status in database
					Trainee trainee = traineeRepository.findOne(note.getTrainee().getTraineeId());
					trainee.setFlagStatus(TraineeFlag.RED);
					// concat a generated flag message at the end of the current trainee notes
					if (trainee.getFlagNotes() != null
							&& !trainee.getFlagNotes().contains("Trainee was automatically flagged by Caliber"))
						trainee.setFlagNotes("Trainee was automatically flagged by Caliber. " + trainee.getFlagNotes());
					else
						trainee.setFlagNotes("Trainee was automatically flagged by Caliber. ");

					traineeRepository.save(trainee);
				} else {
					// remove the flag status in database
					Trainee trainee = traineeRepository.findOne(note.getTrainee().getTraineeId());
					trainee.setFlagStatus(TraineeFlag.NONE);
					traineeRepository.save(trainee);
				}
			} catch (Exception e) {
				log.debug("Failed to autoflag associate with note: " + note);
				log.debug(e);
			}
		}
	}

	/**
	 * Returns all batch-level notes for a given week. Only notes written by
	 * trainers are returned.
	 * 
	 * @param batch
	 * @param week
	 * @return notes
	 */
	public List<Note> findBatchNotes(Integer batchId, Integer week) {
		log.debug(FINDING_WEEK + week + " batch notes for batch: " + batchId);
		return noteRepository.findByBatchBatchIdAndWeekAndType(batchId, week.shortValue(), NoteType.BATCH);
	}

	/**
	 * Returns all individual notes for a given week. Only notes written by trainers
	 * are returned.
	 * 
	 * @param batch
	 * @param week
	 * @return notes
	 */
	public List<Note> findIndividualNotes(Integer batchId, Integer week) {
		log.debug(FINDING_WEEK + week + " individual notes for batch: " + batchId);
		return noteRepository.findByBatchBatchIdAndWeekAndType(batchId, week.shortValue(), NoteType.TRAINEE);
	}

	/**
	 * Returns Trainee note for the week
	 * 
	 * @param trainee
	 * @param week
	 * @return note
	 */
	public Note findTraineeNote(Integer traineeId, Integer week) {
		return noteRepository.findByTraineeTraineeIdAndWeekAndType(traineeId, week.shortValue(), NoteType.TRAINEE);
	}

	/**
	 * Returns QC Trainee note for the week
	 * 
	 * @param trainee
	 * @param week
	 * @return note
	 */
	public Note findQCTraineeNote(Integer traineeId, Integer week) {
		return noteRepository.findByTraineeTraineeIdAndWeekAndType(traineeId, week.shortValue(), NoteType.QC_TRAINEE);
	}

	/**
	 * Returns QC batch note for the batch for the week
	 * 
	 * @param batchId
	 * @param week
	 * @return note
	 */
	public Note findQCBatchNotes(Integer batchId, Integer week) {
		log.debug(FINDING_WEEK + week + " QC batch notes for batch: " + batchId);
		List<Note> notes = noteRepository.findByBatchBatchIdAndWeekAndType(batchId, week.shortValue(),
				NoteType.QC_BATCH);
		if (notes != null && !notes.isEmpty()) {
			return notes.get(0);
		}else {
			return new Note();
		}
	}

	/**
	 * Returns all individual notes written by QC for a given week.
	 * 
	 * @param traineeId
	 * @param week
	 * @return notes
	 */
	public List<Note> findQCIndividualNotes(Integer traineeId, Integer week) {
		log.debug(FINDING_WEEK + week + " QC individual notes for trainee: " + traineeId);
		return noteRepository.findByTraineeTraineeIdAndWeekAndTypeOrderByWeekAsc(traineeId, week.shortValue(),
				NoteType.QC_TRAINEE);
	}

	/**
	 * Returns all batch-level notes for a given week written by the trainer.
	 * 
	 * @param batchId
	 * @param week
	 * @return notes
	 */
	public List<Note> findAllBatchNotes(Integer batchId, Integer week) {
		log.debug(FINDING_WEEK + week + " batch notes for batch: " + batchId);
		return noteRepository.findByBatchBatchIdAndWeekAndTypeOrderByWeekAsc(batchId, week.shortValue(), NoteType.BATCH);
	}

	/**
	 * Find all QC trainee notes for a given batch and specific week.
	 * 
	 * @param batchId
	 * @param week
	 * @return notes
	 */
	public List<Note> findAllQCTraineeNotes(Integer batchId, Integer week) {
		log.debug("Find All QC Trainee Notes");
		return noteRepository
				.findByBatchBatchIdAndWeekAndTypeOrderByWeekAsc(batchId, week.shortValue(), NoteType.QC_TRAINEE);
	}

	/**
	 * Find all trainee notes for a given trainee.
	 * 
	 * @param traineeId
	 * @return
	 */
	public List<Note> findAllIndividualNotesOverall(Integer traineeId) {
		log.debug("Find Overall notes for trainee " + traineeId);
		return noteRepository.findByTraineeTraineeIdAndTypeOrderByWeekAsc(traineeId, NoteType.TRAINEE);
	}

	/**
	 * Find all qc trainee notes
	 * 
	 * @return
	 */
	public List<Note> findAllQCTraineeOverallNotes(Integer traineeId) {
		log.debug("Find All QC Trainee Notes for that trainee");
		return noteRepository.findByTraineeTraineeIdAndTypeOrderByWeekAsc(traineeId, NoteType.QC_TRAINEE);
	}

	/**
	 * Calculates the suggested batch overall QC note based on the average of all
	 * the individual trainees.
	 * 
	 * @param week
	 * @param batch
	 */
	public void calculateAverage(Integer week, Batch batch) {
		if (batch != null) {
			Note notes = noteRepository.findByBatchBatchIdAndWeekAndTypeOrderByWeekDesc(batch.getBatchId(), week.shortValue(),
					NoteType.QC_BATCH);
			Note overallNote = null;
			if (notes != null) {
				overallNote = notes;//.get(0);
			}
			if (overallNote == null) {
				log.debug("Creating a new overall Note for week " + week);
				overallNote = new Note();
				overallNote.setBatch(batch);
				overallNote.setWeek(week.shortValue());
				overallNote.setQcStatus(QCStatus.Undefined);
				overallNote.setType(NoteType.QC_BATCH);
				overallNote.setMaxVisibility(TrainerRole.ROLE_PANEL);
				overallNote.setQcFeedback(true);
				noteRepository.save(overallNote);
			}
			log.debug("Calculating Average of note of week");
			double average = 0.0f;
			List<Note> traineeNoteList = noteRepository
					.findByBatchBatchIdAndWeekAndTypeOrderByWeekAsc(batch.getBatchId(), week.shortValue(),
							NoteType.QC_TRAINEE);
			int denominator = traineeNoteList.size();
			for (Note note : traineeNoteList) {
				switch (note.getQcStatus()) {
				case Superstar:
					average += 4;
					break;
				case Good:
					average += 3;
					break;
				case Average:
					average += 2;
					break;
				case Poor:
					average += 1;
					break;
				default:
					denominator--;
				}
			}
			if (denominator != 0) {
				average /= denominator;
			} else {
				average = 0.0f;
			}
			if (average > 2.5) {
				overallNote.setQcStatus(QCStatus.Good);
			} else if (average >= 2 && average <= 2.5) {
				overallNote.setQcStatus(QCStatus.Average);
			} else if (average > 0 && average < 2) {
				overallNote.setQcStatus(QCStatus.Poor);
			} else {
				overallNote.setQcStatus(QCStatus.Undefined);
			}
			log.debug("The calculated average is: " + overallNote.getQcStatus());
			noteRepository.save(overallNote);
		} else {
			log.warn("The ASSIGN_NOTE_BATCH_ID Stored Procedure hasn't been ran yet to update Note's batch id.");
		}
	}

	/**
	 * Find all QC trainee notes for all weeks
	 * 
	 * @return
	 */
	public List<List<Note>> findAllQCTraineeNotesForAllWeeks(Integer batchId) {
		log.debug("Find All QC Trainee Notes");
		List<Note> notes = noteRepository.findByBatchBatchIdAndTypeOrderByWeekAsc(batchId, NoteType.QC_TRAINEE);
		ArrayList<List<Note>> noteFormatted2d = new ArrayList<>();
		notes = notes.stream().collect(
				Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(note -> {
					String weekAsString = Short.toString(note.getWeek());
					return note.getTrainee().getName()
							+ ((weekAsString.length() == 1) ? WEEK_STATIC_FACTOR + weekAsString : weekAsString)
							+ note.getTrainee().getTraineeId();
				}))), ArrayList::new));
		if (notes == null || notes.size() < 1) {
			return new ArrayList<>();
		}

		Trainee currentTrainee = null;
		List<Note> traineeNotes = new ArrayList<>();
		for (Note note : notes) {
			Trainee aTrainee = note.getTrainee();
			// trainee HAS changed
			if (!aTrainee.equals(currentTrainee) || currentTrainee == null) {
				// if this is not the first iteration
				if (currentTrainee != null) {
					noteFormatted2d.add(traineeNotes);
				}
				currentTrainee = aTrainee;
				traineeNotes = new ArrayList<>(Collections.singletonList(note));
			} else {// trainee HAS NOT changed
				traineeNotes.add(note);
			}
		}
		noteFormatted2d.add(traineeNotes);
		return formatJaggedArray(noteFormatted2d);
	}

	/**
	 * Formats the jagged "2d" ArrayList of notes. Inserts empty Notes into the
	 * missing spots so that the front end batch QC reports table will display
	 * correctly. Will be called by findAllQCTraineeNotesForAllWeeks() method
	 * 
	 * @return the Lists of Lists ("2d" ArrayList) formatted so that number of
	 *         columns and rows are even
	 */
	public List<List<Note>> formatJaggedArray(List<List<Note>> jaggedArray) {
		int maxWeeks = 0;
		for (List<Note> row : jaggedArray) {
			for (Note column : row) {
				short week = 0;
				week = column.getWeek();
				if (week > maxWeeks) {
					maxWeeks = week;
				}
			}
		}

		log.debug("Max weeks: " + maxWeeks);
		for (int row = 0; row < jaggedArray.size(); row++) {
			for (short column = 0; column < maxWeeks; column++) {
				if (jaggedArray.get(row).size() <= column || jaggedArray.get(row).get(column).getWeek() != column + 1) {
					Note emptyNote = new Note();
					emptyNote.setNoteId(row + column); // testing
					emptyNote.setWeek((short) (column + 1));
					jaggedArray.get(row).add(column, emptyNote);
				}
			}
		}
		log.debug("JaggedArray: " + jaggedArray);
		return jaggedArray;
	}

	/**
	 * Returns all QC notes for trainee in the batch for all weeks
	 * 
	 * @param batchId
	 * @return notes
	 */
	public List<Note> findAllQCBatchNotes(Integer batchId) {
		log.debug("Find All QC Batch Notes in ascending order by week");
		return noteRepository.findByBatchBatchIdAndTypeOrderByWeekAsc(batchId, NoteType.QC_BATCH);
	}

}
