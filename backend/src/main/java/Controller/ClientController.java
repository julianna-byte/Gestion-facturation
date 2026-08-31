package Controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import DTO.ClientDto;
import Service.ClientService;
import jakarta.validation.Valid;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/clients")


public class ClientController {

 private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Operation(
        summary = "Lister tous les clients",
        description = "Retourne la liste complète des clients enregistrés"
    )

    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")

    @GetMapping
    public ResponseEntity<List<ClientDto>> getAll() {
        return ResponseEntity.ok(clientService.findAll());
    }

    @Operation(
        summary = "Lister les clients avec pagination",
        description = "Retourne une page de clients selon les paramètres fournis"
    )
    @ApiResponse(responseCode = "200", description = "Page de clients récupérée")

    @GetMapping("/paginated")
    public ResponseEntity<Page<ClientDto>> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(clientService.findAll(page, size));
    }

    @Operation(
        summary = "Récupérer un client par ID",
        description = "Retourne un client spécifique en fonction de son identifiant"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Client trouvé"),
        @ApiResponse(responseCode = "404", description = "Client introuvable")
    })

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.findById(id));
    }

    @Operation(
        summary = "Rechercher des clients par raison sociale",
        description = "Retourne une liste paginée de clients filtrés par raison sociale"
    )
    @ApiResponse(responseCode = "200", description = "Résultats de la recherche")

    @GetMapping("/search")
    public ResponseEntity<Page<ClientDto>> search(
        @RequestParam String raisonsociale,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(clientService.searchByRaisonSociale(raisonsociale, page, size));
    }

    @Operation(
        summary = "Créer un client",
        description = "Ajoute un nouveau client dans la base de données"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Client créé"),
        @ApiResponse(responseCode = "400", description = "Requête invalide")
    })

    @PostMapping
    public ResponseEntity<ClientDto> create(@Valid @RequestBody ClientDto dto) {
        ClientDto created = clientService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
        summary = "Mettre à jour un client",
        description = "Modifie les informations d’un client existant"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Client mis à jour"),
        @ApiResponse(responseCode = "404", description = "Client introuvable")
    })

    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> update(@PathVariable Long id, @Valid @RequestBody ClientDto dto) {
        return ResponseEntity.ok(clientService.update(id, dto));
    }

    @Operation(
        summary = "Désactiver un client",
        description = "Met un client en inactif sans le supprimer"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Client désactivé"),
        @ApiResponse(responseCode = "404", description = "Client introuvable")
    })

    @PatchMapping("/{id}/desactiver")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        clientService.deactivate(id);
        return ResponseEntity.noContent().build();
    }




}
