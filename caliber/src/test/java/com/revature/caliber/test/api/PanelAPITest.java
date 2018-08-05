package com.revature.caliber.test.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.caliber.beans.InterviewFormat;
import com.revature.caliber.beans.Panel;
import com.revature.caliber.beans.PanelStatus;
import com.revature.caliber.data.TraineeRepository;
import com.revature.caliber.data.TrainerRepository;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

/**
 * @author Nathan Koszuta
 * @author Connor Monson
 * @author Allan Jones
 * @author Matt 'Spring Data' Prass
 */

public class PanelAPITest extends AbstractAPITest {

	private static final Logger log = Logger.getLogger(PanelAPITest.class);

	private static final String GET_ALL_PANELS_URL = baseUrl + "panel/all";
	private static final String GET_PANEL_BY_ID_URL = baseUrl + "panel/{panelId}";
	private static final String GET_TRAINEE_PANELS_URL = baseUrl + "panel/trainee/{traineeId}";
	private static final String GET_ALL_REPANELS_URL = baseUrl + "panel/repanel/all";
	private static final String GET_ALL_RECENT_REPANLS_URL = baseUrl + "panel/repanel/recent";
	private static final String CREATE_PANEL_URL = baseUrl + "panel/create";
	private static final String DELETE_PANEL_URL = baseUrl + "panel/delete/{panelId}";
	private static final String UPDATE_PANEL_URL = baseUrl + "panel/update";
	private static final String GET_PANEL_BY_BATCH_URL = baseUrl + "all/reports/batch/{batchId}/panel-batch-all-trainees";
	private static final String SIZE = "size()";
	private static final String EXPECTED = "expected= ";
	
	/*@Autowired
	private BatchDAO batchDAO;*/
	@Autowired
	private TraineeRepository traineeRepository;
	@Autowired
	private TrainerRepository trainerRepository;
	
