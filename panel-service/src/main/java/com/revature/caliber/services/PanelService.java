package com.revature.caliber.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.caliber.beans.Panel;
import com.revature.caliber.beans.PanelFeedback;
import com.revature.caliber.beans.PanelStatus;
import com.revature.caliber.data.PanelFeedbackRepository;
import com.revature.caliber.data.PanelRepository;
import com.revature.caliber.exceptions.MalformedRequestException;

/**
 * Provides logic concerning Panel and PanelFeedback data. Application logic has
 * no business being in a DAO nor in a Controller. This is the ideal place for
 * calculations
 *
 * @author Connor Monson
 * @author Matt 'Spring Data' Prass
 * @author Patrick Walsh
 *
 */
@Service
public class PanelService {

	private static final Logger log = Logger.getLogger(PanelService.class);

	@Autowired
	private PanelRepository panelRepository;
	
	@Autowired
	private PanelFeedbackRepository panelFeedbackRepository;

	/*
	 *******************************************************
	 * PANEL SERVICES
	 *
	 *******************************************************
	 */
	public void update(Panel panel) {
		log.debug("Update panel: " + panel);
		panelRepository.save(panel);
	}

	public void createPanel(Panel panel) throws MalformedRequestException {
		log.debug("Creating Panel " + panel);
		log.debug("Panel for Trainee: " +panel.getTrainee());
		List<Panel> previousPanels = findByTraineeId(panel.getTrainee());
		log.debug("Trainee's Previous Panels: " + previousPanels);
		//verifying server side that the panel round field has not been tampered with
		if(previousPanels.size()+1 != panel.getPanelRound()) {
			log.warn("Failed to create panel. Panel round calculation incorrect.");
			throw new MalformedRequestException();
		}
		else {
			panelRepository.save(panel);
			for(PanelFeedback feedback : panel.getFeedback()) {
				panelFeedbackRepository.save(feedback);
			}
		}
	}

	public void deletePanel(int panelId) {
		log.debug("Deleting Panel " + panelId);
		panelRepository.delete(panelId);
	}

	public List<Panel> findAllPanels() {
		log.debug("Finding all panels");
		return panelRepository.findAll();
	}

	public List<Panel> findAllRepanel() {
		log.debug("Finding all panels");
		return panelRepository.findAllByStatusOrderByInterviewDateDesc(PanelStatus.Repanel);
	}

	public Panel findById(int panelId) {
		log.debug("Getting Panel with ID " + panelId);
		return panelRepository.findOne(panelId);
	}
	
	/**
	 * Find all the panel interviews within the last 2 weeks
	 * 
	 * @return panels
	 */
	public List<Panel> findBiWeeklyPanels() {
		return panelRepository.findBiWeeklyPanels();
	}
	
	/**
	 * Find all by batchId. Orders by Interview Date descending
	 * 
	 * @param batchId
	 * @return
	 */
	public List<Panel> findAllByBatchId(Integer batchId) {
		return panelRepository.findAllByTraineeBatchBatchIdOrderByInterviewDateDesc(batchId);
	}

	public List<Panel> findByTraineeId(int traineeId) {
		log.debug("Getting Panels with trainee ID " + traineeId);
		List<Panel> panels = panelRepository.findAllByTrainee(traineeId);
		panels.parallelStream();
		return panels;
	}

	/**
	 * FIND ALL PANELS WHERE THE TRAINEE'S LAST PANEL HAD STATUS REPANEL
	 */
	public List<Panel> findAllRecentRepanel() {
		log.debug("Find all trainees whose last panel had status Repanel");
		List<Integer> traineeIds = panelRepository.findAllTraineesWithPanels();
		List<Panel> result = new ArrayList<>();
		for (Integer trainee : traineeIds) {
			List<Panel> panels = this.findByTraineeId(trainee);
			if (panels != null && !panels.isEmpty()) {
				Panel p = mostRecentPanel(panels);
				if (p.getStatus() == PanelStatus.Repanel) {
					result.add(p);
					continue;
				}
			}
		}
		return result;
	}

	private Panel mostRecentPanel(List<Panel> panels) {
		int max = -1;
		Panel result = null;
		for (Panel p : panels)
			if (max < p.getPanelRound()) {
				max = p.getPanelRound();
				result = p;
			}
		return result;
	}

}
