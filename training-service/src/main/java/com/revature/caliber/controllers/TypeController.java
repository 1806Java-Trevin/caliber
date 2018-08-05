package com.revature.caliber.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.revature.caliber.beans.SkillType;
import com.revature.caliber.beans.TrainerRole;
import com.revature.caliber.beans.TrainingStatus;
import com.revature.caliber.beans.TrainingType;

/**
 * Provides enumerated types to the UI
 * 
 * @author Patrick Walsh
 * @author Stanley Chouloute
 *
 */
@RestController
//@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "types", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://ec2-54-163-132-124.compute-1.amazonaws.com")
public class TypeController {

	private static final Logger log = Logger.getLogger(TypeController.class);

	/*
	 *******************************************************
	 * TODO TYPE SERVICES
	 *
	 *******************************************************
	 */

	/**
	 * Get Skill types to select appropriate type on UI
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/skill/all", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('VP', 'STAGING','TRAINER','QC','PANEL')")
	public ResponseEntity<List<String>> allSkillTypes() {
		log.debug("Fetching skill types");
		List<String> types = Stream.of(SkillType.values()).map(Enum::toString).collect(Collectors.toList());
		return new ResponseEntity<>(types, HttpStatus.OK);
	}

	/**
	 * Get Training types to select appropriate type on UI
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/training/all", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('VP', 'STAGING', 'QC', 'TRAINER','PANEL')")
	public ResponseEntity<List<String>> allTrainingTypes() {
		log.debug("Fetching training types");
		List<String> types = Stream.of(TrainingType.values()).map(Enum::name).collect(Collectors.toList());
		return new ResponseEntity<>(types, HttpStatus.OK);
	}

	/**
	 * Get Training Status types to select appropriate type on UI
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/trainingstatus/all", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('VP', 'STAGING', 'QC', 'TRAINER','PANEL')")
	public ResponseEntity<List<String>> allTrainingStatusTypes() {
		log.debug("Fetching training status types");
		List<String> types = Stream.of(TrainingStatus.values()).map(Enum::name).collect(Collectors.toList());
		return new ResponseEntity<>(types, HttpStatus.OK);
	}
	
	/**
	 * Get Trainer Tier for dropdown selection on the UI
	 *
	 * @param Role
	 *            the Tier
	 * @return the response entity
	 */
	@RequestMapping(value = "/trainer/role/all", method = RequestMethod.GET)
	//@PreAuthorize("hasAnyRole('VP', 'STAGING','PANEL')")
	public ResponseEntity<List<String>> allTrainerRoles() {
		log.debug("Fetching Trainer Roles");
		// Used toString to Display the roles without the underscore
		List<String> types = Stream.of(TrainerRole.values()).map(Enum::name).collect(Collectors.toList());
		return new ResponseEntity<>(types, HttpStatus.OK);
	}

}
