package Project.web;

import Project.domain.Car;
import Project.domain.Parameters;
import Project.service.CarService;
import Project.service.ParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@RestController
@RequestMapping("/api")
public class CarRestController {
    @Autowired
    CarService carService;

    @Autowired
    ParamsService paramsService;



    @RequestMapping(value = "/cars", method = RequestMethod.GET)
    public List<Car> getAllCars() {
        List<Car> cars = carService.getAllCars();
        return (List<Car>) cars;
    }

    @RequestMapping(value = "/cars/carsforparamid", method = RequestMethod.GET)
    public List<Car> getAllCarsForParamId(String paramname) {
        Parameters param = paramsService.getParamByName(paramname);
        List<Car> cars = carService.getAllCarsForParamId(param.getId());
        return (List<Car>) cars;
    }


    @RequestMapping(value = "/cars/byid", method = RequestMethod.GET)
    public List<Car> getAllCarsById() {
        List<Car> cars = carService.getAllCarsOrderBy(Comparator.comparing(Car::getId));
        return (List<Car>) cars;
    }

    @RequestMapping(value = "/cars/bymodel", method = RequestMethod.GET)
    public List<Car> getAllCarsByModel() {
        List<Car> cars = carService.getAllCarsOrderBy(Comparator.comparing(Car::getModel));
        return (List<Car>) cars;
    }

    @RequestMapping(value = "/cars/lowprice", method = RequestMethod.GET)
    public List<Car> getAllCarsByPrice() {
        List<Car> cars = carService.getAllCarsOrderBy(Comparator.comparing(Car::getPrice));
        return (List<Car>) cars;
    }

    @RequestMapping(value = "/cars/highrate", method = RequestMethod.GET)
    public List<Car> getAllCarsByRate() {
        List<Car> cars = carService.getAllCarsOrderBy(Comparator.comparing(Car::getRate));
        return (List<Car>) cars;
    }
    @RequestMapping(value = "/cars/year/{year}", method = RequestMethod.GET)
    public List<Car> getCarsOfYear(@PathVariable("year") int year) {
        List<Car> cars = carService.getCarsBySpecificYear(year);
        return (List<Car>) cars;
    }

    @RequestMapping(value = "/cars/{type}", method = RequestMethod.GET)
    public List<Car> getCarsOfType(@PathVariable("type") String type) {
        List<Car> cars = carService.getCarsBySpecificType(type);
        return (List<Car>) cars;
    }


    @RequestMapping(value = "/sellcar", method = RequestMethod.GET)
    public List<Car> getNotSoldCars() {
        List<Car> cars = carService.getAllCars();
        List<Car> NotSoldCars = new ArrayList<Car>();
        for (Car c : cars) {
            if (!c.getSold()) {
                NotSoldCars.add(c);
            }
        }
        return (List<Car>) NotSoldCars;
    }


    @RequestMapping(value = "/cars/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<Car> getCarById(@PathVariable("id") Long id)
            throws RuntimeException {
        Car car = carService.getCarById(id);
        if (car == null) {
            return new ResponseEntity<Car>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<Car>(car, HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/cars", method = RequestMethod.POST)
    public ResponseEntity<Car> createCar(@RequestBody Car car) {

        car.setSold(false);
        Parameters def = paramsService.getParamById((long) car.getParamid());
        if (def == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        car.setParams(def);
        if (car.getSeats() == null || car.getSeats() == 0) {
            car.setSeats(def.getSeats());
        }
        float sellprice = car.getPrice() + (car.getParams().getPercentage() * (car.getPrice())) / 100;
        car.setSellprice(sellprice);
        car.setType(car.getParams().getName());
        return new ResponseEntity<Car>(carService.createOrUpdateCar(car, false), HttpStatus.CREATED);
    }


    @RequestMapping(value = "/cars", method = RequestMethod.PUT)
    public ResponseEntity<Car> updateCar(@RequestBody Car car) {
        return new ResponseEntity<Car>(carService.createOrUpdateCar(car, true), HttpStatus.OK);
    }


    @RequestMapping(value = "/sellcar", method = RequestMethod.PUT)
    public ResponseEntity<Car> sellCar(@RequestBody Car car) {
        car.setSold(true);
        return new ResponseEntity<Car>(carService.createOrUpdateCar(car, true), HttpStatus.OK);
    }


    @RequestMapping(value = "/cars/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Car> deleteCar(@PathVariable Long id) {
        Car car = carService.getCarById(id);
        if (car == null) {
            return new ResponseEntity<Car>(HttpStatus.NO_CONTENT);
        } else {
            carService.deleteCarById(car.getId());
            return new ResponseEntity<Car>(car, HttpStatus.OK);
        }

    }


}