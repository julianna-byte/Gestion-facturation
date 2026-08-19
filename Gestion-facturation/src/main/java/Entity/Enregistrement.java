package Entity;

import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Enregistrement")


public class Enregistrement {

    
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "idEnregistrement")
    private Long idEnregistrement;

    @Column(name = "datecreation")
    private Date datecreation;

    @Column(name = "datemodification")
    private Date datemodification;

    @Column(name = "auteur")
    private String auteur;

     @ManyToOne
    @JoinColumn(name ="idBonCommande")
    private BonCommande BonCommande; 
    
    @ManyToOne
    @JoinColumn(name ="idFacture")
    private Facture facture;  

     @ManyToOne
    @JoinColumn(name ="idClients")
    private Clients Clients;  

}
