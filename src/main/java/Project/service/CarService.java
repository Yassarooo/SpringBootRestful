package Project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import Project.aspect.UserAspect;
import Project.domain.Car;
import Project.repository.CarRepository;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CarService {

    @Autowired
    CarRepository carRepository;

    private final static Logger log = Logger.getLogger(UserAspect.class.getName());


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
        try {
            Optional<Car> car;
            if (update) {
                car = carRepository.findById(c.getId());
                if (car.isPresent()) {
                    Car newEntity = car.get();
                    if (c.getVersion() < newEntity.getVersion()) {
                        throw new OptimisticEntityLockException(c, "Optimistic Lock Error");
                    } else {
                        newEntity.setName(c.getName());
                        newEntity.setPrice(c.getPrice());
                        newEntity.setSeats(c.getSeats());
                        newEntity.setParams(c.getParams());
                        newEntity.setBuyername(c.getBuyername());
                        newEntity.setSelldate(c.getSelldate());
                        newEntity.setSellprice(c.getSellprice());
                        newEntity.setSold(c.getSold());
                        newEntity.setSpecs(c.getSpecs());
                        newEntity.setSpecsid(c.getSpecsid());
                        newEntity.setLevel(c.getLevel());
                        newEntity.setRate(c.getRate());
                        newEntity = carRepository.save(newEntity);
                        return newEntity;
                    }
                } else {
                    c = carRepository.save(c);
                    return c;
                }
            } else {
                c = carRepository.save(c);

                return c;
            }
        } catch (ObjectOptimisticLockingFailureException e) {
            log.severe("Somebody has already updated the amount for item:{} in concurrent transaction.");
            throw e;
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