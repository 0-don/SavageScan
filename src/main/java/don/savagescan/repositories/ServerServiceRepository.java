package don.savagescan.repositories;

import don.savagescan.entity.ServerService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerServiceRepository extends JpaRepository<ServerService, Long> {
}
