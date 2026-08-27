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

    @PostMapping("/generer/{idBonCommande}")
    public ResponseEntity<FactureDto> generer(@PathVariable Long idBonCommande,
                                                @RequestParam TypeFacture type,
                                                Authentication authentication) {
        Utilisateur utilisateur = utilisateurRepository.findByIdentifiant(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        return ResponseEntity.ok(factureService.genererDepuisBonCommande(idBonCommande, type, utilisateur));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FactureDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(factureService.findById(id));
    }

    @PostMapping("/{id}/reglements")
    public ResponseEntity<FactureDto> enregistrerReglement(@PathVariable Long id,
                                                              @Valid @RequestBody  ReglementDto reglementDto) {
        return ResponseEntity.ok(factureService.enregistrerReglement(id, reglementDto));
    }
    @PatchMapping("/{id}/annuler")
    public ResponseEntity<FactureDto> annuler(@PathVariable Long id, @RequestParam String motif) {
        return ResponseEntity.ok(factureService.annuler(id, motif));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> telechargerPdf(@PathVariable Long id) {
        byte[] pdf = facturePdfService.genererPdf(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=facture-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }




}
