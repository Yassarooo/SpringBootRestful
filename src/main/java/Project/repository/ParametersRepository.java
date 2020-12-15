package Project.repository;

import Project.domain.Parameters;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametersRepository extends CrudRepository<Parameters, Long> {

}
