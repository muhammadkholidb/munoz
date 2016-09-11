package hu.pe.munoz.commonrest.controller.test;

import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractRestClientServerTest {

    protected RestTemplate restTemplate = new RestTemplate();

    protected MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

    protected ObjectMapper mapper = new ObjectMapper();

    protected void createServer(String path) {
        
    }
    
}
