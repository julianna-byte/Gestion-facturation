package Repository;
import Entity.Enregistrement;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EnregistrementRepository extends JpaRepository<Enregistrement, Long> {

}
