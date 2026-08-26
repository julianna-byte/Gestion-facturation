package Repository;

import Entity.BonCommande;
import Entity.Facture;
import Entity.StatutFacture;
import Entity.TypeFacture;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
public interface FactureRepository extends JpaRepository<Facture, Long> {

     // Vérifie si un bon de commande a déjà été facturé en définitif (RG-05)
    boolean existsByBonCommandeAndType(BonCommande bonCommande, TypeFacture type);

    // Compte les factures créées dans un mois/année donné, pour la numérotation automatique
    @Query("SELECT COUNT(f) FROM Facture f WHERE EXTRACT(MONTH FROM f.dateCreation) = :mois AND EXTRACT(YEAR FROM f.dateCreation) = :annee")
    long countByMoisAnnee(@Param("mois") int mois, @Param("annee") int annee);

    // ---- Méthodes pour le tableau de bord ----

    // Somme des factures non annulées créées entre deux dates (CA du mois)
     @Query("SELECT COALESCE(SUM(f.totalTtc), 0) FROM Facture f " +
           "WHERE f.dateCreation BETWEEN :debut AND :fin AND f.statut <> Entity.StatutFacture.ANNULEE")
    BigDecimal calculerChiffreAffaires(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);
    
    // Nombre de factures impayées (statuts passés en paramètre : EMISE, PARTIELLEMENT_PAYEE)
    long countByStatutIn(List<StatutFacture> statuts);

    // Top clients par montant total facturé (hors annulées)
    @Query("SELECT f.Clients.raisonsociale AS raisonSociale, SUM(f.totalTtc) AS totalFacture " +
           "FROM Facture f " +
           "WHERE f.statut <> Entity.StatutFacture.ANNULEE " +
           "GROUP BY f.Clients.raisonsociale " +
           "ORDER BY SUM(f.totalTtc) DESC")
    List<TopClientProjection> findTopClients(Pageable pageable);

}
