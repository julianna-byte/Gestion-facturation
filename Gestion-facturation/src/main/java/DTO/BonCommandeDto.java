package DTO;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class BonCommandeDto {

    @NotNull(message = "Le client est obligatoire")
    private Long idClients;

    private Long idBonCommande;
    private String numeroBon;
    private double totalHT;
    private double Tva; 
    private double totalTtc;
    private String statut;

   @NotEmpty(message = "Le bon de commande doit contenir au moins une ligne")
    @Valid
    private List<LigneCommandeDto> lignes;

}
