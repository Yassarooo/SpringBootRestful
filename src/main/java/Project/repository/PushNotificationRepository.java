package Project.repository;


import Project.domain.PushNotificationRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PushNotificationRepository extends CrudRepository<PushNotificationRequest, Long> {
    PushNotificationRequest findByCarid(String carid);
}
