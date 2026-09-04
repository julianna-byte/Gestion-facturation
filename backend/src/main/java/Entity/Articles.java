package Entity;


import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "Articles")

public class Articles extends Auditable {

    @Id
    @GeneratedValue(strategy =   GenerationType.IDENTITY)
    @Column(name = "idarticles")
    private Long idArticles;

    @Column(name = "code")
    private String code;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "unite")
    private int unite;

    @Column(name = "Tauxtva")
    private BigDecimal TauxTva;

    @Column(name = "description")
    private String description;

    @Column(name = "prixunitaireHT")
    private BigDecimal prixunitaireHT;

    @Column(name = "margeCommerciale")
    private BigDecimal margeCommerciale;




}