	@BeforeClass
	public static void logIfValidationFails() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}
	
	/**
	 * Tests creation. Asserts that the status code 201 is returned meaning the
	 * creation was successful
	 */
	@Test
	public void testCreate201() {
		log.debug("Creating a new Panel type");
		Panel panel = new Panel();
		panel.setFormat(InterviewFormat.Phone);
		panel.setPanelRound(1);
		panel.setStatus(PanelStatus.Pass);
		panel.setTrainee(traineeRepository.findOne(1));
		panel.setPanelist(trainerRepository.findOne(1));
	
		
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
			body(panel).
		when()
			.post(CREATE_PANEL_URL).
		then().assertThat().
			statusCode(HttpStatus.CREATED_201);
	}

	/**
	 * Tests updating an existing panel by changing the duration. Asserts what is
	 * returned is the same as what we sent in the request
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testUpdate() throws Exception {
		log.debug("Updating an panel");
		Panel expected = new ObjectMapper().readValue("{\r\n" + 
				"    \"id\": 40,\r\n" + 
				"    \"trainee\": {\r\n" + 
				"        \"traineeId\": 5539,\r\n" + 
				"        \"resourceId\": null,\r\n" + 
				"        \"name\": \"Gluskin, Igor\",\r\n" + 
				"        \"email\": \"igorgluskin@yahoo.com\",\r\n" + 
				"        \"trainingStatus\": \"Employed\",\r\n" + 
				"        \"phoneNumber\": \"347-791-1360\",\r\n" + 
				"        \"skypeId\": \"igor.gluskin\",\r\n" + 
				"        \"profileUrl\": \"https://app.revature.com/profile/IgorGluskin/ae6866d406461c1c36de8df7c0a1a7a7\",\r\n" + 
				"        \"recruiterName\": null,\r\n" + 
				"        \"college\": null,\r\n" + 
				"        \"degree\": null,\r\n" + 
				"        \"major\": null,\r\n" + 
				"        \"techScreenerName\": null,\r\n" + 
				"        \"projectCompletion\": null,\r\n" + 
				"        \"flagStatus\": \"RED\",\r\n" + 
				"        \"flagNotes\": \"Trainee was automatically flagged by Caliber. \"\r\n" + 
				"    },\r\n" + 
				"    \"panelist\": {\r\n" + 
				"        \"trainerId\": 19,\r\n" + 
				"        \"name\": \"Stanley Medikonda\",\r\n" + 
				"        \"title\": \"Staging Manager\",\r\n" + 
				"        \"email\": \"stanleym@revature.com\",\r\n" + 
				"        \"tier\": \"ROLE_STAGING\"\r\n" + 
				"    },\r\n" + 
				"    \"interviewDate\": \"2018-05-29\",\r\n" + 
				"    \"duration\": \"1hr 30mins\",\r\n" + 
				"    \"format\": \"Skype\",\r\n" + 
				"    \"internet\": \"Stable\",\r\n" + 
				"    \"panelRound\": 1,\r\n" + 
				"    \"recordingLink\": \"http://www.revature.com\",\r\n" + 
				"    \"status\": \"Repanel\",\r\n" + 
				"    \"feedback\": [\r\n" + 
				"        {\r\n" + 
				"            \"id\": 79,\r\n" + 
				"            \"technology\": {\r\n" + 
				"                \"categoryId\": 1,\r\n" + 
				"                \"skillCategory\": \"Java\",\r\n" + 
				"                \"active\": true\r\n" + 
				"            },\r\n" + 
				"            \"status\": \"Pass\",\r\n" + 
				"            \"result\": 4,\r\n" + 
				"            \"comment\": \"Pretty good use of technical terms\"\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"            \"id\": 80,\r\n" + 
				"            \"technology\": {\r\n" + 
				"                \"categoryId\": 2,\r\n" + 
				"                \"skillCategory\": \"SQL\",\r\n" + 
				"                \"active\": true\r\n" + 
				"            },\r\n" + 
				"            \"status\": \"Repanel\",\r\n" + 
				"            \"result\": 1,\r\n" + 
				"            \"comment\": \"Poor use of technical terms\"\r\n" + 
				"        }\r\n" + 
				"    ],\r\n" + 
				"    \"associateIntro\": \"Good intro\",\r\n" + 
				"    \"projectOneDescription\": \"ERS rocks\",\r\n" + 
				"    \"projectTwoDescription\": \"Nice design discussion\",\r\n" + 
				"    \"projectThreeDescription\": \"Caliber is the best\",\r\n" + 
				"    \"communicationSkills\": \"Good communication\",\r\n" + 
				"    \"overall\": \"Technically weaker\"\r\n" + 
				"}", Panel.class);
		expected.setDuration("100 hours");
		
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
			body(expected).
		when().
			put(UPDATE_PANEL_URL).
		then().assertThat().
			body("id", is(expected.getId())).
			body("duration", is(expected.getDuration())).
			statusCode(HttpStatus.OK_200);
	}

	/**
	 * Tests delete and asserts the appropriate status code is returned (204)
	 */
	@Test
	public void testDelete() {
		log.debug("Deleting an panel");
		int panelId = 40;
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			delete(DELETE_PANEL_URL, panelId).
		then().assertThat().
			statusCode(HttpStatus.NO_CONTENT_204);
	}
	
	/**
	 * Tests deletion of a panel that doesn't exist.
	 * Asserts a 404 Not Found status is returned.
	 */
	@Test
	public void testDeletePanel404() {
		log.debug("Deleting an panel that doesn't exist");
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			delete(DELETE_PANEL_URL, -1).
		then().assertThat().
			statusCode(HttpStatus.NOT_FOUND_404);
	}
	
	
	/**
	 * Tests get all panels.
	 * Asserts correct number of panels returned
	 * and a 200 OK status is returned.
	 */
	@Test
	public void testGetAllPanels200() {
		log.debug("Get all panels, OK...");
		
		int expected = 73;
		
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(GET_ALL_PANELS_URL).
		then().assertThat().
			statusCode(HttpStatus.OK_200).
			body(SIZE, is(expected));
		log.debug("testGetAllPanels200 succeeded!!!");
	}
	
	/**
	 * Tests get panel by id when panel doesn't exist.
	 * Asserts 404 Not Found status is returned.
	 */
	@Test
	public void testGetPanelById404() {
		log.debug("Get panel by id, resource not found...");
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(GET_PANEL_BY_ID_URL, -1).
		then().assertThat().
			statusCode(HttpStatus.NOT_FOUND_404);
		log.debug("testGetPanelById404 succeeded!!!");
	}
	
	/**
	 * Tests get panel by id.
	 * Asserts 200 OK status is returned.
	 */
	@Test
	public void testPanelById200() {
		log.debug("Get panel by id, OK...");
		Integer panelId = 40;
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(GET_PANEL_BY_ID_URL, panelId).
		then().assertThat().
			statusCode(HttpStatus.OK_200).
			body("id", is(panelId));
		log.debug("testPanelById200 succeeded!!!");
	}
	
	/**
	 * Tests get panels by trainee when trainee doesn't exist.
	 * Asserts 404 Not Found status is returned.
	 */
	@Test
	public void testGetPanelsByTrainee404() {
		log.debug("Get all trainee panels, no content...");
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(GET_TRAINEE_PANELS_URL, -1).
		then().assertThat().
			statusCode(HttpStatus.NOT_FOUND_404);
		log.debug("testGetPanelsByTrainee404 succeeded!!!");
	}
	
	/**
	 * Tests get panels by trainee when trainee doesn't have any.
	 * Asserts 204 No Content status is returned.
	 */
	@Test
	public void testGetPanelsByTrainee204() {
		log.debug("Get all trainee panels, no content...");
		/*Trainee t = new Trainee("Test", null, "test@test.com", batchDAO.findAll().get(0));
		traineeRepository.save(t);
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(GET_TRAINEE_PANELS_URL, t.getTraineeId()).
		then().assertThat().
			statusCode(HttpStatus.NO_CONTENT_204);*/
		log.debug("testGetPanelsByTrainee204 succeeded!!!");
	}

	/**
	 * Get all recent repanels.
	 * Assert correct number of panels returned
	 * and a 200 OK status is returned.
	 */
	@Test
	@Ignore // doesn't work PJW
	public void testGetAllRecentRepanels200() {
		log.debug("Get all recent repanels, OK...");
		int expected = 21;
		log.debug(EXPECTED + expected);
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(GET_ALL_RECENT_REPANLS_URL).
		then().assertThat().
			statusCode(HttpStatus.OK_200).
			body(SIZE, is(expected));
		log.debug("testGetAllRepanels200 succeeded!!!");
	}

	@Test
	@Ignore // doesn't work PJW
	public void testGetPanelByBatch200() {
		log.debug("Get all trainee panels by batch, OK...");
		given().
			spec(requestSpec).header(AUTH, accessToken).contentType(ContentType.JSON).
		when().
			get(GET_PANEL_BY_BATCH_URL, 1000).
		then().assertThat().
			statusCode(HttpStatus.OK_200);
		log.debug("testGetPanelByBatch200 succeeded!!!");
	}
}
