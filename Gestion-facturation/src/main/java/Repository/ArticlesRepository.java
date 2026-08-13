package Repository;
import Entity.Articles;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ArticlesRepository extends JpaRepository<Articles, Long> {

}
