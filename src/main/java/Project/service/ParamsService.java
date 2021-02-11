package Project.service;

import Project.domain.Parameters;
import Project.repository.ParametersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParamsService {

    @Autowired
    ParametersRepository paramsRepository;

    @Cacheable(value = "params")
    public List<Parameters> getAllParams() {
        List<Parameters> list = (List<Parameters>) paramsRepository.findAll();

        if (list.size() > 0) {
            return list;
        } else {
            return new ArrayList<Parameters>();
        }
    }

    public Parameters getParamById(Long id) throws RuntimeException {
        Optional<Parameters> param = paramsRepository.findById(id);

        if (param.isPresent()) {
            return param.get();
        } else {
            throw new RuntimeException("No params record exist for given id");
        }
    }
    public Parameters getParamByName(String name) throws RuntimeException {
        Optional<Parameters> param = paramsRepository.findByName(name);

        if (param.isPresent()) {
            return param.get();
        } else {
            throw new RuntimeException("No params record exist for given name");
        }
    }

    @CacheEvict(value = "params", allEntries = true)
    public void delete(Long id) throws RuntimeException {
        Optional<Parameters> param = paramsRepository.findById(id);

        if (param.isPresent()) {
            paramsRepository.deleteById(id);
        } else {
            throw new RuntimeException("No params record exist for given id");
        }
    }

    @CacheEvict(value = "params", allEntries = true)
    public Parameters createOrUpdateParam(Parameters c, Boolean update) throws RuntimeException {
        Optional<Parameters> param;
        if (update) {
            param = paramsRepository.findById(c.getId());
            if (param.isPresent()) {
                Parameters newEntity = param.get();
                newEntity.setSeats(c.getSeats());
                newEntity.setName(c.getName());
                newEntity.setPercentage(c.getPercentage());
                newEntity = paramsRepository.save(newEntity);

                return newEntity;
            } else {
                c = paramsRepository.save(c);
                return c;
            }
        } else {
            c = paramsRepository.save(c);

            return c;
        }
    }


}
