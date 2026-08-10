package Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Utilissateur")


public class Utilisateur {
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long idUtilisateur;
    
     private String identifiant;
      private String motdepasse;
    
    


}
