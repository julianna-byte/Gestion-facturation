package Service;

import DTO.DashboardDto;
import DTO.TopClientDto;
import Entity.StatutFacture;
import Repository.FactureRepository;
import Repository.TopClientProjection;
 
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
 
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final FactureRepository factureRepository;
 
    public DashboardService(FactureRepository factureRepository) {
        this.factureRepository = factureRepository;
    }
 
    public DashboardDto genererDashboard() {
        YearMonth moisEnCours = YearMonth.now();
        LocalDateTime debutMois = moisEnCours.atDay(1).atStartOfDay();
        LocalDateTime finMois = moisEnCours.atEndOfMonth().atTime(23, 59, 59);
 
        BigDecimal chiffreAffaires = factureRepository.calculerChiffreAffaires(debutMois, finMois);
 
        long facturesImpayees = factureRepository.countByStatutIn(
                List.of(StatutFacture.EMISE, StatutFacture.PARTIELLEMENT_PAYEE)
        );
 
        Pageable top5 = PageRequest.of(0, 5);
        List<TopClientDto> topClients = factureRepository.findTopClients(top5).stream()
                .map(this::toTopClientDto)
                .collect(Collectors.toList());
 
        return new DashboardDto(chiffreAffaires, facturesImpayees, topClients);
    }
 
    private TopClientDto toTopClientDto(TopClientProjection projection) {
        return new TopClientDto(projection.getRaisonSociale(), projection.getTotalFacture());
    }

}
