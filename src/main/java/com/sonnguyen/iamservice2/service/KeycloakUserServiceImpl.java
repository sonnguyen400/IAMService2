package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.exception.KeycloakException;
import com.sonnguyen.iamservice2.viewmodel.UserRegistrationPostVm;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
@ConditionalOnProperty(
        value = "keycloak.enable",
        havingValue = "true"
)
public class KeycloakUserServiceImpl implements UserService {
    @Value("${keycloak.user_management.registration.realm}")
    private String realm;
    @Autowired
    private Keycloak keycloak;
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Override
    public void createUser(UserRegistrationPostVm user) {
        createKeycloakUser(user);
        userServiceImpl.createUser(user);
    }

    public void createKeycloakUser(UserRegistrationPostVm userRegistrationPostVm) {

        UserRepresentation userRepresentation = mapUserRegistrationPostVm(userRegistrationPostVm);
        try (Response response = keycloak.realm(realm).users().create(userRepresentation)) {
            if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                throw new KeycloakException("Keycloak user creation failed");
            }
        }

    }


    public UserRepresentation mapUserRegistrationPostVm(UserRegistrationPostVm userRegistrationPostVm) {

        //User's base profile
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userRegistrationPostVm.username());
        userRepresentation.setEmail(userRegistrationPostVm.email());
        userRepresentation.setFirstName(userRegistrationPostVm.firstname());
        userRepresentation.setLastName(userRegistrationPostVm.lastname());
        userRepresentation.setEnabled(true);

        //User's credentials information
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(userRegistrationPostVm.password());


        userRepresentation.setCredentials(List.of(credentialRepresentation));

        return userRepresentation;
    }
}