package DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDto {

    private BigDecimal chiffreAffairesDuMois;
    private Long nombreFacturesImpayees;
    private List<TopClientDto> topClients;

}
