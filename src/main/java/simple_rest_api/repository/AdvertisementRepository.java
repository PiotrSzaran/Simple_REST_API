package simple_rest_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import simple_rest_api.model.Advertisement;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

}
