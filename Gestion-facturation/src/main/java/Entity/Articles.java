package Entity;


import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Articles")

public class Articles {

    @Id
    @GeneratedValue(strategy =   GenerationType.IDENTITY)
    private Long idArticles;

    private String code;
    private String libelle;
    private int unite;
    private BigDecimal TauxTva;
    private String description;
    private BigDecimal prixunitaireHT;




}
