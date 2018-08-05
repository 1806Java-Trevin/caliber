package com.revature.caliber.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.caliber.beans.Note;
import com.revature.caliber.beans.NoteType;

/**
 * Spring Data operations for the type {@link Note}
 * 
 * @author Patrick Walsh
 *
 */
@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {

	/**
	 * Save a new note
	 * 
	 * @param note
	 * @return saved note
	 */
	@SuppressWarnings("unchecked")
	public Note save(Note note);

	/**
	 * Returns batch-level note for a given week.
	 * 
	 * NoteType: BATCH are for batch-level notes written by the trainer. QC_BATCH
	 * are for batch-level written by a quality auditor.
	 * 
	 * TODO Refactor to only return ONE batch note. There only ever should be one
	 * batch note per week
	 * 
	 * @param batchId
	 * @param week
	 * @param noteType
	 * @return notes
	 */
	public List<Note> findByBatchAndWeekAndType(Integer batchId, Short week, NoteType type);
	
	// TODO fix me
	public Note findByBatchAndWeekAndTypeOrderByWeekDesc(Integer batchId, Short week, NoteType type);

	/**
	 * Returns all individual notes for a given week.
	 * 
	 * NoteType: TRAINEE are for individual notes written by the trainer. QC_TRAINEE
	 * are for individual notes written by a quality auditor.
	 * 
	 * @param traineeId
	 * @param week
	 * @param noteType
	 * @return notes
	 */
	public List<Note> findByTraineeAndWeekAndTypeOrderByWeekAsc(Integer traineeId, Short week, NoteType type);

	/**
	 * Returns all notes for a given trainee for all weeks.
	 * 
	 * NoteType: TRAINEE are for individual notes written by the trainer. QC_TRAINEE
	 * are for individual notes written by a quality auditor.
	 * 
	 * @param traineeId
	 * @param noteType
	 * @return notes
	 */
	public List<Note> findByTraineeAndTypeOrderByWeekAsc(Integer traineeId, NoteType type);

	/**
	 * Returns Trainee note for the week
	 * 
	 * NoteType: TRAINEE are for individual notes written by the trainer. QC_TRAINEE
	 * are for individual notes written by a quality auditor.
	 * 
	 * @param traineeId
	 * @param week
	 * @param type
	 * @return Note
	 */
	public Note findByTraineeAndWeekAndType(Integer traineeId, Short week, NoteType type);

	/**
	 * Returns all notes for trainees in the batch for a given week.
	 * 
	 * NoteType: TRAINEE are for individual notes written by the trainer. QC_TRAINEE
	 * are for individual notes written by a quality auditor.
	 * 
	 * @param BatchID
	 * @param week
	 * @param noteType
	 * @return notes
	 */
	public List<Note> findByBatchAndWeekAndTypeOrderByWeekAsc(Integer batchId, Short week, NoteType type);

	/**
	 * Returns all notes for all trainees in the batch for all weeks
	 * 
	 * @param batchId
	 * @param noteType
	 * @return notes
	 */
	public List<Note> findByBatchAndTypeOrderByWeekAsc(Integer batchId, NoteType type);
}
