package bouraouiechaib.space.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.bytecode.enhance.spi.EnhancementInfo;

@Entity @Getter
@Setter
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id", nullable = false)
    private Long user_id;

    private String username;

    private String email;
}
