package com.revature.caliber.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.revature.caliber.beans.Batch;

/**
 * Spring Data operations for the type {@link Batch}
 * 
 * @author Emily Higgins
 */
@Repository
public interface BatchRepository extends JpaRepository<Batch, Integer> {

	public static final int MONTHS_BACK = -1;
	public static final String TRAINEES = "trainees";
	public static final String T_TRAINING_STATUS = "t.trainingStatus";
	public static final String START_DATE = "startDate";
	public static final String END_DATE = "endDate";

	/**
	 * Find a batch by its given identifier
	 * 
	 * @param batchId
	 * @return batch
	 */
	public Batch findOne(int batchId);

	/**
	 * Save a batch to the database.
	 * 
	 * @param batch
	 * @return updated batch
	 */
	@SuppressWarnings("unchecked")
	public Batch save(Batch batch);

	/**
	 * Looks for all batches, excluding dropped trainees, and orders by descending
	 * start date.
	 * 
	 * @return list of batches
	 */
	@Query("select distinct b from Batch b inner join b.trainees t order by b.startDate desc")
	public List<Batch> findAllDistinct();

	/**
	 * Looks for all batches where the user was the trainer or co-trainer.
	 * 
	 * @param auth
	 * @return
	 */
	@Query("select distinct b from Batch b inner join b.trainer t inner join b.coTrainer c where t.trainerId = :trainerId or c.trainerId = :trainerId")
	public List<Batch> findAllByTrainer(@Param("trainerId") Integer trainerId);

	/**
	 * Looks for all batches, excluding dropped trainees, and orders by descending
	 * start date.
	 * 
	 * Behavior is the same as {@link #findAllDistinct()} because the standard
	 * Spring Data method {@link #findAll()} will return many duplicate and unwanted
	 * batches.
	 * 
	 * @return list of batches
	 */
	@Override
	default List<Batch> findAll() {
		return findAllDistinct();
	}

}
