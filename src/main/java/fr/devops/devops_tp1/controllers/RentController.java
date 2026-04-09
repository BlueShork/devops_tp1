package fr.devops.devops_tp1.controllers;

import fr.devops.devops_tp1.entities.Car;
import fr.devops.devops_tp1.services.CarService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class RentController {

    private final CarService carService;

    public RentController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping("/cars")
    public Car addCar(@RequestBody Car car) {
        return carService.addCar(car);
    }

    @GetMapping("/cars")
    public List<Car> getCars() {
        return carService.getCars();
    }

    @GetMapping("/cars/{plateNumber}")
    public Optional<Car> getCarByPlateNumber(@PathVariable String plateNumber) {
        return carService.getCarByPlateNumber(plateNumber);
    }
}
