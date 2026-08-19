package Repository;
import Entity.BonCommande;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BonCommandeRepository extends JpaRepository<BonCommande,Long> {

    int countByMoisAnnee(int monthValue, int year);

}
