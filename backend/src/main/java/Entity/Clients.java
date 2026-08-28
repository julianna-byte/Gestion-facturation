package Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "Clients")

public class Clients extends Auditable {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "idClient")
    private Long idClient;

    @Column(name = "raisonsociale")
    private String raisonsociale;

    @Column(name = "NIF")
    private String NIF;

    @Column(name = "RCCM" , length = 30)
    private String RCCM;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "ville")
    private String ville;

    @Column(name = "pays")
    private String pays;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "email")
    private String email;

    @Column(name = "nomcontact")
    private String nomcontact;

    @Column(name = "actif")
    private Boolean actif;









}
