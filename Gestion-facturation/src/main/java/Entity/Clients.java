package Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Clients")

public class Clients {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long idClient;

    private String raisonsociale;
    private String NIF;
    private String RCCM;
    private String adresse;
    private String ville;
    private String pays;
    private String telephone;
    private String email;
    private String nomcontact;
    private Boolean actif;









}
