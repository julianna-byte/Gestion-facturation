package Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Facture")

public class Facture {

    
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long idFacture;
    private String numerofacture;

     @OneToOne
    @JoinColumn(name ="idBonCommande")
    private BonCommande BonCommande; 
    
    @ManyToOne
    @JoinColumn(name ="idUtilisateur")
    private Utilisateur Utilisateur;  

     @ManyToOne
    @JoinColumn(name ="idClients")
    private Clients Clients;  
}
