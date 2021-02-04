package Project.repository;

import Project.domain.Specs;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecsRepository extends CrudRepository<Specs, Long> {

}
