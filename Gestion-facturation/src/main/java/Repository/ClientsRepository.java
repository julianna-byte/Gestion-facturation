package Repository;
import Entity.Clients;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ClientsRepository extends JpaRepository<Clients, Long> {
    // recherche par raison sociale ou NIF
    List<Clients> findByRaisonsociale(String raisonsocial);
    List<Clients> findByNif(String nif);

    // recherche par liste paginée
    Page<Clients> findByNomcontact(String nomcontact, Pageable pageable);
}
