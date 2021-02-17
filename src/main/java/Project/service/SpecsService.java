package Project.service;

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
    public Specs createOrUpdateSpecs(Specs c, Boolean update) throws RuntimeException {
        Optional<Specs> spec;
        if (update) {
            spec = specsRepository.findById(c.getId());
            if (spec.isPresent()) {
                Specs newEntity = spec.get();
                newEntity.setAbs(c.getAbs());
                newEntity.setAcceleration(c.getAcceleration());
                newEntity.setCar(c.getCar());
                newEntity.setCarid(c.getCarid());
                newEntity.setConsumption(c.getConsumption());
                newEntity.setDoors(c.getDoors());
                newEntity.setDrive(c.getDrive());
                newEntity.setFrontstabilizer(c.getFrontstabilizer());
                newEntity.setFueltype(c.getFueltype());
                newEntity.setPower(c.getPower());
                newEntity.setRearstabilizer(c.getRearstabilizer());
                newEntity.setTank(c.getTank());
                newEntity.setTopspeed(c.getTopspeed());
                newEntity.setTransmission(c.getTransmission());
                newEntity.setTurnangle(c.getTurnangle());
                newEntity = specsRepository.save(newEntity);
                return newEntity;
            } else {
                c = specsRepository.save(c);
                return c;
            }
        } else {
            c = specsRepository.save(c);

            return c;
        }
    }


}
