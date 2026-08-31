package Controller;


import org.springframework.http.HttpStatus;
import DTO.BonCommandeDto;
import Entity.Utilisateur;
import Service.BonCommandeService;
import Repository.UtilisateurRepository;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/bons_commande")
public class BonCommandeController {

     private final BonCommandeService bonCommandeService;
    private final UtilisateurRepository utilisateurRepository;

    public BonCommandeController(BonCommandeService bonCommandeService,
                                   UtilisateurRepository utilisateurRepository) {
        this.bonCommandeService = bonCommandeService;
        this.utilisateurRepository = utilisateurRepository;
    }

    //creer bons de commande
     @Operation(
        summary = "Créer un bon de commande",
        description = "Ajoute un nouveau bon de commande lié à l’utilisateur connecté"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Bon de commande créé"),
        @ApiResponse(responseCode = "400", description = "Requête invalide"),
        @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié")
    })

    @PostMapping
    public ResponseEntity<BonCommandeDto> create(@Valid @RequestBody BonCommandeDto dto,
                                                   Authentication authentication) {

        String identifiant = authentication.getName();
        Utilisateur utilisateurConnecte = utilisateurRepository.findByIdentifiant(identifiant)
                .orElseThrow(() -> new RuntimeException("Utilisateur connecté introuvable"));

        BonCommandeDto created = bonCommandeService.create(dto, utilisateurConnecte);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    // Liste paginee des bons de commande

     @Operation(
        summary = "Lister les bons de commande avec pagination",
        description = "Retourne une page de bons de commande selon les paramètres fournis"
    )
    @ApiResponse(responseCode = "200", description = "Page de bons de commande récupérée")

    @GetMapping("/paginated")
    public ResponseEntity<org.springframework.data.domain.Page<BonCommandeDto>> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bonCommandeService.findAll(page, size));
    }

    
    //rechercher par identifiant

    @Operation(
        summary = "Récupérer un bon de commande par ID",
        description = "Retourne un bon de commande spécifique en fonction de son identifiant"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bon de commande trouvé"),
        @ApiResponse(responseCode = "404", description = "Bon de commande introuvable")
    })

    @GetMapping("/{id}")
    public ResponseEntity<BonCommandeDto> getById (@PathVariable Long id){

        return ResponseEntity.ok(bonCommandeService.findById(id));
    }


    @Operation(
        summary = "Mettre à jour un bon de commande",
        description = "Modifie les informations d’un bon de commande existant"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bon de commande mis à jour"),
        @ApiResponse(responseCode = "404", description = "Bon de commande introuvable")
    })

    @PutMapping("/{id}")
    public ResponseEntity<BonCommandeDto> update (@PathVariable Long id, @Valid @RequestBody BonCommandeDto dto){

        return ResponseEntity.ok(bonCommandeService.update(id, dto));
    }

    //valider bons de commande
    @Operation(
        summary = "Valider un bon de commande",
        description = "Change le statut du bon de commande en 'Validé'"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bon de commande validé"),
        @ApiResponse(responseCode = "404", description = "Bon de commande introuvable")
    })

    @GetMapping("/{id}/valider")
        public ResponseEntity<BonCommandeDto> valider (@PathVariable Long id){
            return ResponseEntity.ok(bonCommandeService.valider(id));
    
    }

    //annuler bons de commande

    @Operation(
        summary = "Annuler un bon de commande",
        description = "Change le statut du bon de commande en 'Annulé'"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bon de commande annulé"),
        @ApiResponse(responseCode = "404", description = "Bon de commande introuvable")
    })

    @GetMapping("/{id}/annuler")
        public ResponseEntity<BonCommandeDto> annuler (@PathVariable Long id){
            return ResponseEntity.ok(bonCommandeService.annuler(id));
    
    }



    
    
    
    


}
