package don.savagescan.connector;

import don.savagescan.entity.Server;
import don.savagescan.entity.ServerService;
import don.savagescan.model.ServiceName;
import don.savagescan.scan.ScanConfig;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


@Data
@Component
@Scope("prototype")
@RequiredArgsConstructor
public class FTP {

    private final int port = 21;
    private final ScanConfig scanConfig;
    private String username = "";
    private String host = "";
    private String password = "";
    private boolean validSession = false;
    private boolean validFtp = false;

    public void setHost(String host) {
        this.host = host;
        this.validSession = false;
        this.validFtp = false;
    }

    public void tryConnections() {
        for (String access : scanConfig.getFtpAccesses()) {

            String[] splitAccess = access.split(":");
            username = splitAccess[0];
            password = splitAccess[1];

            connect();

            if (!validSession || validFtp) {
                if (validFtp) save();
                break;
            }
        }
    }

    public void connect() {

        String ftpUrl = "ftp://" + username + ":" + password + "@" + host + ":" + port;

        try {
            URLConnection ftpConnection = new URL(ftpUrl).openConnection();
            ftpConnection.setConnectTimeout(10000);
            ftpConnection.setReadTimeout(10000);

            InputStream inputStream = ftpConnection.getInputStream();
            inputStream.close();
            validFtp = true;

        } catch (IOException e) {
            String message = e.getMessage();
            if (message != null && message.contains("Invalid username/password")) {
                validSession = true;
            }
        }
    }

    private void save() {
        Server checkServer = scanConfig.getServerRepository().findByHost(host);

        if (checkServer == null) {
            Server server = new Server(getHost());
            ServerService serverService = new ServerService(ServiceName.FTP, getUsername(), getPassword(), getPort());

            server.addServerService(serverService);
            scanConfig.getServerRepository().save(server);
        } else {
            ServerService checkServerService = checkServer.getServerServices().stream().filter(s -> s.getServiceName().equals(ServiceName.FTP)).findFirst().orElse(null);

            if (checkServerService == null) {
                ServerService serverService = new ServerService(ServiceName.FTP, getUsername(), getPassword(), getPort());
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
        return "ftp://" + username + ":" + password + "@" + host + ":" + port;
    }
}