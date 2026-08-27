package Service;

import java.util.stream.Collectors;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import DTO.ArticleDto;
import Entity.Articles;
import Mapper.ArticleMapper;
import Repository.ArticlesRepository;

@Service
public class ArticleService {

   private final ArticlesRepository articlesRepository;
private final ArticleMapper articleMapper;

public ArticleService(ArticlesRepository articleRepository, ArticleMapper articleMapper) {
    this.articlesRepository = articleRepository;
    this.articleMapper = articleMapper;
}
// Avoir tous les clients
    public List<ArticleDto> findAll() {
        return articlesRepository.findAll()
                .stream()
                .map(articleMapper::toDTO)
                .collect(Collectors.toList());
    }

     public Page<ArticleDto> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return articlesRepository.findAll(pageable).map(articleMapper::toDTO);
    }
  //rechercher par identifiants
    public ArticleDto findById(Long id) {
        Articles article = articlesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article introuvable avec l'id : " + id));
        return articleMapper.toDTO(article);
    }
    public Page<ArticleDto> searchByLibelle(String libelle, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return articlesRepository.findByLibelleContainingIgnoreCase(libelle, pageable)
            .map(articleMapper::toDTO);
    }
 //creer un article
    public ArticleDto create(ArticleDto dto) {
        Articles article = articleMapper.toEntity(dto);
        return articleMapper.toDTO(articlesRepository.save(article));
    }
  //modifier un article
    public ArticleDto update(Long id, ArticleDto dto) {
        Articles article = articlesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article introuvable avec l'id : " + id));
        articleMapper.updateEntityFromDTO(dto, article);
        return articleMapper.toDTO(articlesRepository.save(article));
    }
     //supprimer un article
     public void delete(Long id) {
        Articles article = articlesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article introuvable avec l'id : " + id));
        articlesRepository.delete(article);
    }







}
