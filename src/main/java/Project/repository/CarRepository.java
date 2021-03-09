package Project.repository;

import Project.domain.Car;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CarRepository extends CrudRepository<Car, Long> {

    Car findAllByparamid(Long paramid);
}
