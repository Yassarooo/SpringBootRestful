package Project.repository;

import Project.domain.Parameters;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ParametersRepository extends CrudRepository<Parameters, Long> {
    Optional<Parameters> findByName(String name);
}
