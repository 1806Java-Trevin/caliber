package com.revature.caliber.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.caliber.beans.PanelFeedback;
import com.revature.caliber.beans.PanelStatus;

/**
 * Spring Data operations for the type {@link PanelFeedback}
 * 
 * This is a conversion from a Caliber 1 DAO class. This interface is not
 * actually used within the service classes. May be useful eventually.
 * 
 * @author Patrick Walsh
 *
 */
@Repository
public interface PanelFeedbackRepository extends JpaRepository<PanelFeedback, Long> {

	/**
	 * Save PanelFeedback to database
	 * 
	 * @param Updated
	 *            PanelFeedback object
	 */
	@SuppressWarnings("unchecked")
	public PanelFeedback save(PanelFeedback panelFeedback);

	/**
	 * Find panel by the given identifier
	 * 
	 * @param id
	 * @return PanelFeedback object or null
	 */
	public PanelFeedback findOne(Long id);

	/**
	 * Find all panels. Useful for listing available panels
	 * 
	 * @return all panels or null
	 */
	public List<PanelFeedback> findAll();

	/**
	 * Find all panel feedbacks for a panel.
	 * 
	 * @return all panels for a given Panel or null
	 */
	public List<PanelFeedback> findByPanelId(int panelId);

	/**
	 * Find all panel feedbacks for one panel
	 * 
	 * @return List of panel feedbacks for a given panel
	 */
	public List<PanelFeedback> findByPanelIdAndPanelStatus(int panelId, PanelStatus status);

	/**
	 * Delete a PanelFeedback object by its given identifier
	 * 
	 */
	public void delete(Long id);

}
