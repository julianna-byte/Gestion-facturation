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
    
    @ManyToMany
    @JoinColumn(name ="idUtilisateur")
    private Utilisateur Utilisateur;  

     @ManyToMany
    @JoinColumn(name ="idClients")
    private Clients Clients;  
}
