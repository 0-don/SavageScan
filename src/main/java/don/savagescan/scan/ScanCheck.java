package don.savagescan.scan;

import don.savagescan.connector.SSH;
import don.savagescan.entity.Server;
import don.savagescan.entity.ServerService;
import don.savagescan.model.ServiceName;
import don.savagescan.repositories.ServerRepository;
import don.savagescan.repositories.ServerServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScanCheck {

    private final ServerRepository serverRepository;

    private final ServerServiceRepository serverServiceRepository;

    private final SSH ssh;


    public void check() {
        List<Server> servers = serverRepository.findAll();

        for (Server server : servers) {
            ServerService serverService = serverServiceRepository.findByServerAndServiceName(server, ServiceName.SSH);

            if (serverService != null) {
                ssh.setHost(server.getHost());
                ssh.setPassword(serverService.getPassword());
                ssh.connect();
                if (!ssh.isSshState() && !ssh.isValidSession()) {
                    serverRepository.deleteById(server.getId());
                }
                System.out.println(ssh);
            }
        }
    }


}
