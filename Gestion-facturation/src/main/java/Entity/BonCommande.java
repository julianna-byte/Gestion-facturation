package Entity;




import java.time.LocalDate;
import java.util.List;

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

    @Column(name = "dateCreation")
    private LocalDate dateCreation;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutBonCommande statut;

    @ManyToOne
    @JoinColumn(name = "idClient")
    private Clients client;

    @ManyToOne
    @JoinColumn(name = "idUtilisateur")
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "bonCommande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LignesCommande> lignes;

    
    

}
