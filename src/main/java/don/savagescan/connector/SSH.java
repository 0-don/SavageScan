package don.savagescan.connector;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.Data;

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

//            System.out.println(this + " sshState:" + sshState);

            if (!this.validSession || sshState) {
                break;
            }
        }
        return sshState;
    }

    public void connect() {
        try {
            session = jsch.getSession(username, host, port);
            session.setTimeout(10000);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
        } catch (JSchException e) {
            String msg = e.getMessage();
            // server doesn't exist or ssh port is changed
            this.validSession = !(msg.contains("Connection refused: connect") || msg.contains("Network is unreachable: connect"));
            // wrong login or password
            sshState = false;
        } finally {
            if (session != null) {
                if (session.isConnected()) {
                    System.out.println("connected");
                    sshState = true;
                }
                session.disconnect();
            }
        }
    }

    @Override
    public String toString() {
        return "ssh " + username + ":" + password + "@" + host;
    }
}
