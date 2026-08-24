package Repository;

import Entity.BonCommande;
import Entity.Facture;
import Entity.TypeFacture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
public interface FactureRepository extends JpaRepository<Facture, Long> {

     // Vérifie si un bon de commande a déjà été facturé en définitif (RG-05)
    boolean existsByBonCommandeAndType(BonCommande bonCommande, TypeFacture type);

    // Compte les factures créées dans un mois/année donné, pour la numérotation automatique
    @Query("SELECT COUNT(f) FROM Facture f WHERE FUNCTION('MONTH', f.dateCreation) = :mois AND FUNCTION('YEAR', f.dateCreation) = :annee")
    long countByMoisAnnee(@Param("mois") int mois, @Param("annee") int annee);

}
