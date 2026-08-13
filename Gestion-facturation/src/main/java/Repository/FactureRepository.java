package Repository;

import Entity.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FactureRepository extends JpaRepository<Facture, Long> {

}
