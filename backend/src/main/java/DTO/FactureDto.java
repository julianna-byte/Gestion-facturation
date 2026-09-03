package DTO;


import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class FactureDto {

    private Long idFacture;
    private String numerofacture;
    private String type;
    private String statut;
    private LocalDate dateCreation;
    private BigDecimal totalTtc;
    private BigDecimal montantPaye;
    private BigDecimal resteAPayer;
    private Long idBonCommande;
    private Long idClient;
    private List<ReglementDto> reglements;
    private String nomClient;
    private String ConditionsPersonnalisees;

    //tracabilite
    private String auteur;
}
