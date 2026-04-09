package fr.devops.devops_tp1.services;

import fr.devops.devops_tp1.entities.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CarServiceTest {

    private CarService carService;

    @BeforeEach
    void setUp() {
        carService = new CarService();
    }

    @Test
    void testAddCar() {
        Car car = new Car("ABC123", "Toyota", 15000.0);
        Car result = carService.addCar(car);
        assertEquals("ABC123", result.getPlateNumber());
        assertEquals(1, carService.getCars().size());
    }

    @Test
    void testGetCars() {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));
        carService.addCar(new Car("DEF456", "Renault", 12000.0));
        List<Car> cars = carService.getCars();
        assertEquals(2, cars.size());
    }

    @Test
    void testGetCarByPlateNumber() {
        carService.addCar(new Car("ABC123", "Toyota", 15000.0));
        Optional<Car> found = carService.getCarByPlateNumber("ABC123");
        assertTrue(found.isPresent());
        assertEquals("Toyota", found.get().getBrand());
    }

    @Test
    void testGetCarByPlateNumberNotFound() {
        Optional<Car> found = carService.getCarByPlateNumber("UNKNOWN");
        assertFalse(found.isPresent());
    }
}
