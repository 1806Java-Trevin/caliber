package com.revature.caliber.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.revature.caliber.beans.AssessmentType;
import com.revature.caliber.beans.NoteType;
import com.revature.caliber.beans.QCStatus;

@RestController
public class TypeController {
	
	private static final Logger log = Logger.getLogger(TypeController.class);

	/**
	 * Get note types to select appropriate type on UI
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/note/all", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('VP', 'TRAINER', 'QC', 'STAGING','PANEL')")
	public ResponseEntity<List<String>> allNoteTypes() {
		log.debug("Fetching note types");
		List<String> types = Stream.of(NoteType.values()).map(Enum::name).collect(Collectors.toList());
		return new ResponseEntity<>(types, HttpStatus.OK);
	}

	/**
	 * Get QC Status types to select appropriate type on UI
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/qcstatus/all", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('VP', 'TRAINER', 'QC', 'STAGING','PANEL')")
	public ResponseEntity<List<String>> allQCStatusTypes() {
		log.debug("Fetching QC status types");
		List<String> types = Stream.of(QCStatus.values()).map(Enum::name).collect(Collectors.toList());
		return new ResponseEntity<>(types, HttpStatus.OK);
	}

	/**
	 * Get assessment types for dropdown selection on the UI
	 *
	 * @param assessment the assessment
	 * @return the response entity
	 */
	@RequestMapping(value = "/assessment/all", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('VP', 'TRAINER', 'STAGING')")
	public ResponseEntity<List<String>> allAssessmentTypes() {
		log.debug("Fetching assessment types");
		List<String> types = Stream.of(AssessmentType.values()).map(Enum::name).collect(Collectors.toList());
		return new ResponseEntity<>(types, HttpStatus.OK);
	}

	
}
