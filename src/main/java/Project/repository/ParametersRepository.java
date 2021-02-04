package Project.repository;

import Project.domain.Parameters;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParametersRepository extends CrudRepository<Parameters, Long> {

    List<Parameters> findAllByOrderByIdAsc();

}
