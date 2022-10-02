package don.savagescan.scan;

import com.github.jgonian.ipmath.Ipv4;
import don.savagescan.connector.FTP;
import don.savagescan.connector.SSH;
import don.savagescan.entity.CurrentServer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class ScanConsumer implements Runnable {

    private final ScanConfig scanConfig;

    private final ApplicationContext applicationContext;

    private final BlockingQueue<String> queue;

    @Override
    public void run() {
        FTP ftp = applicationContext.getBean(FTP.class);
        SSH ssh = applicationContext.getBean(SSH.class);


        while (scanConfig.getCurrent() < Ipv4.LAST_IPV4_ADDRESS.asBigInteger().longValue()) {
            try {
                String ip = queue.take();

                ftp.setHost(ip);
                ftp.tryConnections();

                ssh.setHost(ip);
                ssh.tryConnections();

                setCurrentIp(ip);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setCurrentIp(String ip) {
        if (scanConfig.getCurrent() < Ipv4.of(ip).asBigInteger().longValue()) {
            scanConfig.setCurrent(Ipv4.of(ip).asBigInteger().longValue());
            CurrentServer currentServer = scanConfig.getCurrentServerRepository().findFirstByOrderByIdDesc();
            currentServer.setHost(ip);
            scanConfig.getCurrentServerRepository().save(currentServer);
        }
    }
}
