package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.exception.KeycloakException;
import com.sonnguyen.iamservice2.model.Account;
import com.sonnguyen.iamservice2.viewmodel.UserCreationPostVm;
import com.sonnguyen.iamservice2.viewmodel.UserRegistrationPostVm;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Primary
@ConditionalOnProperty(
        value = "default-idp",
        havingValue = "KEYCLOAK"
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
    public void resetPasswordByAccountId(Long accountId,String rawPassword)  {
        Account account=accountServiceImpl.findById(accountId);
        UserRepresentation userRepresentation=findByEmail(account.getEmail());
        CredentialRepresentation newCredential=new CredentialRepresentation();
        newCredential.setType(CredentialRepresentation.PASSWORD);
        newCredential.setValue(rawPassword);
        keycloak.realm(realm).users().get(userRepresentation.getId()).resetPassword(newCredential);
        accountServiceImpl.resetPasswordByAccountId(accountId,rawPassword);
    }
    @Override
    public void register(UserRegistrationPostVm userRegistrationPostVm) {
        Account account=userRegistrationPostVm.toEntity();
        account.setLocked(false);
        account.setVerified(true);
        createKeycloakUser(account);
        accountServiceImpl.saveAccount(account);
    }

    @Override
    public void create(UserCreationPostVm userCreationPostVm) {
        Account account=userCreationPostVm.toEntity();
        UserRepresentation userRepresentation= createKeycloakUser(account);
        accountServiceImpl.saveAccount(account);
    }

    public UserRepresentation findByEmail(String email){
        UsersResource usersResource=keycloak.realm(realm).users();
        List<UserRepresentation> users=usersResource.searchByEmail(email,true);
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

    @Override
    public ResponseEntity<?> deleteByEmail(String email) {
        UserRepresentation user=findByEmail(email);
        UsersResource usersResource=keycloak.realm(realm).users();
        try(Response deletedResponse = usersResource.delete(user.getId())){
            accountServiceImpl.deleteByEmail(email);
            return mapResponseToResponseEntity(deletedResponse);
        }
    }

    @Override
    public ResponseEntity<?> deleteById(Object id) {
        UsersResource user=keycloak.realm(realm).users();
        try(Response response=user.delete((String)id)){
            return mapResponseToResponseEntity(response);
        }
    }

    public UserRepresentation createKeycloakUser(Account account) {
        UserRepresentation userRepresentation = mapAccount(account);
        try (Response response = keycloak.realm(realm).users().create(userRepresentation)) {
            log.info(response.readEntity(String.class));
            if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                throw new KeycloakException("Keycloak user creation failed");
            }else{
                return findByEmail(userRepresentation.getEmail());
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


        userRepresentation.setEnabled(!account.isLocked());
        userRepresentation.setEmailVerified(account.isVerified());
        //User's credentials information
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(account.getPassword());

        userRepresentation.setCredentials(List.of(credentialRepresentation));
        return userRepresentation;
    }
    public ResponseEntity<?> mapResponseToResponseEntity(Response response){
        return ResponseEntity
                .status(response.getStatus())
                .body(response.readEntity(String.class));
    }
}