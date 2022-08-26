package don.savagescan.repositories;

import don.savagescan.entity.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {

    Server findFirstByOrderByIdDesc();


}
