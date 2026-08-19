package Service;

import org.springframework.stereotype.Service;

import DTO.BonCommandeDto;
import DTO.LigneCommandeDto;

import Entity.*;
import Mapper.BonCommandeMapper;
import Repository.BonCommandeRepository;
import Repository.ClientsRepository;
import Repository.ArticlesRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class BonCommandeService {

 private final BonCommandeRepository bonCommandeRepository;

 private final BonCommandeMapper bonCommandeMapper;
 private final ClientsRepository clientsRepository ;
 private final ArticlesRepository articlesRepository ;
 private final NumerotationService numerotationService;

 public BonCommandeService(BonCommandeRepository bonCommandeRepository, 
                           BonCommandeMapper bonCommandeMapper,
                           ArticlesRepository articlesRepository,
                           ClientsRepository clientsRepository,
                           NumerotationService numerotationService){

        this.bonCommandeRepository = bonCommandeRepository;
        this.clientsRepository = clientsRepository;
        this.articlesRepository = articlesRepository;
        this.bonCommandeMapper = bonCommandeMapper;
        this.numerotationService = numerotationService;
    }

    // Création d'un bon de commande en statut BROUILLON
    public BonCommandeDto create(BonCommandeDto dto, Utilisateur utilisateurConnecte) {
        Clients client = clientsRepository.findById(dto.getIdClients())
                                          .orElseThrow(() -> new RuntimeException("Client introuvable"));

        BonCommande bc = new BonCommande();
        bc.setClient(client);
        bc.setUtilisateur(utilisateurConnecte);
        bc.setStatut(StatutBonCommande.BROUILLON);
        bc.setNumeroBon(numerotationService.genererNumeroBonCommande(utilisateurConnecte));

        List<LignesCommande> lignes = construireLignes(dto.getLignes (), bc);
        bc.setLignes(lignes);

        calculerTotaux(bc);

        BonCommande saved = bonCommandeRepository.save(bc);
        return bonCommandeMapper.toDTO(saved);
    }


    // Modification (uniquement si BROUILLON)
    public BonCommandeDto update(Long id, BonCommandeDto dto) {
        BonCommande bc = bonCommandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bon de commande introuvable"));

        if (bc.getStatut() != StatutBonCommande.BROUILLON) {
            throw new IllegalStateException("Un bon de commande validé n'est plus modifiable");
        }

        bc.getLignes().clear();
        List<LignesCommande> nouvellesLignes = construireLignes(dto.getLignes(), bc);
        bc.getLignes().addAll(nouvellesLignes);

        calculerTotaux(bc);

        BonCommande saved = bonCommandeRepository.save(bc);
        return bonCommandeMapper.toDTO(saved);
    }

    

    // Validation du bon de commande (changement de statut)
    public BonCommandeDto valider(Long id) {
        BonCommande bc = bonCommandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bon de commande introuvable"));

        if (bc.getStatut() != StatutBonCommande.BROUILLON) {
            throw new IllegalStateException("Seul un bon en BROUILLON peut être validé");
        }

        bc.setStatut(StatutBonCommande.VALIDE);
        BonCommande saved = bonCommandeRepository.save(bc);
        return bonCommandeMapper.toDTO(saved);
    }

    // Annulation
    public BonCommandeDto annuler(Long id) {
        BonCommande bc = bonCommandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bon de commande introuvable"));

        if (bc.getStatut() == StatutBonCommande.FACTURE) {
            throw new IllegalStateException("Un bon déjà facturé ne peut pas être annulé directement");
        }

        bc.setStatut(StatutBonCommande.ANNULE);
        BonCommande saved = bonCommandeRepository.save(bc);
        return bonCommandeMapper.toDTO(saved);
    }
    public BonCommandeDto findById(Long id) {
        BonCommande bc = bonCommandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bon de commande introuvable"));
        return bonCommandeMapper.toDTO(bc);
    }


    // --- Méthodes privées ---

    private List<LignesCommande> construireLignes(List<LigneCommandeDto> lignesDTO, BonCommande bc) {
        List<LignesCommande> lignes = new ArrayList<>();
        for (LigneCommandeDto ligneDTO : lignesDTO) {
            Articles article = articlesRepository.findById(ligneDTO.getIdArticles())
                    .orElseThrow(() -> new RuntimeException("Article introuvable : " + ligneDTO.getIdArticles()));

            LignesCommande ligne = new LignesCommande();
            ligne.setArticles(article);
            ligne.setQuantite(ligneDTO.getQuantite());
            ligne.setRemise(ligneDTO.getRemise() != null ? ligneDTO.getRemise() : BigDecimal.ZERO);


        // Prix pré-rempli depuis le catalogue si non fourni, sinon celui du DTO (modifiable)
            BigDecimal prix = ligneDTO.getPrixunitaire() != null
                    ? ligneDTO.getPrixunitaire()
                    : BigDecimal.valueOf(article.getPrixunitaireHT());
            ligne.setPrixunitaire(prix);

            ligne.setBonCommande(bc);
            lignes.add(ligne);
        }
        return lignes;
    }    


    // RG-06 : tous les calculs de totaux sont effectués côté serveur
    private void calculerTotaux(BonCommande bc) {
        BigDecimal totalHT = BigDecimal.ZERO;

        for (LignesCommande ligne : bc.getLignes()) {
            BigDecimal montantLigne = ligne.getPrixunitaire()
                    .multiply(BigDecimal.valueOf(ligne.getQuantite()))
                    .subtract(ligne.getRemise() != null ? ligne.getRemise() : BigDecimal.ZERO);
            totalHT = totalHT.add(montantLigne);
        }

        // RG-02 : taux TVA par défaut 18%, idéalement récupéré par article
        BigDecimal tauxTva = BigDecimal.valueOf(0.18);
        BigDecimal tva = totalHT.multiply(tauxTva).setScale(0, RoundingMode.HALF_UP);
        BigDecimal totalTtc = totalHT.add(tva);

        bc.setTotalHT(totalHT.setScale(0, RoundingMode.HALF_UP));
        bc.setTva(tva);
        bc.setTotalTtc(totalTtc);
    }















 






}