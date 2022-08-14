package don.savagescan.entity;

import com.github.jgonian.ipmath.Ipv4;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class CurrentServer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @Column(name = "host", length = 15, nullable = false, unique = true)
    private String host;

    private long ipDecimal;

    public CurrentServer() {

    }

    public CurrentServer(String host) {
        this.host = host;
    }

    @PrePersist
    public void prePersist() {
        this.ipDecimal = Ipv4.of(this.host).asBigInteger().longValue();
    }


}
