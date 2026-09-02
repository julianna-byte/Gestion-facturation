package DTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;


@Data

public class ArticleDto {

    private Long idArticles;

    @NotBlank(message = "Le code est obligatoire")
    private String code;

    @NotBlank(message = "Le libellé est obligatoire")
    private String libelle;

    private String description;

    @DecimalMin(value = "0.0", message = "La marge commerciale ne peut pas être négative")
    private BigDecimal margeCommerciale;

    @NotNull(message = "L'unité est obligatoire")
    private Integer unite;

    @NotNull(message = "Le taux de TVA est obligatoire")
    @DecimalMin(value = "0.0", message = "Le taux de TVA ne peut pas être négatif")
    private BigDecimal tauxTva;

    @NotNull(message = "Le prix unitaire HT est obligatoire")
    @DecimalMin(value = "0.0", message = "Le prix unitaire ne peut pas être négatif")
    private BigDecimal prixunitaireHT;
}
