package Repository;
import Entity.Clients;

import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ClientsRepository extends JpaRepository<Clients, Long> {
    //recherche par raison social ou nif
    List<Clients> findByRaisonsocial(String raisonsocial);
    List<Clients> findByNIF(String nIF);
    //recherche par liste paginee
List<Clients> findByNomcontact(String nomcontact,Pageable pageable);
}
