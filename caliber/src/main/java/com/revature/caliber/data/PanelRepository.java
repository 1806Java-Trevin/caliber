package com.revature.caliber.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.revature.caliber.beans.Panel;
import com.revature.caliber.beans.PanelStatus;

/**
 * Spring Data operations for the type {@link Panel}
 * 
 * @author Patrick Walsh
 *
 */
@Repository
public interface PanelRepository extends JpaRepository<Panel, Integer> {
	
	/**
	 * Save a panel object to the database
	 * 
	 * @param panel
	 * @return updated panel object
	 */
	@SuppressWarnings("unchecked")
	public Panel save(Panel panel);
	
	/**
	 * Find panel by the given identifier. Initialize panel feedback
	 * 
	 * @param id
	 * @return panel
	 */
	public Panel findOne(Integer panelId);
	
	/**
	 * Looks for all panels where the user was the trainee
	 * 
	 * @param traineeId
	 * @return all panels for given trainee
	 */
	public List<Panel> findAllByTraineeTraineeId(Integer traineeId);
	
	/**
	 * Looks for all panels with the given panel status
	 * 
	 * @return all panels in descending order by interviewDate
	 */
	public List<Panel> findAllByStatusOrderByInterviewDateDesc(PanelStatus status);
	
	/**
	 * Find all panels. Useful for listing available panels
	 * 
	 * @return all panels
	 */
	public List<Panel> findAll();
	
	/**
	 * Looks for all panels within past 14 days (includes today and 13 days before)
	 * 
	 * @return all panels within last 14 days
	 */
	@Query(value="FROM Panel p WHERE p.interviewDate >= TRUNC(SYSDATE) - 13")
	public List<Panel> findBiWeeklyPanels();
	
	/**
	 * Delete a panel from the database
	 * 
	 * @param panelId
	 */
	public void delete(Integer id);
	
	/**
	 * Return all panel interviews for a given batchId
	 * 
	 * TODO refactor this to be more microservice-friendly
	 */
	public List<Panel> findAllByTraineeBatchBatchIdOrderByInterviewDateDesc(Integer batchId); 
}
