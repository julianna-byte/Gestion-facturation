package Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import Entity.Utilisateur;
import Repository.BonCommandeRepository;

@Service
public class NumerotationService {

    private final BonCommandeRepository bonCommandeRepository;

    public NumerotationService(BonCommandeRepository bonCommandeRepository) {
        this.bonCommandeRepository = bonCommandeRepository;
    }
    public String genererNumeroBonCommande(Utilisateur utilisateur) {
        String initiales = genererInitiales(utilisateur.getIdentifiant());
        LocalDate now = LocalDate.now();
        String moisAnnee = now.format(DateTimeFormatter.ofPattern("MMyyyy"));

        long sequence = bonCommandeRepository.countByMoisAnnee(now.getMonthValue(), now.getYear()) + 1;
        String sequenceFormatee = String.format("%03d", sequence);

        return initiales + "/OA/B" + sequenceFormatee + "/" + moisAnnee;
    }

    private String genererInitiales(String identifiant){

        return identifiant.substring(0,Math.min(2, identifiant.length())).toUpperCase();
    }



}
