package Mapper;

import DTO.FactureDto;
import DTO.ReglementDto;
import Entity.Facture;
import Entity.Reglement;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component

public class FactureMapper {

    public FactureDto toDTO(Facture facture) {
        FactureDto dto = new FactureDto();
        dto.setIdFacture(facture.getIdFacture());
        dto.setNumerofacture(facture.getNumerofacture());
        dto.setType(facture.getType().name());
        dto.setStatut(facture.getStatut().name());
    
        dto.setTotalTtc(facture.getTotalTtc());
        dto.setIdBonCommande(facture.getBonCommande() != null ? facture.getBonCommande().getIdBonCommande() : null);
        dto.setIdClient(facture.getClients().getIdClient());

      List<ReglementDto> reglementsDTO = facture.getReglements() != null
                ? facture.getReglements().stream().map(this::toReglementDTO).collect(Collectors.toList())
                : List.of();
        dto.setReglements(reglementsDTO);

        BigDecimal montantPaye = reglementsDTO.stream()
                .map(ReglementDto::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setMontantPaye(montantPaye);
        dto.setResteAPayer(facture.getTotalTtc().subtract(montantPaye));

        return dto;
    }

  public ReglementDto toReglementDTO(Reglement reglement) {
        ReglementDto dto = new ReglementDto();
        dto.setIdReglement(reglement.getIdReglement());
        dto.setMontant(reglement.getMontant());
        dto.setMode(reglement.getMode());
        dto.setDateReglement(reglement.getDateReglement());
        return dto;
    }


}
