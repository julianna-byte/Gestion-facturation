package DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReglementDto {

    private Long idReglement;

    @NotNull(message = "Le montant est obligatoire")
    private BigDecimal montant;

    @NotBlank(message = "Le mode de règlement  est obligatoire")
    private String mode;

    private LocalDate dateReglement;




}
