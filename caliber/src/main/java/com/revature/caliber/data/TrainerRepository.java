package com.revature.caliber.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.revature.caliber.beans.Trainer;
import com.revature.caliber.beans.TrainerRole;

/**
 * Spring Data operations for the type {@link Trainer}
 * 
 * @author Patrick Walsh
 *
 */
@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Integer> {

	/**
	 * Find all trainers titles to be displayed on front end as suggestions when
	 * setting job title
	 * 
	 * @return
	 */
	@Query(value = "select distinct title FROM Trainer")
	public List<String> findAllTrainerTitles();

	/**
	 * Find a trainer by their email address. Practical for authenticating users.
	 * 
	 * @param email
	 * @return
	 */
	public Trainer findByEmail(String email);

	/**
	 * Find all trainers. Useful for listing available trainers to select as trainer
	 * and cotrainer
	 * 
	 * @return
	 */
	public List<Trainer> findAllByTierNot(TrainerRole tier);

	/**
	 * 
	 * Note: trainers must be registered in the Salesforce with a matching email
	 * address. In order to use this method for UPDATE operations, you must have the
	 * trainerId set accordingly to the trainer_id in the database.
	 * 
	 * @param trainer
	 */
	@SuppressWarnings("unchecked")
	public Trainer save(Trainer trainer);

	/**
	 * Find trainer by the given identifier
	 * 
	 * @param id
	 * @return
	 */
	public Trainer findOne(Integer trainerId);

	/**
	 * Convenience method only. Deletes a trainer from the database. Trainer will
	 * still be registered with a Salesforce account.
	 * 
	 * @param trainer
	 */
	public void delete(Trainer trainer);

}
