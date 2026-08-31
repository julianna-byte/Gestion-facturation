package Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import DTO.ArticleDto;
import Service.ArticleService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/articles")

public class ArticleController {

     private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Operation(
        summary = "Lister tous les articles",
        description = "Retourne la liste complète des articles disponibles"
    )
    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")

    @GetMapping
    public ResponseEntity<List<ArticleDto>> getAll() {
        return ResponseEntity.ok(articleService.findAll());
    }

    @Operation(
        summary = "Lister les articles avec pagination",
        description = "Retourne une page d’articles selon les paramètres fournis"
    )
    @ApiResponse(responseCode = "200", description = "Page d’articles récupérée")

    @GetMapping("/paginated")
    public ResponseEntity<Page<ArticleDto>> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(articleService.findAll(page, size));
    }

    @Operation(
        summary = "Récupérer un article par ID",
        description = "Retourne un article spécifique en fonction de son identifiant"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Article trouvé"),
        @ApiResponse(responseCode = "404", description = "Article introuvable")
    })

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.findById(id));
    }

     @Operation(
        summary = "Rechercher des articles par libellé",
        description = "Retourne une liste paginée d’articles filtrés par libellé"
    )
    @ApiResponse(responseCode = "200", description = "Résultats de la recherche")

    @GetMapping("/search")
    public ResponseEntity<Page<ArticleDto>> search(
            @RequestParam String libelle,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(articleService.searchByLibelle(libelle, page, size));
    }

    @Operation(
        summary = "Créer un article",
        description = "Ajoute un nouvel article dans la base de données"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Article créé"),
        @ApiResponse(responseCode = "400", description = "Requête invalide")
    })

    @PostMapping
    public ResponseEntity<ArticleDto> create(@Valid @RequestBody ArticleDto dto) {
        ArticleDto created = articleService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
        summary = "Mettre à jour un article",
        description = "Modifie les informations d’un article existant"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Article mis à jour"),
        @ApiResponse(responseCode = "404", description = "Article introuvable")
    })

    @PutMapping("/{id}")
    public ResponseEntity<ArticleDto> update(@PathVariable Long id, @Valid @RequestBody ArticleDto dto) {
        return ResponseEntity.ok(articleService.update(id, dto));
    }

    @Operation(
        summary = "Supprimer un article",
        description = "Supprime un article de la base de données"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Article supprimé"),
        @ApiResponse(responseCode = "404", description = "Article introuvable")
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
