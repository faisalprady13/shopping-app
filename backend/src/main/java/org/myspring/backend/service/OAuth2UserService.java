package org.myspring.backend.service;

import lombok.RequiredArgsConstructor;
import org.myspring.backend.model.AuthProvider;
import org.myspring.backend.model.Role;
import org.myspring.backend.model.User;
import org.myspring.backend.repository.UserRepo;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserService {
    private final UserRepo userRepo;
    private final IdService idService;

    public User getOrCreateUser(String registrationId, OAuth2User oauth2User) {
        AuthProvider provider = AuthProvider.valueOf(registrationId.toUpperCase());
        String providerId = resolveProviderId(provider, oauth2User.getAttributes());

        return userRepo.findByAuthProviderAndProviderId(provider, providerId)
                .orElseGet(() -> userRepo.save(new User(
                        idService.generateId(),
                        resolveName(provider, oauth2User.getAttributes()),
                        resolveEmail(provider, providerId, oauth2User.getAttributes()),
                        null,
                        provider,
                        providerId,
                        Role.USER,
                        new ArrayList<>()
                )));
    }

    private String resolveProviderId(AuthProvider provider, Map<String, Object> attributes) {
        Object id = provider == AuthProvider.GOOGLE ? attributes.get("sub") : attributes.get("id");
        return String.valueOf(id);
    }

    private String resolveName(AuthProvider provider, Map<String, Object> attributes) {
        Object name = attributes.get("name");
        if (name != null && !String.valueOf(name).isBlank()) {
            return String.valueOf(name);
        }

        Object login = attributes.get("login");
        if (login != null && !String.valueOf(login).isBlank()) {
            return String.valueOf(login);
        }

        return provider.name().toLowerCase() + "-user";
    }

    private String resolveEmail(AuthProvider provider, String providerId, Map<String, Object> attributes) {
        Object email = attributes.get("email");
        if (email != null && !String.valueOf(email).isBlank()) {
            return String.valueOf(email).trim().toLowerCase();
        }

        return providerId + "@" + provider.name().toLowerCase() + ".oauth.local";
    }
}
