package don.savagescan.repositories;

import don.savagescan.entity.CurrentServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentServerRepository extends JpaRepository<CurrentServer, Long> {

    CurrentServer findFirstByOrderByIdDesc();
}
