package Repository;

import Entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

              //rechercher par identifiant
    Optional<Utilisateur> findByIdentifiant(String identifiant);

}
