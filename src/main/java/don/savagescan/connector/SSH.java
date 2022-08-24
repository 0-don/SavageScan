package don.savagescan.connector;

import lombok.Data;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
@Component
public class SSH {
    private final String username = "root";
    private final int port = 22;
    long defaultTimeoutSeconds = 10;
    SshClient client = SshClient.setUpDefaultClient();
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
    }

    public boolean tryConnections() {
        for (String password : sshPasswords) {
            this.password = password;
            connect();

            if (!this.validSession || sshState) {
                break;
            }
        }
        return sshState;
    }


    public void connect() {

        SshClient client = SshClient.setUpDefaultClient();
        client.start();


        try (ClientSession session = client.connect(username, host, port).verify().getSession()) {
            session.addPasswordIdentity(password);
            session.auth().verify(defaultTimeoutSeconds, TimeUnit.SECONDS);
            this.sshState = session.isAuthenticated();
            session.close(true);
        } catch (Throwable e) {
            message = e.getMessage();
            validSession = message.contains("No more authentication methods available");
        } finally {
            client.stop();
        }
        System.out.println(message + " " + this);
//        if (validSession) {
//
//        }
    }

    @Override
    public String toString() {
        return "ssh " + username + ":" + password + "@" + host;
    }
}
