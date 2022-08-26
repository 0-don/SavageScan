package don.savagescan.repositories;

import don.savagescan.entity.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {

    Server findFirstByOrderByIdDesc();

    @Query("SELECT DISTINCT server FROM Server server JOIN FETCH server.serverServices serverService")
    List<Server> findServerServices();
}
