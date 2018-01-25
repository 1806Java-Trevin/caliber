package com.revature.caliber.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DtoApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void testMockServer()		//dummy test that won't do what we want, just an example so we can expand later
	{
		mockServerClient
	    .when(
	        new HttpRequest()
	            .withMethod("POST")
	            .withPath("/login")
	            .withBody("{username: 'foo', password: 'bar'}")
	    )
	    .respond(
	        new HttpResponse()
	            .withStatusCode(302)
	            .withCookie(
	                "sessionId", "2By8LOhBmaW5nZXJwcmludCIlMDAzMW"
	            )
	            .withHeader(
	                "Location", "https://www.mock-server.com"
	            )
	    );
	}

}
