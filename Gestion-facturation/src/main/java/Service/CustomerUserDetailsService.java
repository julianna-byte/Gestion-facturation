package Service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import Entity.Utilisateur;
import Repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import java.util.Collections;

@Service
@RequiredArgsConstructor

public class CustomerUserDetailsService  implements UserDetailsService{

    private final UtilisateurRepository UtilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Utilisateur utilisateur = UtilisateurRepository.findByIdentifiant(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'identifiant : " + username)); 
        return new User(
            utilisateur.getIdentifiant(),
                utilisateur.getMotdepasse(),
                Collections.singletonList(new SimpleGrantedAuthority(utilisateur.getRole().name()))
        );
    }

}
