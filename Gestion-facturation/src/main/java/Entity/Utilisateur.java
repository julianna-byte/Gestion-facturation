package Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Utilisateur")


public class Utilisateur {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "idUtilisateur")
    private Long idUtilisateur;

    @Column(name = "identifiant")
    private String identifiant;

    @Column(name = "motdepasse")
    private String motdepasse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

}
