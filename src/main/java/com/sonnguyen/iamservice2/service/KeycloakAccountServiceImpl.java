package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.exception.KeycloakException;
import com.sonnguyen.iamservice2.model.Account;
import com.sonnguyen.iamservice2.viewmodel.UserCreationPostVm;
import com.sonnguyen.iamservice2.viewmodel.UserRegistrationPostVm;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Primary
@ConditionalOnProperty(
        value = "keycloak.enable",
        havingValue = "true"
)
@Slf4j
public class KeycloakAccountServiceImpl implements AccountService {
    @Value("${keycloak.user_management.registration.realm}")
    private String realm;
    @Autowired
    private Keycloak keycloak;
    @Autowired
    private AccountServiceImpl accountServiceImpl;

    @Override
    public void register(UserRegistrationPostVm userRegistrationPostVm) {
        Account account=userRegistrationPostVm.toEntity();
        account.setLocked(false);
        createKeycloakUser(account);
        accountServiceImpl.register(userRegistrationPostVm);
    }

    @Override
    public void create(UserCreationPostVm userCreationPostVm) {
        createKeycloakUser(userCreationPostVm.toEntity());
    }

    public UserRepresentation findByEmail(String email){
        List<UserRepresentation> users=keycloak.realm(realm).users().searchByEmail(email,true);
        if(users.size()!=1){
            throw new RuntimeException("Invalid email");
        }
        return users.getFirst();
    }




    @Override
    public void updateLockedStatusByEmail(Boolean isLocked, String email) {
        UserRepresentation user=findByEmail(email);
        user.setEnabled(!isLocked);
        UserResource userResource=keycloak.realm(realm).users().get(user.getId());
        userResource.update(user);
    }
    public void createKeycloakUser(Account account) {
        UserRepresentation userRepresentation = mapAccount(account);
        try (Response response = keycloak.realm(realm).users().create(userRepresentation)) {
            log.info(response.readEntity(String.class));
            if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                throw new KeycloakException("Keycloak user creation failed");
            }
        }

    }


    public UserRepresentation mapAccount(Account account) {

        //User's base profile
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(account.getEmail());
        userRepresentation.setEmail(account.getEmail());
        userRepresentation.setFirstName(account.getFirstName());
        userRepresentation.setLastName(account.getLastName());

        userRepresentation.setAttributes(
                Map.of(
                        "picture_url",List.of(account.getPicture()),
                        "phone",List.of(account.getPhone()),
                        "address",List.of(account.getAddress())
                )
        );

        userRepresentation.setEnabled(!account.isLocked());

        //User's credentials information
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(account.getPassword());



        userRepresentation.setCredentials(List.of(credentialRepresentation));

        return userRepresentation;
    }
}