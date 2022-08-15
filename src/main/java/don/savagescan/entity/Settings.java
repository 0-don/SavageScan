package don.savagescan.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    private int threads;

    public Settings() {

    }

    public Settings(int threads) {
        this.threads = threads;
    }


}
