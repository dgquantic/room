package com.room.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.room.demo.model.OccupancyRequest;
import com.room.demo.model.OptimizedOccupancy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class RoomOptimizationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void optimizeRoomUsageTest () throws Exception {
        OccupancyRequest guestData = getGuestsFromJson("test_1.json");

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/occupancy/optimize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guestData)))
                .andReturn();

        mvcResult.getRequest().getAsyncContext().addListener(new TestAsyncListener());

        // Get the ResponseEntity from the async result
        ResponseEntity<OptimizedOccupancy> responseEntity = (ResponseEntity<OptimizedOccupancy>) mvcResult.getAsyncResult();

        // Extract the OptimizedOccupancy object from the ResponseEntity
        OptimizedOccupancy result = responseEntity.getBody();

        // Check the status of the ResponseEntity
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Assert the results
        assert result != null;
        assertEquals(3, result.occupiedPremium());
        assertEquals(3, result.occupiedEconomy());
        assertEquals(738d, result.premiumRevenue(), 0.01d);
        assertEquals(167.99d, result.economyRevenue(), 0.01d);
        assertNotNull(result.totalRevenue());
    }

    @Test
    public void optimizeRoomUsageTest2 () throws Exception {
        OccupancyRequest guestData = getGuestsFromJson("test_2.json");

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/occupancy/optimize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guestData)))
                .andReturn();

        mvcResult.getRequest().getAsyncContext().addListener(new TestAsyncListener());

        // Get the ResponseEntity from the async result
        ResponseEntity<OptimizedOccupancy> responseEntity = (ResponseEntity<OptimizedOccupancy>) mvcResult.getAsyncResult();

        // Extract the OptimizedOccupancy object from the ResponseEntity
        OptimizedOccupancy result = responseEntity.getBody();

        // Check the status of the ResponseEntity
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Assert the results
        assert result != null;
        assertEquals(6, result.occupiedPremium());
        assertEquals(4, result.occupiedEconomy());
        assertEquals(1054d, result.premiumRevenue(), 0.01d);
        assertEquals(189.99d, result.economyRevenue(), 0.01d);
        assertNotNull(result.totalRevenue());
    }

    @Test
    public void optimizeRoomUsageTest3 () throws Exception {
        OccupancyRequest guestData = getGuestsFromJson("test_3.json");

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/occupancy/optimize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guestData)))
                .andReturn();

        mvcResult.getRequest().getAsyncContext().addListener(new TestAsyncListener());

        // Get the ResponseEntity from the async result
        ResponseEntity<OptimizedOccupancy> responseEntity = (ResponseEntity<OptimizedOccupancy>) mvcResult.getAsyncResult();

        // Extract the OptimizedOccupancy object from the ResponseEntity
        OptimizedOccupancy result = responseEntity.getBody();

        // Check the status of the ResponseEntity
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Assert the results
        assert result != null;
        assertEquals(2, result.occupiedPremium());
        assertEquals(4, result.occupiedEconomy());
        assertEquals(583d, result.premiumRevenue(), 0.01d);
        assertEquals(189.99d, result.economyRevenue(), 0.01d);
        assertNotNull(result.totalRevenue());
    }

    @Test
    public void optimizeRoomUsageTest4 () throws Exception {
        OccupancyRequest guestData = getGuestsFromJson("test_4.json");

        // Start async
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/occupancy/optimize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guestData)))
                .andExpect(request().asyncStarted())
                .andReturn();

        // Async dispatch the result
        MvcResult result = mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andReturn();

        // Retrieve the response content and map to OptimizedOccupancy
        String contentAsString = result.getResponse().getContentAsString();
        OptimizedOccupancy occupancyResult = objectMapper.readValue(contentAsString, OptimizedOccupancy.class);

        // Assert the results
        assertEquals(7, occupancyResult.occupiedPremium());
        assertEquals(1, occupancyResult.occupiedEconomy());
        assertEquals(1153.99d, occupancyResult.premiumRevenue(), 0.01d);
        assertEquals(45.0d, occupancyResult.economyRevenue(), 0.01d);
        assertNotNull(occupancyResult.totalRevenue());
    }

    private OccupancyRequest getGuestsFromJson (String name) throws IOException {
        Resource resource = new ClassPathResource(name);
        return new ObjectMapper().readValue(resource.getInputStream(), OccupancyRequest.class);
    }
}