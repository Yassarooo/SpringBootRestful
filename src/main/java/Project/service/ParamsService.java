package Project.service;

import Project.domain.Car;
import Project.domain.Parameters;
import Project.repository.CarRepository;
import Project.repository.ParametersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParamsService {

    @Autowired
    ParametersRepository paramsRepository;

    public Parameters getParamsById(Long id) throws RuntimeException {
        Optional<Parameters> param = paramsRepository.findById(id);

        if (param.isPresent()) {
            return param.get();
        } else {
            throw new RuntimeException("No params record exist for given id");
        }
    }

    public Parameters createOrUpdateParams(Parameters c) throws RuntimeException {
        Optional<Parameters> params = paramsRepository.findById(c.getId());
        if (params.isPresent()) {
            Parameters newEntity = params.get();
            newEntity.setSeats(c.getSeats());
            newEntity.setPercentage(c.getPercentage());
            newEntity.setId(c.getId());
            newEntity = paramsRepository.save(newEntity);

            return newEntity;
        } else {
            c = paramsRepository.save(c);
            return c;
        }
    }


}
