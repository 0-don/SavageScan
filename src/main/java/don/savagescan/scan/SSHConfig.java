package don.savagescan.scan;

import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import don.savagescan.repositories.ServerRepository;
import don.savagescan.utils.IpRange;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@Data
public class SSHConfig {
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private final List<Ipv4Range> ipv4ReservedIps = new ArrayList<>();
    private final List<String> sshPasswords = new ArrayList<>();
    private final ServerRepository serverRepository;
    private Ipv4 start = Ipv4.FIRST_IPV4_ADDRESS;
    private Ipv4 current = start;
    @Value("classpath:sshPasswords.txt")
    private Resource sshPasswordsFile;
    @Value("classpath:reservedIps.json")
    private Resource reservedIpsFile;

    public SSHConfig(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    @PostConstruct
    public void init() throws IOException {
        Charset charset = StandardCharsets.UTF_8;
        sshPasswords.addAll(Files.readAllLines(sshPasswordsFile.getFile().toPath(), charset));

        JsonReader reader = new JsonReader(new InputStreamReader(reservedIpsFile.getInputStream()));
        IpRange[] reservedIps = new Gson().fromJson(reader, IpRange[].class);

        Arrays.stream(reservedIps).forEach(range ->
                ipv4ReservedIps.add(Ipv4Range.from(range.getStart()).to(range.getEnd())));
    }
}
