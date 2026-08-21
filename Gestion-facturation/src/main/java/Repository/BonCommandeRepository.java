package Repository;
import Entity.BonCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface BonCommandeRepository extends JpaRepository<BonCommande,Long> {

    @Query("""
        SELECT COUNT(b)
        FROM BonCommande b
        WHERE MONTH(b.dateCreation) = :mois
          AND YEAR(b.dateCreation) = :annee
        """)
    long countByMoisAnnee(
        @Param("mois") int mois,
        @Param("annee") int annee
    );
}