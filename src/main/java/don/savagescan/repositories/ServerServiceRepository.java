package don.savagescan.repositories;

import don.savagescan.entity.Server;
import don.savagescan.entity.ServerService;
import don.savagescan.model.ServiceName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerServiceRepository extends JpaRepository<ServerService, Long> {
    ServerService findByServerAndServiceName(Server server, ServiceName ssh);
}
