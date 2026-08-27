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
 @Column(name = "idLignesCommande")
 private Long idLignesCommande;

 @Column(name = "quantite")
 private int  quantite;

 @Column(name = "prixunitaire")
 private BigDecimal prixunitaire;

 @Column(name = "remise")
 private BigDecimal remise;

    @ManyToOne
    @JoinColumn(name ="idBonCommande",nullable = false )
    private BonCommande bonCommande; 
    
    @ManyToOne
    @JoinColumn(name ="idArticles")
    private Articles articles;  
}
