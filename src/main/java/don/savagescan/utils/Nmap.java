package don.savagescan.utils;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class Nmap {
    public Future<Boolean> portIsOpen(final ExecutorService es, final String ip, final int port, final int timeout) {
        return es.submit(() -> {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(ip, port), timeout);
                InputStream in = socket.getInputStream();
                byte[] bs = new byte[1000];
                int len = in.read(bs);
                String serverInfo = new String(bs, 0, len);
                System.out.println(serverInfo);
                socket.close();
                return true;
            } catch (Exception ex) {
                return false;
            }
        });
    }

    public void scan() {
        final ExecutorService es = Executors.newFixedThreadPool(10000);
        final String ip = "";
        final int timeout = 50;
        final List<Future<Boolean>> futures = new ArrayList<>();
        for (int port = 1; port <= 65535; port++) {
            futures.add(portIsOpen(es, ip, port, timeout));
        }
        es.shutdown();
        int openPorts = 0;
        for (final Future<Boolean> f : futures) {
            try {
                if (f.get()) openPorts++;
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("There are " + openPorts + " open ports on host " + ip + " (probed with a timeout of " + timeout + "ms)");
    }
}
