package com.revature.caliber.tasks;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.data.SalesforceDAO;
import com.revature.caliber.services.SalesforceService;
import com.revature.caliber.services.TrainingService;

@Component
public class BatchUpdate {

	private static final Logger log = Logger.getLogger(BatchUpdate.class);

	@Autowired
	private SalesforceAuth salesforceAuth;
	@Autowired
	private SalesforceDAO salesforceDao;
	@Autowired
	private TrainingService trainingService;

	/**
	 * Used cron to perform midnight execution To update batches
	 */
	//@Scheduled(cron = "0 0/15 * * * ?") // Every 15 minutes
	@Scheduled(cron = "0 0 0 * * *") // Midnight
	public void updateBatchTask() {
		try {
			log.debug("Update Batch Task");
			boolean userSet = salesforceAuth.setUser();
			if (userSet) {
				List<Batch> caliberBatches = trainingService.findAllBatches();
				log.debug("Caliber Batch list size: " + caliberBatches.size());
				List<Batch> salesforceBatches = salesforceDao.getAllRelevantBatches();

				compareBatches(caliberBatches, salesforceBatches);
			} else {
				log.error("Unable to perform BatchUpdate");
			}

			salesforceAuth.clearUser();
			log.debug("End of Update Task");
		} catch (Exception e) {
			log.fatal(e);
		}
	}

	/**
	 * Grabs all batches from Salesforce and all batches from Caliber. For each of
	 * the Caliber batches, it checks against all the Salesforce batches.. if the
	 * resourceIds match, then the batch details in Caliber need to be updated with
	 * the data in the Salesforce. Furthermore, we then need to check each of the
	 * trainees in that batch and update their information from the salesforce as
	 * well.
	 */
	public boolean compareBatches(List<Batch> caliberBatches, List<Batch> salesforceBatches) {
		log.debug("Comparing batches...");
		for (int sIndex = 0; sIndex < salesforceBatches.size(); sIndex++) {
			if (salesforceBatches.get(sIndex).getTrainer() == null) {
				log.info(salesforceBatches.get(sIndex).getResourceId() + " batch trainer is null");
				continue;
			}
			for (int cIndex = 0; cIndex < caliberBatches.size(); cIndex++) {
				// if caliber batch does not have resourceId, it cannot be synced. continue...
				if (caliberBatches.get(cIndex).getResourceId() == null)
					continue;
				// if resourceIds are same, update all the datas with fresh Salesforce data
				if (caliberBatches.get(cIndex).getResourceId().equals(salesforceBatches.get(sIndex).getResourceId())) {
					log.info("Found batch match: " + salesforceBatches.get(sIndex).getResourceId() + " "
							+ salesforceBatches.get(sIndex).getTrainingName());
					// extract salesforce data and save
					updateBatch(caliberBatches.get(cIndex), salesforceBatches.get(sIndex));

					// extract trainee information from Salesforce and update the trainees in the
					// Caliber batch
					for (Trainee trainee : caliberBatches.get(cIndex).getTrainees()) {
						for (Trainee salesforceTrainee : salesforceDao
								.getBatchDetails(caliberBatches.get(cIndex).getResourceId())) {
							// if caliber trainee does not have resourceId, it cannot be synced. continue...
							if (trainee.getResourceId() == null)
								continue;
							if (trainee.getResourceId().equals(salesforceTrainee.getResourceId())) {
								log.info("Updating trainee: " + salesforceTrainee.getResourceId() + " " + trainee);
								// extract salesforce data and save
								updateTrainee(trainee, salesforceTrainee);
							}
						}
					}
				}
			}
		}
		return true;
	}

	private void updateTrainee(Trainee caliberTrainee, Trainee salesforceTrainee) {
		try {
			caliberTrainee.setTrainingStatus(salesforceTrainee.getTrainingStatus());
			caliberTrainee.setCollege(salesforceTrainee.getCollege());
			caliberTrainee.setDegree(salesforceTrainee.getDegree());
			caliberTrainee.setEmail(salesforceTrainee.getEmail());
			caliberTrainee.setMajor(salesforceTrainee.getMajor());
			caliberTrainee.setName(salesforceTrainee.getName());
			caliberTrainee.setPhoneNumber(salesforceTrainee.getPhoneNumber());
			caliberTrainee.setProjectCompletion(salesforceTrainee.getProjectCompletion());
			caliberTrainee.setRecruiterName(salesforceTrainee.getRecruiterName());
			caliberTrainee.setTechScreenerName(salesforceTrainee.getTechScreenerName());
			trainingService.save(caliberTrainee);
		} catch (Exception e) {
			log.fatal(e);
		}
	}

	private void updateBatch(Batch caliberBatch, Batch salesforceBatch) {
		try {
			if (salesforceBatch.getTrainer() != null) {
				caliberBatch.setTrainer(trainingService.findTrainer(salesforceBatch.getTrainer().getEmail()));
			} else {
				log.info("Trainer is null for " + salesforceBatch.getTrainingName());
				caliberBatch.setTrainer(trainingService.findTrainer(SalesforceService.DEFAULT_TRAINER));
				log.info("Trainer is now " + SalesforceService.DEFAULT_TRAINER + " for "
						+ caliberBatch.getTrainingName());
			}
			if (salesforceBatch.getCoTrainer() != null) {
				caliberBatch.setCoTrainer(trainingService.findTrainer(salesforceBatch.getCoTrainer().getEmail()));
				log.info("Cotrainer for " + salesforceBatch.getTrainingName() + " is: " + caliberBatch.getCoTrainer());
			}
			caliberBatch.setEndDate(salesforceBatch.getEndDate());
			caliberBatch.setSkillType(salesforceBatch.getSkillType());
			caliberBatch.setStartDate(salesforceBatch.getStartDate());
			caliberBatch.setTrainingName(salesforceBatch.getTrainingName());
			caliberBatch.setTrainingType(salesforceBatch.getTrainingType());
			trainingService.update(caliberBatch);
		} catch (Exception e) {
			log.fatal(e);
		}
	}

}
