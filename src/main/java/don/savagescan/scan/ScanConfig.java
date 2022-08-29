package don.savagescan.scan;

import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import don.savagescan.entity.CurrentServer;
import don.savagescan.repositories.CurrentServerRepository;
import don.savagescan.repositories.ServerRepository;
import don.savagescan.utils.IpRange;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@Data
public class ScanConfig {
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>(100_000);
    private final List<Ipv4Range> ipv4ReservedIps = new ArrayList<>();
    private final List<String> sshPasswords = new ArrayList<>();
    private final ServerRepository serverRepository;
    private final CurrentServerRepository currentServerRepository;
    private Ipv4 start = Ipv4.FIRST_IPV4_ADDRESS;
    private long current = start.asBigInteger().longValue();
    @Value("classpath:sshPasswords.txt")
    private Resource sshPasswordsFile;
    @Value("classpath:reservedIps.json")
    private Resource reservedIpsFile;

    public ScanConfig(ServerRepository serverRepository, CurrentServerRepository currentServerRepository) {
        this.serverRepository = serverRepository;
        this.currentServerRepository = currentServerRepository;
    }

    @PostConstruct
    public void init() throws IOException {
        sshPasswords.addAll(Files.readAllLines(sshPasswordsFile.getFile().toPath(), StandardCharsets.UTF_8));

        JsonReader reader = new JsonReader(new InputStreamReader(reservedIpsFile.getInputStream()));
        IpRange[] reservedIps = new Gson().fromJson(reader, IpRange[].class);

        Arrays.stream(reservedIps).forEach(range -> ipv4ReservedIps.add(Ipv4Range.from(range.getStart()).to(range.getEnd())));

        CurrentServer currentServer = currentServerRepository.findFirstByOrderByIdDesc();

        if (currentServer == null) {
            currentServer = currentServerRepository.save(new CurrentServer(start.toString()));
        }

        start = Ipv4.of(currentServer.getHost());

    }
}
