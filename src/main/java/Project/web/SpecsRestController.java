package Project.web;

import Project.domain.Car;
import Project.domain.Specs;
import Project.service.CarService;
import Project.service.SpecsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class SpecsRestController {
    @Autowired
    private SpecsService specsService;
    @Autowired
    private CarService carService;

    @RequestMapping(value = "/specs", method = RequestMethod.GET)
    public List<Specs> specs() {
        return specsService.getAllSpecs();
    }


    @RequestMapping(value = "/specs/{id}", method = RequestMethod.GET)
    public ResponseEntity<Specs> specById(@PathVariable Long id) {
        Specs spec = specsService.getSpecsById(id);
        if (spec == null) {
            return new ResponseEntity<Specs>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<Specs>(spec, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/specs/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Specs> deleteSpec(@PathVariable Long id) {
        Specs spec = specsService.getSpecsById(id);
        if (spec == null) {
            return new ResponseEntity<Specs>(HttpStatus.NO_CONTENT);
        } else {
            specsService.delete(spec.getId());
            return new ResponseEntity<Specs>(spec, HttpStatus.OK);
        }

    }

    @RequestMapping(value = "/specs", method = RequestMethod.POST)
    public ResponseEntity<Specs> createSpec(@RequestBody Specs spec) {
        return new ResponseEntity<Specs>(specsService.createOrUpdateSpecs(spec, false), HttpStatus.CREATED);
    }


    @RequestMapping(value = "/specs", method = RequestMethod.PUT)
    public Specs updateSpec(@RequestBody Specs spec) {
        if (specsService.getSpecsById(spec.getId()) != null
                && specsService.getSpecsById(spec.getId()).getId() != spec.getId()) {
            throw new RuntimeException("spec already exist");
        }
        return specsService.createOrUpdateSpecs(spec, true);
    }

    @RequestMapping(value = "/getcarspecs/{id}", method = RequestMethod.GET)
    public ResponseEntity<Specs> specByCarId(@PathVariable Long id) {
        Car car = carService.getCarById(id);

        if (car.getSpecs() != null)
            return new ResponseEntity<Specs>(car.getSpecs(), HttpStatus.OK);
        else
            return new ResponseEntity<Specs>(HttpStatus.NO_CONTENT);
    }

}
