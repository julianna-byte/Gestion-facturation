package Entity;



import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "BonCommande")

public class BonCommande {
    @Id
    @GeneratedValue(strategy =   GenerationType.IDENTITY)
    private Long idBonCommande;
    private String numeroBon;
    private double totalHT;
    private double Tva; 
    private double totalTtc;

    @ManyToOne
    @JoinColumn(name ="idClient")
    private Clients client;  

}
