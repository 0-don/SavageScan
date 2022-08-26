package don.savagescan.scan;

import don.savagescan.connector.SSH;
import don.savagescan.entity.Server;
import don.savagescan.entity.ServerService;
import don.savagescan.model.ServiceName;
import don.savagescan.repositories.ServerRepository;
import don.savagescan.repositories.ServerServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class ScanCheck {

    private final ServerRepository serverRepository;

    private final ServerServiceRepository serverServiceRepository;

    private final SSH ssh;

    @Transactional(readOnly = true)
    public void check() {


//        List<ServerService> serverServices = serverServiceRepository.findAll();
//
//        for (ServerService serverService : serverServices) {
//            System.out.println(serverService);
//        }

        List<Server> servers = serverRepository.findAll();

        servers.forEach(server -> {
            ServerService sshServerService = server.getServerServices().stream()
                    .filter(serverService -> serverService.getServiceName().equals(ServiceName.SSH))
                    .findFirst().orElse(null);


            if(sshServerService != null) {
                ssh.setHost(server.getHost());
                ssh.setPassword(sshServerService.getPassword());
                ssh.connect();

                if(!ssh.isSshState() && !ssh.isValidSession()) {
//                    serverRepository.de
                    System.out.println("deleted");
                }

                System.out.println(ssh);
            }
        });
    }
}
