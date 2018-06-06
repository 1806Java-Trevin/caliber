package com.revature.caliber.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.revature.caliber.beans.Address;
import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.beans.TrainerRole;
import com.revature.caliber.beans.TrainerTask;
import com.revature.caliber.beans.TrainerTaskCompletion;
import com.revature.caliber.beans.TrainingStatus;
import com.revature.caliber.data.AddressRepository;
import com.revature.caliber.data.BatchDAO;
import com.revature.caliber.data.TaskCompletionDAO;
import com.revature.caliber.data.TaskDAO;
import com.revature.caliber.data.TraineeRepository;
import com.revature.caliber.data.TrainerRepository;

/**
 * Provides logic concerning trainer and trainee data. Application logic has no
 * business being in a DAO nor in a Controller. This is the ideal place for
 * calculations
 *
 * @author Patrick Walsh
 * @author Emily Higgins
 *
 */
@Service
public class TrainingService {

	private static final Logger log = Logger.getLogger(TrainingService.class);
	
	@Autowired
	private TrainerRepository trainerRepository;
	
	@Autowired
	private TraineeRepository traineeRepository;
	
	private BatchDAO batchDAO;
	@Autowired
	private AddressRepository addressRepository;
	private TaskDAO taskDAO;
	private TaskCompletionDAO taskCompletionDAO;

	@Autowired
	public void setBatchDAO(BatchDAO batchDAO) {
		this.batchDAO = batchDAO;
	}
	
	@Autowired
	public void setTaskDAO(TaskDAO taskDAO) {
		this.taskDAO = taskDAO;
	}
	
	@Autowired
	public void setTaskCompletionDao(TaskCompletionDAO taskCompletionDAO) {
		this.taskCompletionDAO = taskCompletionDAO;
	}

	/*
	 *******************************************************
	 * LOCATION SERVICES
	 *
	 *******************************************************
	 */

	/**
	 * Add new Address
	 * @param location
	 */
	public void createLocation(Address location) {
		log.debug("Creating Location " + location);
		addressRepository.save(location);
	}

	/**
	 * Update existing Address
	 * @param location
	 */
	public void update(Address location) {
		log.debug("Update location: " + location);
		addressRepository.save(location);
	}

	/**
	 * retrieve all locations
	 * @return all Addresses in the database
	 */
	public List<Address> findAllLocations() {
		log.debug("Finding all locations");
		return addressRepository.findAll();
	}
	
	/**
	 * Find Address with provided id
	 * @param id
	 * @return address
	 */
	public Address findById(int id) {
		log.debug("Getting Address with ID " + id);
		Address address = addressRepository.findOne(id);
		log.debug("Got " + address);
		return address;
	}

	/*
	 *******************************************************
	 * TRAINER SERVICES
	 *
	 *******************************************************
	 */

	/**
	 * Add New Trainer
	 *
	 * @param trainer
	 */
	public Trainer createTrainer(Trainer trainer) {
		log.debug("Creating Trainer " + trainer);
		return trainerRepository.save(trainer);
	}

	/**
	 * FIND TRAINER BY EMAIL
	 *
	 * @param email
	 * @return
	 */
	public Trainer findTrainer(String email) {
		log.debug("Find trainer by email " + email);
		return trainerRepository.findByEmail(email);
	}
	
	

	/**
	 * FIND ALL TRAINERS (not inactive trainers)
	 *
	 * @return
	 */
	public List<Trainer> findAllTrainers() {
		log.debug("Finding all trainers");
		return trainerRepository.findAllByTierNot(TrainerRole.ROLE_INACTIVE);
	}

	/**
	 * UPDATE TRAINER
	 *
	 * @param trainer
	 */
	public Trainer update(Trainer trainer) {
		log.debug("Update trainer: " + trainer);
		return trainerRepository.save(trainer);
	}

	/**
	 * FIND TRAINER BY ID
	 *
	 * @param trainerId
	 * @return
	 */
	public Trainer findTrainer(Integer trainerId) {
		log.debug("Find trainer by id: " + trainerId);
		return trainerRepository.findOne(trainerId);
	}

	/**
	 *
	 * MAKE TRAINER INACTIVE
	 *
	 * @param trainer
	 **/
	public void makeInactive(Trainer trainer) {
		log.debug(trainer + " is now inactive");
		trainer.setTier(TrainerRole.ROLE_INACTIVE);
		trainerRepository.save(trainer);
	}

	/**
	 * Find all distinct titles that have been given to trainers
	 **/
	public List<String> findAllTrainerTitles() {
		log.debug("Found all trainer titles");
		return trainerRepository.findAllTrainerTitles();
	}

	/*
	 *******************************************************
	 * BATCH SERVICES
	 *
	 *******************************************************
	 */

	/**
	 * ADD ANOTHER WEEK TO BATCH
	 *
	 * @param batchId
	 */
	public void addWeek(Integer batchId) {
		log.debug("Adding week to batch: " + batchId);
		Batch batch = batchDAO.findOne(batchId);
		if (batch == null)
			throw new IllegalArgumentException("Invalid batch");
		int weeks = batch.getWeeks();
		batch.setWeeks(++weeks);
		batchDAO.update(batch);
	}

	/**
	 * SAVE BATCH
	 *
	 * @param batch
	 */
	public void save(Batch batch) {
		log.debug("Saving batch: " + batch);
		batchDAO.save(batch);
	}

	/**
	 * FIND ALL BATCHES
	 *
	 * @return
	 */
	public List<Batch> findAllBatches() {
		log.debug("Find all batches");
		return batchDAO.findAll();
	}

	/**
	 * FIND ALL CURRENT BATCHES
	 *
	 * @return
	 */
	public List<Batch> findAllCurrentBatches() {
		log.debug("Find all current batches");
		return batchDAO.findAllCurrent();
	}

