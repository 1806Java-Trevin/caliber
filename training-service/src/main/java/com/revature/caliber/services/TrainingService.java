package com.revature.caliber.services;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.caliber.beans.Address;
import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.beans.TrainerRole;
import com.revature.caliber.beans.TrainingStatus;
import com.revature.caliber.data.AddressRepository;
import com.revature.caliber.data.BatchRepository;
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

	@Autowired
	private BatchRepository batchRepository;

	@Autowired
	private AddressRepository addressRepository;

	/*
	 *******************************************************
	 * LOCATION SERVICES
	 *
	 *******************************************************
	 */

	/**
	 * Add new Address
	 * 
	 * @param location
	 */
	public void createLocation(Address location) {
		log.debug("Creating Location " + location);
		addressRepository.save(location);
	}

	/**
	 * Update existing Address
	 * 
	 * @param location
	 */
	public void update(Address location) {
		log.debug("Update location: " + location);
		addressRepository.save(location);
	}

	/**
	 * retrieve all locations
	 * 
	 * @return all Addresses in the database
	 */
	public List<Address> findAllLocations() {
		log.debug("Finding all locations");
		return addressRepository.findAll();
	}

	/**
	 * Find Address with provided id
	 * 
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
	 * Find trainer by email
	 *
	 * @param email
	 * @return
	 */
	public Trainer findTrainer(String email) {
		log.debug("Find trainer by email " + email);
		return trainerRepository.findByEmail(email);
	}

	/**
	 * Find all trainers (not inactive trainers)
	 *
	 * @return
	 */
	public List<Trainer> findAllTrainers() {
		log.debug("Finding all trainers");
		return trainerRepository.findAllByTierNot(TrainerRole.ROLE_INACTIVE);
	}

	/**
	 * Update trainer
	 *
	 * @param trainer
	 */
	public Trainer update(Trainer trainer) {
		log.debug("Update trainer: " + trainer);
		return trainerRepository.save(trainer);
	}

	/**
	 * Find trainer by id
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
	 * Change a trainer's status to Inactive
	 *
	 * @param trainer
	 **/
	public void makeInactive(Trainer trainer) {
		log.debug(trainer + " is now inactive");
		trainer.setTier(TrainerRole.ROLE_INACTIVE);
		trainerRepository.save(trainer);
	}

	/**
	 * Find all distinct titles that have been given to trainers. Useful for
	 * populating suggested titles on the UI.
	 * 
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
	 * Add another week to a batch
	 *
	 * @param batchId
	 */
	public void addWeek(Integer batchId) {
		log.debug("Adding week to batch: " + batchId);
		Batch batch = batchRepository.findOne(batchId);
		if (batch == null)
			throw new IllegalArgumentException("Invalid batch");
		int weeks = batch.getWeeks();
		batch.setWeeks(++weeks);
		batchRepository.save(batch);
	}

	/**
	 * Save batch
	 *
	 * @param batch
	 */
	public Batch save(Batch batch) {
		log.debug("Saving batch: " + batch);
		return batchRepository.save(batch);
	}

	/**
	 * Looks for all batches without any restriction. Dropped trainees are excluded.
	 *
	 * @return
	 */
	public List<Batch> findAllBatches() {
		log.debug("Find all batches");
		return filterOutDroppedTrainees(batchRepository.findAll());
	}

	/**
	 * Looks for all batches within the last 2 months. Dropped trainees are
	 * excluded.
	 *
	 * @return
	 */
	public List<Batch> findAllCurrentBatches() {
		log.debug("Find all current batches");
		return filterToCurrentBatches(batchRepository.findAll());
	}

	/**
	 * Looks for all batches where the user was the trainer or co-trainer. Dropped
	 * trainees are excluded.
	 *
	 * @param trainerId
	 * @return
	 */
	public List<Batch> findAllBatches(int trainerId) {
		log.debug("Find all batches for trainer: " + trainerId);
		return filterOutDroppedTrainees(batchRepository.findAllByTrainer(trainerId));
	}

	/**
	 * Looks for all batches where the user was the trainer or co-trainer. Batches
	 * returned are currently actively in training. Dropped trainees are excluded.
	 *
	 * @param trainerId
	 * @return
	 */
	public List<Batch> findAllCurrentBatches(int trainerId) {
		log.debug("Find all current batches for trainer: " + trainerId);
		return filterOutDroppedTrainees(filterToCurrentBatches(batchRepository.findAllByTrainer(trainerId)));
	}

	/**
	 * Find a batch by its given identifier. Dropped trainees are excluded.
	 *
	 * @param batchId
	 * @return
	 */
	public Batch findBatch(Integer batchId) {
		log.debug("Finding batch with id: " + batchId);
		return filterOutDroppedTrainees(batchRepository.findOne(batchId));
	}

	/**
	 * Update batch
	 *
	 * @param batch
	 */
	public void update(Batch batch) {
		log.debug("Update batch " + batch);
		batchRepository.save(batch);
	}

	/**
	 * Delete batch
	 *
	 * @param batch
	 */
	public void delete(Batch batch) {
		log.debug("Delete batch " + batch);
		batchRepository.delete(batch);
	}

	/**
	 * Find all batches after a given date. Dropped trainees are excluded.
	 * 
	 * @param date
	 * @return batches
	 */
	public List<Batch> findAllAfterDate(Calendar startDate) {
		return filterOutDroppedTrainees(batchRepository.findAll().stream()
				.filter(batch -> batch.getStartDate().after(startDate.getTime())).collect(Collectors.toList()));
	}

	/**
	 * Helper method to filter all Dropped trainees from a batch.
	 * 
	 * @param batch
	 * @return filtered batch
	 */
	private static Batch filterOutDroppedTrainees(Batch batch) {
		batch.setTrainees(batch.getTrainees().stream()
				.filter(t -> !t.getTrainingStatus().equals(TrainingStatus.Dropped)).collect(Collectors.toSet()));
		return batch;
	}

	/**
	 * Helper method to filter all Dropped trainees from a list of batches.
	 * 
	 * @param batches
	 * @return filtered batches
	 */
	private static List<Batch> filterOutDroppedTrainees(List<Batch> batches) {
		for (Batch batch : batches) {
			filterOutDroppedTrainees(batch);
		}
		return batches;
	}

	/**
	 * Filter the given batches to only have batches currently in training or ended
	 * within the last month
	 * 
	 * @param batches
	 * @return current batches
	 */
	private static List<Batch> filterToCurrentBatches(List<Batch> batches) {
		LocalDate oneMonthAgo = LocalDate.now().minus(Period.ofMonths(1));
		return batches.stream().filter(batch -> batch.getEndDate().after(java.sql.Date.valueOf(oneMonthAgo)))
				.collect(Collectors.toList());
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

	/**
	 * Find a trainee by their Salesforce resourceId
	 * 
	 * @param resourceId
	 * @return
	 */
	public Trainee findTraineeByResourceId(String resourceId) {
		return traineeRepository.findByResourceId(resourceId);
	}

}
