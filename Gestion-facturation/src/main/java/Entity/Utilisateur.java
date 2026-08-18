package Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Utilisateur")


public class Utilisateur {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long idUtilisateur;

    private String identifiant;
    private String motdepasse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

}
