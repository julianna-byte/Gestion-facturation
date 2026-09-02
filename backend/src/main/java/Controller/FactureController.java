package Controller;

import DTO.FactureDto;
import DTO.ReglementDto;
import Entity.TypeFacture;
import Entity.Utilisateur;
import Repository.UtilisateurRepository;
import Service.FactureService;
import Service.FacturePdfService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
@RestController
@RequestMapping("/api/factures")

public class FactureController {

    private final FactureService factureService;
    private final UtilisateurRepository utilisateurRepository;
    private final FacturePdfService facturePdfService;

    public FactureController(FactureService factureService,
                              UtilisateurRepository utilisateurRepository,
                              FacturePdfService facturePdfService) {
        this.factureService = factureService;
        this.utilisateurRepository = utilisateurRepository;
        this.facturePdfService = facturePdfService;
    } 

    @Operation(
        summary = "Générer une facture",
        description = "Crée une facture à partir d’un bon de commande existant et du type de facture choisi"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Facture générée avec succès"),
        @ApiResponse(responseCode = "404", description = "Bon de commande introuvable"),
        @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié")
    })

    @PostMapping("/generer/{idBonCommande}")
    public ResponseEntity<FactureDto> generer(@PathVariable Long idBonCommande,
                                                @RequestParam TypeFacture type,
                                                Authentication authentication) {
        Utilisateur utilisateur = utilisateurRepository.findByIdentifiant(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        return ResponseEntity.ok(factureService.genererDepuisBonCommande(idBonCommande, type, utilisateur));
    }

    @Operation(
        summary = "Lister les factures avec pagination",
        description = "Retourne une page de factures selon les paramètres fournis"
    )
    @ApiResponse(responseCode = "200", description = "Page de factures récupérée")

    @GetMapping("/paginated")
    public ResponseEntity<org.springframework.data.domain.Page<FactureDto>> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(factureService.findAll(page, size));
    }

    @Operation(
        summary = "Récupérer une facture par ID",
        description = "Retourne une facture spécifique en fonction de son identifiant"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Facture trouvée"),
        @ApiResponse(responseCode = "404", description = "Facture introuvable")
    })

    @GetMapping("/{id}")
    public ResponseEntity<FactureDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(factureService.findById(id));
    }

    @Operation(
        summary = "Enregistrer un règlement",
        description = "Ajoute un paiement (règlement) à une facture existante"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Règlement enregistré"),
        @ApiResponse(responseCode = "404", description = "Facture introuvable")
    })

    @PostMapping("/{id}/reglements")
    public ResponseEntity<FactureDto> enregistrerReglement(@PathVariable Long id,
                                                              @Valid @RequestBody  ReglementDto reglementDto) {
        return ResponseEntity.ok(factureService.enregistrerReglement(id, reglementDto));
    }

    @Operation(
        summary = "Annuler une facture",
        description = "Change le statut d’une facture en 'Annulée' avec un motif"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Facture annulée"),
        @ApiResponse(responseCode = "404", description = "Facture introuvable")
    })
    @PatchMapping("/{id}/annuler")
    public ResponseEntity<FactureDto> annuler(@PathVariable Long id, @RequestParam String motif) {
        return ResponseEntity.ok(factureService.annuler(id, motif));
    }

    @Operation(
        summary = "Télécharger une facture en PDF",
        description = "Génère et télécharge le fichier PDF correspondant à une facture"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "PDF généré avec succès"),
        @ApiResponse(responseCode = "404", description = "Facture introuvable")
    })

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> telechargerPdf(
        @PathVariable Long id,
        @RequestParam(required = false) Boolean inclureSuiviPaiement) {
    byte[] pdf = facturePdfService.genererPdf(id, inclureSuiviPaiement);

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=facture-" + id + ".pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
}




}
