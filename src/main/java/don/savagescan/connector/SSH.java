package don.savagescan.connector;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.sshtools.client.SshClient;
import com.sshtools.common.ssh.SshException;
import lombok.Data;

import java.io.IOException;
import java.util.List;

@Data
public class SSH {
    private final String username = "root";
    private final int port = 22;
    private String host;
    private String password;
    private List<String> sshPasswords;
    private boolean validSession = false;
    private boolean sshState = false;
    private Session session = null;
    private JSch jsch = new JSch();

    public SSH(List<String> sshPasswords) {
        this.sshPasswords = sshPasswords;
    }

    public void setHost(String host) {
        this.host = host;
        this.validSession = false;
    }

    public boolean tryConnections() {
        for (String password : sshPasswords) {
            this.password = password;
            connect();
            System.out.println(this);
            if (!this.validSession || sshState) {
                break;
            }
        }
        return sshState;
    }

    public void connect() {
        try (SshClient ssh = new SshClient(host, port, username, password.toCharArray())) {
            sshState = ssh.isConnected();
        } catch (IOException | SshException e) {
            validSession = e.getMessage().contains("Authentication failed");
        }
    }

    @Override
    public String toString() {
        return "ssh " + username + ":" + password + "@" + host;
    }
}
