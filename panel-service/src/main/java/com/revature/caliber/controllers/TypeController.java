package com.revature.caliber.controllers;

import java.util.ArrayList;
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

import com.revature.caliber.beans.InterviewFormat;
import com.revature.caliber.beans.PanelStatus;

@RestController
public class TypeController {
	
	private static final Logger log = Logger.getLogger(TypeController.class);

	/**
	 * Get Panel Status for dropdown selection on the UI
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/panelstatus/all", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('VP', 'PANEL')")
	public ResponseEntity<List<String>> allPanelStatus() {
		log.debug("Fetching Panel Status");
		List<String> types = Stream.of(PanelStatus.values()).map(Enum::name).collect(Collectors.toList());
		return new ResponseEntity<>(types, HttpStatus.OK);
	}

	/**
	 * Get Interview Format for dropdown selection on the UI
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/interviewformat/all", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('VP', 'PANEL')")
	public ResponseEntity<List<String>> allInterviewFormat() {
		log.debug("Fetching Interview Format");
		List<String> types = Stream.of(InterviewFormat.values()).map(Enum::name).collect(Collectors.toList());
		types = hyphenate(types);
		return new ResponseEntity<>(types, HttpStatus.OK);
	}
	
    public static List<String> hyphenate(List<String> list) {
        List<String> hyphenatedlist = new ArrayList<>();
        for(String s: list) {
        hyphenatedlist.add(s.replace("_", "-"));
        }
		return hyphenatedlist;
    }
	
}
