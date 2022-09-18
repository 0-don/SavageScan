package don.savagescan.scan;

import don.savagescan.connector.SSH;
import don.savagescan.entity.Server;
import don.savagescan.entity.ServerService;
import don.savagescan.model.ServiceName;
import don.savagescan.repositories.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScanCheck {

    private final ServerRepository serverRepository;

    private final SSH ssh;

    public void check() {
        List<Server> servers = serverRepository.findServerServices();

        for (Server server : servers) {
            ServerService serverService = server.getServerServices().stream()
                    .filter(service -> service.getServiceName().equals(ServiceName.SSH)).findFirst().orElse(null);

            if (serverService != null) {
                ssh.setHost(server.getHost());
                ssh.setPassword(serverService.getPassword());

                ssh.connect();

                if (!ssh.isValidSsh() && !ssh.isValidSession()) {
                    serverRepository.deleteById(server.getId());
                }

            } else {
                serverRepository.deleteById(server.getId());
            }
        }
    }


}
