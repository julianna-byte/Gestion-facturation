package Controller;

import DTO.DashboardDto;
import Service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
 
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Operation(
        summary = "Obtenir le tableau de bord",
        description = "Retourne les données agrégées du dashboard (statistiques, indicateurs, etc.)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dashboard généré avec succès"),
        @ApiResponse(responseCode = "401", description = "Non autorisé - token manquant ou invalide")
    })
 
    @GetMapping
    public ResponseEntity<DashboardDto> getDashboard() {
        return ResponseEntity.ok(dashboardService.genererDashboard());
    }



}
