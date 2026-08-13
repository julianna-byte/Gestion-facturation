package Entity;


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
    private Double TauxTva;
    private String description;
    private Double prixunitaireHT;




}
