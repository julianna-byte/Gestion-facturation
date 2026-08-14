package Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import Entity.Articles;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ArticlesRepository extends JpaRepository<Articles, Long> {

 // recheche d'articles,ignorer la casse et paginee
 Page<Articles> findByLibelleContainingIgnoreCase(String libelle, Pageable pageable);
 
}
