package Configuration;

import Entity.Role;
import Entity.Utilisateur;
import Repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

     private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

     @Override
    public void run(String... args) {
        if (utilisateurRepository.findByIdentifiant("admin").isEmpty()) {
            Utilisateur admin = new Utilisateur();
            admin.setIdentifiant("admin");
            admin.setMotdepasse(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            utilisateurRepository.save(admin);
            System.out.println("Utilisateur admin créé avec succès.");
        }

            if (utilisateurRepository.findByIdentifiant("commercial").isEmpty()) {
            Utilisateur commercial = new Utilisateur();
            commercial.setIdentifiant("commercial");
            commercial.setMotdepasse(passwordEncoder.encode("commercial123"));
            commercial.setRole(Role.COMMERCIAL);
            utilisateurRepository.save(commercial);
            System.out.println("Utilisateur commercial créé avec succès.");
        }
        
        if (utilisateurRepository.findByIdentifiant("admin2").isEmpty()) {
            Utilisateur admin2 = new Utilisateur();
            admin2.setIdentifiant("admin2");
            admin2.setMotdepasse(passwordEncoder.encode("admin2424"));
            admin2.setRole(Role.ADMIN);
            utilisateurRepository.save(admin2);
            System.out.println("Utilisateur admin créé avec succès.");
        }

    }
    

}
