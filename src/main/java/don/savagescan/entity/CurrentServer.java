package don.savagescan.entity;

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

    public CurrentServer() {

    }

    public CurrentServer(String host) {
        this.host = host;
    }

}
