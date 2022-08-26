package don.savagescan.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @Column(name = "host", length = 15, nullable = false, unique = true)
    private String host;


    @CreationTimestamp
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @UpdateTimestamp
    @Column(name = "modify_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "server", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<ServerService> serverServices = new ArrayList<>();

    public Server(String host) {
        this.host = host;
    }


    public void addServerService(ServerService serverService) {
        this.serverServices.add(serverService);
        serverService.setServer(this);
    }

}
