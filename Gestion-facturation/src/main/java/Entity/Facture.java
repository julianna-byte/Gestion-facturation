package Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
@Entity
@Data
@Table(name = "Facture")

public class Facture {

    
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "idFacture")
    private Long idFacture;

    @Column(name = "numerofacture")
    private String numerofacture;

    @Enumerated(EnumType.STRING)
    @Column(name="type", nullable = false)
    private TypeFacture type;

   @Enumerated(EnumType.STRING)
   @Column(name = "statut")
   private StatutFacture statut;

    @Column(name = "totalttc")
    private BigDecimal totalTtc;

    @Column(name = "datecreation")
    private LocalDate dateCreation;

    @Column(name = "motifannulation")
    private String motifAnnulation;

    @ManyToOne
    @JoinColumn(name ="idBonCommande")
    private BonCommande BonCommande; 
    
    @ManyToOne
    @JoinColumn(name ="idUtilisateur")
    private Utilisateur Utilisateur;  

    @ManyToOne
    @JoinColumn(name ="idClients")
    private Clients Clients;  

    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Reglement> reglements;
}
