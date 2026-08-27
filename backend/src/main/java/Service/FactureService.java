package Service;


import DTO.FactureDto;
import DTO.ReglementDto;
import Entity.*;
import Mapper.FactureMapper;
import Repository.BonCommandeRepository;
import Repository.FactureRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class FactureService {

   private final FactureRepository factureRepository;
    private final BonCommandeRepository bonCommandeRepository;
    private final FactureMapper factureMapper;
    private final NumerotationService numerotationService;

    public FactureService(FactureRepository factureRepository,
                           BonCommandeRepository bonCommandeRepository,
                           FactureMapper factureMapper,
                           NumerotationService numerotationService) {
        this.factureRepository = factureRepository;
        this.bonCommandeRepository = bonCommandeRepository;
        this.factureMapper = factureMapper;
        this.numerotationService = numerotationService;
    }

    // Génère une facture proforma OU définitive à partir d'un bon de commande VALIDÉ
    public FactureDto genererDepuisBonCommande(Long idBonCommande, TypeFacture type, Utilisateur utilisateurConnecte) {
        BonCommande bc = bonCommandeRepository.findById(idBonCommande)
                .orElseThrow(() -> new RuntimeException("Bon de commande introuvable"));

        if (bc.getStatut() != StatutBonCommande.VALIDE) {
            throw new IllegalStateException("Seul un bon de commande VALIDÉ peut être facturé");
        }

        // RG-05 : un bon de commande ne peut être facturé qu'une seule fois (uniquement pour DEFINITIVE)
        if (type == TypeFacture.DEFINITIVE && factureRepository.existsByBonCommandeAndType(bc, TypeFacture.DEFINITIVE)) {
            throw new IllegalStateException("Ce bon de commande a déjà été facturé");
        }

        Facture facture = new Facture();
        facture.setBonCommande(bc);
        facture.setClients(bc.getClient());
        facture.setUtilisateur(utilisateurConnecte);
        facture.setType(type);
        facture.setStatut(StatutFacture.EMISE);
        
        facture.setTotalTtc(bc.getTotalTtc());
        facture.setNumerofacture(numerotationService.genererNumeroFacture(utilisateurConnecte, type));

         Facture saved = factureRepository.save(facture);

        // Si facture définitive, le bon de commande passe au statut FACTURE
        if (type == TypeFacture.DEFINITIVE) {
            bc.setStatut(StatutBonCommande.FACTURE);
            bonCommandeRepository.save(bc);
        }

        return factureMapper.toDTO(saved);
    }

    // Enregistre un règlement et met à jour le statut automatiquement
    public FactureDto enregistrerReglement(Long idFacture, ReglementDto reglementDto) {
        Facture facture = factureRepository.findById(idFacture)
                .orElseThrow(() -> new RuntimeException("Facture introuvable"));

        if (facture.getStatut() == StatutFacture.ANNULEE) {
            throw new IllegalStateException("Impossible d'enregistrer un règlement sur une facture annulée");
        }
        if (facture.getStatut() == StatutFacture.PAYEE) {
            throw new IllegalStateException("Cette facture est déjà entièrement payée");
        }

        Reglement reglement = new Reglement();
        reglement.setFacture(facture);
        reglement.setMontant(reglementDto.getMontant());
        reglement.setMode(reglementDto.getMode());
        reglement.setDateReglement(LocalDate.now());

        facture.getReglements().add(reglement);

        // RG-06 : calcul côté serveur du statut selon le total réglé
        BigDecimal totalPaye = facture.getReglements().stream()
                .map(Reglement::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalPaye.compareTo(facture.getTotalTtc()) >= 0) {
            facture.setStatut(StatutFacture.PAYEE);
        } else if (totalPaye.compareTo(BigDecimal.ZERO) > 0) {
            facture.setStatut(StatutFacture.PARTIELLEMENT_PAYEE);
        }
        Facture saved = factureRepository.save(facture);
        return factureMapper.toDTO(saved);
    }
    // Annulation avec motif obligatoire (RG-04)
    public FactureDto annuler(Long idFacture, String motif) {
        if (motif == null || motif.isBlank()) {
            throw new IllegalArgumentException("Le motif d'annulation est obligatoire");
        }

        Facture facture = factureRepository.findById(idFacture)
                .orElseThrow(() -> new RuntimeException("Facture introuvable"));

        facture.setStatut(StatutFacture.ANNULEE);
        facture.setMotifAnnulation(motif);

        Facture saved = factureRepository.save(facture);
        return factureMapper.toDTO(saved);

        }

    public FactureDto findById(Long id) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facture introuvable"));
        return factureMapper.toDTO(facture);
    }











 

}
