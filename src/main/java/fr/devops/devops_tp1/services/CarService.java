package fr.devops.devops_tp1.services;

import fr.devops.devops_tp1.entities.Car;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    private final List<Car> cars = new ArrayList<>();

    public Car addCar(Car car) {
        cars.add(car);
        return car;
    }

    public List<Car> getCars() {
        return cars;
    }

    public Optional<Car> getCarByPlateNumber(String plateNumber) {
        return cars.stream()
                .filter(car -> car.getPlateNumber().equals(plateNumber))
                .findFirst();
    }
}
