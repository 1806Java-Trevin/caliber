package com.revature.caliber.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.TrainingStatus;

/**
 * Spring Data operations for the type {@link Trainee}
 * 
 * @author Patrick Walsh
 *
 */
@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Integer>{

	/**
	 * Save a trainee to the database
	 * 
	 * @param trainee
	 */
	@SuppressWarnings("unchecked")
	public Trainee save(Trainee trainee);
	
	/**
	 * Delete a trainee from the database
	 * 
	 * @param trainee
	 */
	public void delete(Integer traineeId);
	
	/**
	 * Find all trainees in a given batch
	 * 
	 * TODO find another way to load their grades
	 * 
	 * @param batchId
	 * @return
	 */
	public List<Trainee> findByBatchBatchIdAndTrainingStatusNot(Integer batchId, TrainingStatus status);
	
	/**
	 * Find all trainees in a given batch with a given training status 
	 * 
	 * @param batchId
	 * @return
	 */
	public List<Trainee> findByBatchBatchIdAndTrainingStatus(Integer batchId, TrainingStatus status);
	
	/**
	 * Find a trainee by the given identifier
	 * 
	 * @param traineeId
	 * @return
	 */
	public Trainee findOneByTraineeIdAndTrainingStatusNot(Integer traineeId, TrainingStatus status);
	
	/**
	 * Find a trainee by email address using LIKE operator
	 * 
	 * @param email
	 * @return
	 */
	public List<Trainee> findByEmailContaining(String email);
	
	/**
	 * Find a trainee by name
	 * 
	 * @param name
	 * @return
	 */
	public List<Trainee> findByNameContaining(String name);
	
	/**
	 * Find a trainee by Salesforce resourceId
	 * 
	 * @param resourceId
	 * @return
	 */
	public Trainee findByResourceId(String resourceId);
	
	/**
	 * Find a trainee by skypeId
	 * 
	 * @param skypeId
	 * @return
	 */
	public List<Trainee> findBySkypeIdContaining(String skypeId);
	
	///////////////////////////////////////////////////////////////////////////////////////////
	//	Possibly unused methods transferred over from Caliber 1
	///////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Find all trainees without condition. Useful for calculating report data
	 * 
	 * @return
	 */
	public List<Trainee> findByTrainingStatusNot(TrainingStatus status);
	
	/**
	 * Find all trainees without condition. Useful for calculating report data
	 * 
	 * @return
	 */
	public List<Trainee> findAll();
}
