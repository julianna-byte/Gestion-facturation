package Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Lignes")

public class Lignes {
 @Id
 @GeneratedValue(strategy =  GenerationType.IDENTITY)
 private Long idLignes;
 private int  quantite;
 private double prixunitaire;
 private double remise;

    @ManyToMany
    @JoinColumn(name ="idBonCommande")
    private BonCommande BonCommande; 
    
    @ManyToMany
    @JoinColumn(name ="idArticles")
    private Articles Articles;  
}
