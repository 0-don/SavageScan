package don.savagescan.repositories;

import don.savagescan.entity.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Settings, Long> {

    Settings findFirstByOrderByIdDesc();
}
