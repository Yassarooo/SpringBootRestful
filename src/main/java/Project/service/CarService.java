package Project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Project.domain.Car;
import Project.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CarService {

    @Autowired
    CarRepository carRepository;

    public List<Car> getAllCars() {
        List<Car> carList = (List<Car>) carRepository.findAll();

        if (carList.size() > 0) {
            return carList;
        } else {
            return new ArrayList<Car>();
        }
    }

    public Car getCarById(Long id) throws RuntimeException {
        Optional<Car> car = carRepository.findById(id);

        if (car.isPresent()) {
            return car.get();
        } else {
            throw new RuntimeException("No car record exist for given id");
        }
    }

    @Transactional
    public Car createOrUpdateCar(Car c, Boolean update) throws RuntimeException {
        Optional<Car> car;
        if (update) {
            car = carRepository.findById(c.getId());
            if (car.isPresent()) {
                Car newEntity = car.get();
                newEntity.setName(c.getName());
                newEntity.setPrice(c.getPrice());
                newEntity.setSeats(c.getSeats());
                newEntity.setParams(c.getParams());
                newEntity.setBuyername(c.getBuyername());
                newEntity.setSelldate(c.getSelldate());
                newEntity.setSellprice(c.getSellprice());
                newEntity.setSold(c.getSold());
                newEntity = carRepository.save(newEntity);

                return newEntity;
            } else {
                c = carRepository.save(c);
                return c;
            }
        } else {
            c = carRepository.save(c);

            return c;
        }
    }

    @Transactional
    public void deleteCarById(Long id) throws RuntimeException {
        Optional<Car> car = carRepository.findById(id);

        if (car.isPresent()) {
            carRepository.deleteById(id);
        } else {
            throw new RuntimeException("No car record exist for given id");
        }
    }
}