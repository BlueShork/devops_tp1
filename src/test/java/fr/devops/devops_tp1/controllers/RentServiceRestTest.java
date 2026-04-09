package fr.devops.devops_tp1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.devops.devops_tp1.entities.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class RentServiceRestTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testAddCar() throws Exception {
        Car car = new Car("ABC123", "Toyota", 15000.0);

        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetCars() throws Exception {
        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetCarByPlateNumber() throws Exception {
        Car car = new Car("TEST001", "Renault", 12000.0);
        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/cars/TEST001"))
                .andExpect(status().isOk());
    }
}
