package Entity;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "LignesCommande")

public class LignesCommande {
 @Id
 @GeneratedValue(strategy =  GenerationType.IDENTITY)
 private Long idLignesCommande;
 private int  quantite;
 private BigDecimal prixunitaire;
 private BigDecimal remise;

    @ManyToMany
    @JoinColumn(name ="idBonCommande")
    private BonCommande BonCommande; 
    
    @ManyToMany
    @JoinColumn(name ="idArticles")
    private Articles Articles;  
}
