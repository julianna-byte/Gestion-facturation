package DTO;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BonCommandeDto {

    @NotNull(message = "Le client est obligatoire")
    private Long idClients;

    private String nomClient;
    private Long idBonCommande;
    private String numeroBon;
    private BigDecimal totalHT;
    private BigDecimal Tva; 
    private BigDecimal totalTtc;
    private String statut;

   @NotEmpty(message = "Le bon de commande doit contenir au moins une ligne")
    @Valid
    private List<LigneCommandeDto> lignes;

}
