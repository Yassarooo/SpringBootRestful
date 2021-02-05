package Project.web;

import Project.domain.AppUser;
import Project.domain.Car;
import Project.domain.Parameters;
import Project.service.CarService;
import Project.service.ParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
public class CarRestController {
    @Autowired
    CarService carService;

    @Autowired
    ParamsService paramsService;


    /**
     * Web service for getting all the cars in the application.
     *
     * @return list of all cars
     */
    @RequestMapping(value = "/cars", method = RequestMethod.GET)
    public List<Car> getAllCars() {
        List<Car> cars = carService.getAllCars();
        return (List<Car>) cars;
    }

    /**
     * Web service for getting all the appUsers in the application.
     *
     * @return list of all cars
     */
    @RequestMapping(value = "/sellcar", method = RequestMethod.GET)
    public List<Car> getNotSoldCars() {
        List<Car> cars = carService.getAllCars();
        List<Car> NotSoldCars = new ArrayList<Car>();
        for (Car c : cars) {
            if (!c.getSold()) {
                //Parameters def = paramsService.getParamsById((long) c.getParamid());
                //float sellprice = c.getPrice() + (def.getPercentage() * (c.getPrice())) / 100;
                //c.setSellprice(sellprice);

                NotSoldCars.add(c);
            }
        }
        return (List<Car>) NotSoldCars;
    }


    /**
     * Web service for getting a car by its ID
     *
     * @param id Car cid
     * @return car
     */
    @RequestMapping(value = "/cars/{id}", method = RequestMethod.GET)
    public ResponseEntity<Car> getCarById(@PathVariable("id") Long id)
            throws RuntimeException {
        Car car = carService.getCarById(id);
        if (car == null) {
            return new ResponseEntity<Car>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<Car>(car, HttpStatus.OK);
        }
    }

    /**
     * Method for adding a car
     *
     * @param car
     * @return
     */
    @RequestMapping(value = "/cars", method = RequestMethod.POST)
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        car.setSold(false);
        Parameters def = paramsService.getParamsById((long) car.getParamid());
        if (def == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        car.setParams(def);
        if (car.getSeats() == null) {
            car.setSeats(def.getSeats());
        }
        return new ResponseEntity<Car>(carService.createOrUpdateCar(car, false), HttpStatus.CREATED);
    }


    /**
     * Method for editing an car details
     *
     * @param car
     * @return modified car
     */
    @RequestMapping(value = "/cars", method = RequestMethod.PUT)
    public Car updateCar(@RequestBody Car car) {
        return carService.createOrUpdateCar(car, true);
    }

    /**
     * Method for editing an car details
     *
     * @param car
     * @return modified car
     */
    @RequestMapping(value = "/sellcar", method = RequestMethod.PUT)
    public Car sellCar(@RequestBody Car car) {
        car.setSold(true);
        return carService.createOrUpdateCar(car, true);
    }

    /**
     * Method for deleting a car by its ID
     *
     * @param id
     * @return
     */
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