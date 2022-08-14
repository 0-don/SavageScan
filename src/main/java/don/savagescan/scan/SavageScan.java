package don.savagescan.scan;

import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import don.savagescan.utils.IpRange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class SavageScan {

    private static final ExecutorService pool = Executors.newCachedThreadPool();
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private final List<Ipv4Range> ipv4Ranges = new ArrayList<>();
    private List<String> sshPasswords = new ArrayList<>();
    @Value("classpath:sshPasswords.txt")
    private Resource sshPasswordsFile;
    @Value("classpath:reservedIps.json")
    private Resource reservedIpsFile;

    public void init() throws IOException {
        Charset charset = StandardCharsets.UTF_8;
        sshPasswords = Files.readAllLines(sshPasswordsFile.getFile().toPath(), charset);

        JsonReader reader = new JsonReader(new InputStreamReader(reservedIpsFile.getInputStream()));
        IpRange[] reservedIps = new Gson().fromJson(reader, IpRange[].class);

        Arrays.stream(reservedIps).forEach(range ->
                ipv4Ranges.add(Ipv4Range.from(range.getStart()).to(range.getEnd())));
    }

    public void start() throws IOException {
        init();

        Ipv4 start = Ipv4.FIRST_IPV4_ADDRESS;

        SSHProducer sshProducer = new SSHProducer(queue, ipv4Ranges, start);
        pool.execute(sshProducer);

        for (int i = 0; i < 10000; i++) {
            SSHConsumer consumer = new SSHConsumer(queue, sshPasswords);
            pool.execute(consumer);
        }

    }
}
