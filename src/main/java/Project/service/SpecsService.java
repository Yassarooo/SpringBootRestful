package Project.service;

import Project.domain.Car;
import Project.domain.Specs;
import Project.repository.SpecsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SpecsService {

    @Autowired
    SpecsRepository specsRepository;

    @Autowired
    CarService carService;

    @Cacheable(value = "specs")
    public List<Specs> getAllSpecs() {
        List<Specs> list = (List<Specs>) specsRepository.findAll();

        if (list.size() > 0) {
            return list;
        } else {
            return new ArrayList<Specs>();
        }
    }

    public Specs getSpecsById(Long id) throws RuntimeException {
        Optional<Specs> spec = specsRepository.findById(id);

        if (spec.isPresent()) {
            return spec.get();
        } else {
            throw new RuntimeException("No specs record exist for given id");
        }
    }

    @CacheEvict(value = "specs", allEntries = true)
    public void delete(Long id) throws RuntimeException {
        Optional<Specs> spec = specsRepository.findById(id);

        if (spec.isPresent()) {
            specsRepository.deleteById(id);
        } else {
            throw new RuntimeException("No specs record exist for given id");
        }
    }

    @CacheEvict(value = "specs", allEntries = true)
    public Specs createOrUpdateSpecs(Specs s, Boolean update) throws RuntimeException {

        Car car = carService.getCarById((Long) s.getCarid());
        if (car != null) {
            s.setCar(car);
        } else
            throw new RuntimeException("Car not found for the its car id");
        Optional<Specs> spec;

        if (update) {
            spec = specsRepository.findById(s.getId());
            if (spec.isPresent()) {
                Specs newEntity = spec.get();
                newEntity.setAbs(s.getAbs());
                newEntity.setAcceleration(s.getAcceleration());
                newEntity.setCar(s.getCar());
                newEntity.setCarid(s.getCarid());
                newEntity.setConsumption(s.getConsumption());
                newEntity.setDoors(s.getDoors());
                newEntity.setDrive(s.getDrive());
                newEntity.setFrontstabilizer(s.getFrontstabilizer());
                newEntity.setFueltype(s.getFueltype());
                newEntity.setPower(s.getPower());
                newEntity.setRearstabilizer(s.getRearstabilizer());
                newEntity.setTank(s.getTank());
                newEntity.setTopspeed(s.getTopspeed());
                newEntity.setTransmission(s.getTransmission());
                newEntity.setTurnangle(s.getTurnangle());
                newEntity = specsRepository.save(newEntity);
                return newEntity;
            } else {
                throw new RuntimeException("Specs not found for the given id");
            }
        } else {
            s = specsRepository.save(s);
            return s;
        }
    }

}
