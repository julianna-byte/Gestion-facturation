package DTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LigneCommandeDto {

    private Long idLignesCommande;

    @NotNull(message = "La quantité est obligatoire")
    @Min(value =1,message = "La quantité doit etre au moins 1" )
    private Integer quantite;

    @NotNull(message = "L'article est obligatoire")
    private Long idArticles;

    private BigDecimal prixunitaire;

    private BigDecimal remise;



}
