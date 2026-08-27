package Mapper;

import org.springframework.stereotype.Component;
import DTO.ArticleDto;
import Entity.Articles;

@Component

public class ArticleMapper {


    //Dto(Data Transfert Object) en entités
    public ArticleDto toDTO(Articles article) {

        ArticleDto dto = new ArticleDto();
        dto.setIdArticles(article.getIdArticles());
        dto.setCode(article.getCode());
        dto.setLibelle(article.getLibelle());
        dto.setDescription(article.getDescription());
        dto.setUnite(article.getUnite());
        dto.setTauxTva(article.getTauxTva());
        dto.setPrixunitaireHT(article.getPrixunitaireHT());
        return dto;
    }
    
    // Entités en  Dto(Data Transfert Object) 
    public Articles toEntity(ArticleDto dto) {
        Articles article = new Articles();
        article.setCode(dto.getCode());
        article.setLibelle(dto.getLibelle());
        article.setDescription(dto.getDescription());
        article.setUnite(dto.getUnite());
        article.setTauxTva(dto.getTauxTva());
        article.setPrixunitaireHT(dto.getPrixunitaireHT());
        return article;
    }

    // Mise a jour d'entité 
    public void updateEntityFromDTO(ArticleDto dto, Articles article) {
        article.setCode(dto.getCode());
        article.setLibelle(dto.getLibelle());
        article.setDescription(dto.getDescription());
        article.setUnite(dto.getUnite());
        article.setTauxTva(dto.getTauxTva());
        article.setPrixunitaireHT(dto.getPrixunitaireHT());
    }




}
