package Controller;

import Configuration.JwtUtils;
import Entity.Utilisateur;
import Repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    
     @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        Utilisateur utilisateur = utilisateurRepository.findByIdentifiant(request.getIdentifiant())
                .orElseThrow(() -> new RuntimeException("Identifiants invalides"));

        if (!passwordEncoder.matches(request.getMotdepasse(), utilisateur.getMotdepasse())) {
            throw new RuntimeException("Identifiants invalides");
        }

        String token = jwtUtils.generateToken(utilisateur.getIdentifiant(), utilisateur.getRole().name());

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("role", utilisateur.getRole().name());

        return ResponseEntity.ok(response);
      }

    public static class LoginRequest {
        private String identifiant;
        private String motdepasse;

        public String getIdentifiant() { return identifiant; }
        public void setIdentifiant(String identifiant) { this.identifiant = identifiant; }
        public String getMotdepasse() { return motdepasse; }
        public void setMotdepasse(String motdepasse) { this.motdepasse = motdepasse; }
    }  





}
