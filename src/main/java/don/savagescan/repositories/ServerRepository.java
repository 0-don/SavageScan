package don.savagescan.repositories;

import don.savagescan.entity.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepository extends JpaRepository<Server, Long> {

    Server findFirstOrderByIdDesc();
}
