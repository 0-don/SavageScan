package don.savagescan.connector;

import lombok.Data;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.channel.Channel;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.EnumSet;
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

        try (ClientSession session = client.connect(username, host, port).verify(defaultTimeoutSeconds, TimeUnit.SECONDS).getSession()) {
            session.addPasswordIdentity(password);
            session.auth().verify(defaultTimeoutSeconds, TimeUnit.SECONDS);
            this.validSession = session.isAuthenticated();
            try (ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
                 ClientChannel channel = session.createChannel(Channel.CHANNEL_SHELL)) {
                channel.setOut(responseStream);
                try {
                    String responseString = runCommand(responseStream, channel, defaultTimeoutSeconds);
                    this.sshState = responseString.toLowerCase().contains("open");
                } finally {
                    channel.close(false);
                }
            }
            session.close(true);
        } catch (Throwable e) {
            message = e.getMessage();
            validSession = message.contains("No more authentication methods available");
        } finally {
            client.stop();
        }

        if (validSession) {
            System.out.println(this);
        }
    }

    public String runCommand(ByteArrayOutputStream responseStream, ClientChannel channel, long defaultTimeoutSeconds) throws java.io.IOException {
        channel.open().verify(defaultTimeoutSeconds, TimeUnit.SECONDS);
        try (OutputStream pipedIn = channel.getInvertedIn()) {
            pipedIn.write("ssh -V \n".getBytes());
            pipedIn.flush();
        }

        channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED),
                TimeUnit.SECONDS.toMillis(defaultTimeoutSeconds));
        return responseStream.toString();
    }

    @Override
    public String toString() {
        return "ssh " + username + ":" + password + "@" + host;
    }
}
