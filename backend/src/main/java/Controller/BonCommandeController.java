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

    @GetMapping("/paginated")
    public ResponseEntity<org.springframework.data.domain.Page<BonCommandeDto>> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bonCommandeService.findAll(page, size));
    }

    
    //rechercher par identifiant
    @GetMapping("/{id}")
    public ResponseEntity<BonCommandeDto> getById (@PathVariable Long id){

        return ResponseEntity.ok(bonCommandeService.findById(id));
    }

  

    @PutMapping("/{id}")
    public ResponseEntity<BonCommandeDto> update (@PathVariable Long id, @Valid @RequestBody BonCommandeDto dto){

        return ResponseEntity.ok(bonCommandeService.update(id, dto));
    }

    //valider bons de commande

    @GetMapping("/{id}/valider")
        public ResponseEntity<BonCommandeDto> valider (@PathVariable Long id){
            return ResponseEntity.ok(bonCommandeService.valider(id));
    
    }

    //annuler bons de commande

    @GetMapping("/{id}/annuler")
        public ResponseEntity<BonCommandeDto> annuler (@PathVariable Long id){
            return ResponseEntity.ok(bonCommandeService.annuler(id));
    
    }



    
    
    
    


}
