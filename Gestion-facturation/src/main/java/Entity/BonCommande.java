package Entity;



import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "BonCommande")

public class BonCommande {
    @Id
    @GeneratedValue(strategy =   GenerationType.IDENTITY)
    @Column(name = "idBonCommande")
    private Long idBonCommande;

    @Column(name = "numeroBon")
    private String numeroBon;

    @Column(name = "totalHT")
    private double totalHT;

    @Column(name = "Tva")
    private double Tva; 

    @Column(name = "totalTtc")
    private double totalTtc;

    @ManyToOne
    @JoinColumn(name ="idClient")
    private Clients client;  

}