	/**
	 * FIND ALL BATCHES BY TRAINER
	 *
	 * @param trainerId
	 * @return
	 */
	public List<Batch> findAllBatches(int trainerId) {
		log.debug("Find all batches for trainer: " + trainerId);
		return batchDAO.findAllByTrainer(trainerId);
	}

	/**
	 * FIND ALL CURRENT BATCHES BY TRAINER
	 *
	 * @param trainerId
	 * @return
	 */
	public List<Batch> findAllCurrentBatches(int trainerId) {
		log.debug("Find all current batches for trainer: " + trainerId);
		return batchDAO.findAllCurrent(trainerId);
	}

	/**
	 * FIND BATCH BY ID
	 *
	 * @param batchId
	 * @return
	 */
	public Batch findBatch(Integer batchId) {
		log.debug("Finding batch with id: " + batchId);
		return batchDAO.findOne(batchId);
	}

	/**
	 * UPDATE BATCH
	 *
	 * @param batch
	 */
	public void update(Batch batch) {
		log.debug("Update batch " + batch);
		batchDAO.update(batch);
	}

	/**
	 * DELETE BATCH
	 *
	 * @param batch
	 */
	public void delete(Batch batch) {
		Batch fullBatch = batchDAO.findOneWithDroppedTrainees(batch.getBatchId());
		log.debug("Delete batch " + fullBatch);
		batchDAO.delete(fullBatch);
	}

	/*
	 *******************************************************
	 * TRAINEE SERVICES
	 *
	 *******************************************************
	 */
	/**
	 * SAVE TRAINEE
	 *
	 * @param trainee
	 */
	public void save(Trainee trainee) {
		log.debug("Save trainee: " + trainee);
		traineeRepository.save(trainee);
	}

	/**
	 * FIND ALL TRAINEES
	 *
	 * @return
	 */
	public List<Trainee> findAllTrainees() {
		log.debug("Find all trainees");
		return traineeRepository.findAll();
	}

	/**
	 * FIND ALL TRAINEES BY BATCH ID
	 *
	 * @param batchId
	 * @return
	 */
	public List<Trainee> findAllTraineesByBatch(Integer batchId) {
		log.debug("Find trainees by batch");
		return traineeRepository.findByBatchBatchIdAndTrainingStatusNot(batchId, TrainingStatus.Dropped);
	}

	/**
	 * FIND ALL DROPPED TRAINEES BY BATCH ID
	 *
	 * @param batchId
	 * @return
	 */
	public List<Trainee> findAllDroppedTraineesByBatch(Integer batchId) {
		log.debug("Find dropped trainees by batch");
		return traineeRepository.findByBatchBatchIdAndTrainingStatus(batchId, TrainingStatus.Dropped);
	}

	/**
	 * FIND TRAINEE BY ID
	 *
	 * @param traineeId
	 * @return
	 */
	public Trainee findTrainee(Integer traineeId) {
		log.debug("Find trainee by id: " + traineeId);
		return traineeRepository.findOneByTraineeIdAndTrainingStatusNot(traineeId, TrainingStatus.Dropped);
	}

	/**
	 * FIND TRAINEE BY EMAIL ADDRESS
	 *
	 * @param email
	 * @return
	 */
	public Set<Trainee> search(String searchTerm) {
		log.debug("Find trainee : " + searchTerm);
		Set<Trainee> result = new HashSet<>();
		List<Trainee> traineeByEmail = traineeRepository.findByEmailContaining(searchTerm);
		result.addAll(traineeByEmail);
		List<Trainee> traineeByName = traineeRepository.findByNameContaining(searchTerm);
		result.addAll(traineeByName);
		List<Trainee> traineeBySkypeId = traineeRepository.findBySkypeIdContaining(searchTerm);
		result.addAll(traineeBySkypeId);
		return result;
	}
	

	/**
	 * DELETE TRAINEE
	 *
	 * @param trainee
	 */
	public void delete(Integer traineeId) {
		log.debug("Delete trainee " + traineeId);
		traineeRepository.delete(traineeId);
	}

	/**
	 * UPDATE TRAINEE
	 *
	 * @param trainee
	 */
	public Trainee update(Trainee trainee) {
		log.debug("Update trainee " + trainee);
		return traineeRepository.save(trainee);
	}
	
	/*
	 *******************************************************
	 * TASK SERVICES
	 *
	 *******************************************************
	 */

	/**
	 * FIND ALL ACTIVE TASKS
	 */
	public List<TrainerTask> findAllActiveTasks() {
		log.debug("Find all active tasks");
		return taskDAO.findAllActiveTasks();
	}
	
	/**
	 * FIND ALL COMPLETED TASKS
	 */
	public List<TrainerTaskCompletion> findAllCompletedTasks() {
		log.debug("Find all completed tasks");
		return taskCompletionDAO.findAllCompletedTasks();
	}
	
	/**
	 * FIND ALL COMPLETED TASKS BY TRAINER ID
	 */
	public List<TrainerTaskCompletion> findAllTasksByTrainerId(int id) {
		log.debug("Find all completed tasks for trainer with id " +  id);
		return taskCompletionDAO.findAllTasksByTrainerId(id);
	}

	/**
	 * SAVE A NEW TASK
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public void saveOrUpdateTask(TrainerTask task) {
		log.debug("Save task: " + task);
		taskDAO.saveOrUpdateTask(task);
	}
	
	/**
	 * SAVE A NEW TASK COMPLETION
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public void saveTaskCompletion(TrainerTaskCompletion taskCompletion) {
		log.debug("Save task completed: " + taskCompletion);
		taskCompletionDAO.saveTaskCompletion(taskCompletion);
	}
}