package com.unitrack.config;

import com.unitrack.entity.Collaborator;
import com.unitrack.repository.CollaboratorRepository;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CollaboratorOidcUserService
        implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final CollaboratorRepository collaboratorRepository;

    public CollaboratorOidcUserService(CollaboratorRepository userRepository) {
        this.collaboratorRepository = userRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest request) {

        OidcUser oidcUser = new OidcUserService().loadUser(request);

        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();
        String picture = oidcUser.getPicture();
        String providerId = oidcUser.getSubject();

        Collaborator user = collaboratorRepository.findByEmail(email)
                .orElseGet(() -> registerNewUser(
                        email, name, picture, providerId
                ));

        return oidcUser;
    }

    private Collaborator registerNewUser(
            String email,
            String name,
            String picture,
            String providerId) {

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
