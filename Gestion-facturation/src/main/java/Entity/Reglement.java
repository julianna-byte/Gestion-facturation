package Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "Reglement")
public class Reglement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idreglement")
    private Long idReglement;

    @Column(name = "datereglement", nullable = false)
    private LocalDate dateReglement;

    @Column(name = "montant", nullable = false)
    private BigDecimal montant;

    @Column(name = "mode", nullable = false)
    private String mode; //"Espèces", "Virement", "Chèque", "Mobile Money"

    @ManyToOne
    @JoinColumn(name = "idfacture", nullable = false)
    private Facture facture;
}
