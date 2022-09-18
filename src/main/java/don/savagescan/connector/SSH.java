package don.savagescan.connector;

import don.savagescan.entity.Server;
import don.savagescan.entity.ServerService;
import don.savagescan.model.ServiceName;
import don.savagescan.scan.ScanConfig;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Data
@Component
@Scope("prototype")
@RequiredArgsConstructor
public class SSH {
    private final int port = 22;
    private final ScanConfig scanConfig;
    private String username = "root";
    private String message;
    private String host;
    private String password;
    private boolean validSession = false;
    private boolean validSsh = false;

    public void setHost(String host) {
        this.host = host;
        this.validSession = false;
        this.validSsh = false;
        this.message = "";
    }

    public void tryConnections() {
        for (String password : scanConfig.getSshPasswords()) {
            setPassword(password);

            connect();

            if (!validSession || validSsh) {
                if (validSsh) save();
                break;
            }
        }
    }

    public void connect() {

        try (final SSHClient ssh = new SSHClient()) {
            System.out.println(this);
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.setTimeout(10000);
            ssh.setConnectTimeout(10000);
            ssh.connect(host, port);
            ssh.authPassword(username, password);
            validSession = ssh.isAuthenticated();

            try (Session session = ssh.startSession()) {
                final Session.Command cmd = session.exec("ssh -V");
                message = IOUtils.readFully(cmd.getInputStream()).toString().toLowerCase() + IOUtils.readFully(cmd.getErrorStream()).toString().toLowerCase();
                validSsh = message.contains("openssh");
            } catch (IOException ignored) {
            }
        } catch (IOException e) {
            if (e.getMessage() != null) {
                message = e.getMessage();
                validSession = e.getMessage().contains("Exhausted available authentication methods");
            }
        }
    }

    public void save() {
        Server checkServer = scanConfig.getServerRepository().findByHost(host);

        if (checkServer == null) {
            Server server = new Server(getHost());
            ServerService serverService = new ServerService(ServiceName.SSH, getUsername(), getPassword(), getPort());

            server.addServerService(serverService);
            scanConfig.getServerRepository().save(server);
        } else {
            ServerService checkServerService = checkServer.getServerServices().stream().filter(s -> s.getServiceName().equals(ServiceName.SSH)).findFirst().orElse(null);

            if (checkServerService == null) {
                ServerService serverService = new ServerService(ServiceName.SSH, getUsername(), getPassword(), getPort());
                checkServer.addServerService(serverService);
                scanConfig.getServerRepository().save(checkServer);
            } else {
                checkServerService.setUsername(getUsername());
                checkServerService.setPassword(getPassword());
                checkServerService.setPort(getPort());
                scanConfig.getServerRepository().save(checkServer);
            }
        }
    }

    @Override
    public String toString() {
        return "ssh " + username + "@" + host + " Password: " + password + "\n" + message;
    }
}
