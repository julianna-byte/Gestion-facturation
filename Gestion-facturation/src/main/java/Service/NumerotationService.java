package Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import Repository.FactureRepository;
import org.springframework.stereotype.Service;

import Entity.TypeFacture;
import Entity.Utilisateur;
import Repository.BonCommandeRepository;

@Service
public class NumerotationService {

    private final BonCommandeRepository bonCommandeRepository;

    private final FactureRepository factureRepository;

    public NumerotationService(BonCommandeRepository bonCommandeRepository,
    FactureRepository factureRepository    
    ) {

        this.bonCommandeRepository = bonCommandeRepository;

        this.factureRepository = factureRepository;
    }

    //generation automatique numero bon de commande

     public String genererNumeroBonCommande(Utilisateur utilisateur) {
        String initiales = genererInitiales(utilisateur);
        LocalDate now = LocalDate.now();
        String moisAnnee = now.format(DateTimeFormatter.ofPattern("MMyyyy"));

        long sequence = bonCommandeRepository.countByMoisAnnee(now.getMonthValue(), now.getYear()) + 1;
        String sequenceFormatee = String.format("%03d", sequence);

        return initiales + "/OA/B" + sequenceFormatee + "/" + moisAnnee;
    }

    //generation numero factures(avec type FP= Facture proforma; FD= Facture definitive)

     public String genererNumeroFacture(Utilisateur utilisateur, TypeFacture type) {
        String initiales = genererInitiales(utilisateur);
        LocalDate now = LocalDate.now();
        String moisAnnee = now.format(DateTimeFormatter.ofPattern("MMyyyy"));

        String prefixe = type == TypeFacture.PROFORMA ? "FP" : "FD";
        long sequence = factureRepository.countByMoisAnnee(now.getMonthValue(), now.getYear()) + 1;
        String sequenceFormatee = String.format("%03d", sequence);

        return initiales + "/OA/" + prefixe + sequenceFormatee + "/" + moisAnnee;
    }

    


    //generation initiales
    private String genererInitiales(Utilisateur utilisateur) {
        String initialeNom = (utilisateur.getNom() != null && !utilisateur.getNom().isEmpty())
                ? utilisateur.getNom().substring(0, 1).toUpperCase()
                : "X"; // valeur de secours si le nom n'est pas renseigné
        String initialePrenom = (utilisateur.getPrenom() != null && !utilisateur.getPrenom().isEmpty())
                ? utilisateur.getPrenom().substring(0, 1).toUpperCase()
                : "X";
        return initialeNom + initialePrenom;
    }



}
