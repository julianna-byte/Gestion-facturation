package Mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import DTO.BonCommandeDto;
import Entity.BonCommande;
import Entity.LignesCommande;
import java.util.List;

import DTO.LigneCommandeDto;

@Component
public class BonCommandeMapper {

    //Dto(Data Transfert Object) en entités
     public BonCommandeDto toDTO(BonCommande bc) {
        BonCommandeDto dto = new BonCommandeDto();
        dto.setIdBonCommande(bc.getIdBonCommande());
        dto.setNumeroBon(bc.getNumeroBon());
        dto.setIdClients(bc.getClient().getIdClient());
        dto.setStatut(bc.getStatut().name());

        dto.setTotalHT(bc.getTotalHT());
        dto.setTva(bc.getTva());
        dto.setTotalTtc(bc.getTotalTtc());


        List<LigneCommandeDto> lignesDTO = bc.getLignes().stream()
                .map(this::toLigneDTO)
                .collect(Collectors.toList());
        dto.setLignes(lignesDTO);

        return dto;
    }


    //Dto(Data Transfert Object) en entités
    public LigneCommandeDto toLigneDTO(LignesCommande ligne) {
        LigneCommandeDto dto = new LigneCommandeDto();
        dto.setIdLignesCommande(ligne.getIdLignesCommande());
        dto.setIdArticles(ligne.getArticles().getIdArticles());
        dto.setQuantite(ligne.getQuantite());
        dto.setRemise(ligne.getRemise());
        dto.setPrixunitaire(ligne.getPrixunitaire());
        return dto;
    }


    


}
