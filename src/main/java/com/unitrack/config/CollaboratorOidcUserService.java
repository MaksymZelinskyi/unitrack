package com.unitrack.config;

import com.unitrack.entity.AuthProvider;
import com.unitrack.entity.Collaborator;
import com.unitrack.repository.CollaboratorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class  CollaboratorOidcUserService
        implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final CollaboratorRepository collaboratorRepository;

    public CollaboratorOidcUserService(CollaboratorRepository userRepository) {
        this.collaboratorRepository = userRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest request) {

        OidcUserService delegate = new OidcUserService();

        OidcUser user = delegate.loadUser(request);

        Optional<Collaborator> optional = collaboratorRepository.findByEmail(user.getEmail());
        Collaborator collaborator;
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (optional.isEmpty()) {
            collaborator = new Collaborator(user.getGivenName(), user.getFamilyName(), user.getEmail());
            collaborator.setAdmin(true);
        } else {
            collaborator = optional.get();
        }
        collaborator.addAuthProvider(AuthProvider.OIDC_GOOGLE);

        if (collaborator.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        collaboratorRepository.save(collaborator);
        log.debug("OIDC authentication succeeded for user {}", user.getEmail());
        return new DefaultOidcUser(
                authorities,
                user.getIdToken(),
                user.getUserInfo(),
                "email"
        );
    }

    private Collaborator registerNewUser(
            String email,
            String name,
            String picture
    ) {

        String[] firstLastName = name.split(" ");
        Collaborator user = new Collaborator();
        user.setEmail(email);
        user.setFirstName(firstLastName[0]);
        user.setLastName(firstLastName[1]);
        user.setAvatarUrl(picture);
        user.setAdmin(true);

        return collaboratorRepository.save(user);
    }
}
