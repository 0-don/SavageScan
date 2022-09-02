package don.savagescan.connector;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Data
@Component
@NoArgsConstructor
public class SSH {

    private final int port = 22;
    private String username = "root";
    private Session session;
    private String message = "";
    private String host;
    private String password;
    private List<String> sshPasswords;
    private boolean validSession = false;
    private boolean sshState = false;

    public SSH(List<String> sshPasswords) {
        this.sshPasswords = sshPasswords;
    }

    public void setHost(String host) {
        this.host = host;
        this.validSession = false;
        this.sshState = false;
        this.message = "";
    }

    public boolean tryConnections() {
        for (String password : sshPasswords) {
            this.password = password;

            connect();

            if (sshState && validSession) {
                System.out.println(this);
            }

            if (!validSession || sshState) {
                break;
            }
        }
        return sshState;
    }

    public void connect() {

        try (final SSHClient ssh = new SSHClient()) {
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.setTimeout(10000);
            ssh.setConnectTimeout(10000);
            ssh.connect(host, port);
            ssh.authPassword(username, password);
            validSession = ssh.isAuthenticated();

            try (Session session = ssh.startSession()) {

                final Session.Command cmd = session.exec("ssh -V");

                message = IOUtils.readFully(cmd.getInputStream()).toString().toLowerCase() + IOUtils.readFully(cmd.getErrorStream()).toString().toLowerCase();

                sshState = message.contains("openssh");

            } catch (IOException ignored) {
            }
        } catch (IOException e) {
            if (e.getMessage() != null) {
                message = e.getMessage();
                validSession = e.getMessage().contains("Exhausted available authentication methods");
            }

        }
    }

    @Override
    public String toString() {
        return "ssh " + username + "@" + host + " Password: " + password + "\n" + message;
    }
}
