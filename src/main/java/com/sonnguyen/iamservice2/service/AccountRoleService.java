package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.exception.ResourceNotFoundException;
import com.sonnguyen.iamservice2.model.AccountRole;
import com.sonnguyen.iamservice2.repository.AccountRoleRepository;
import com.sonnguyen.iamservice2.viewmodel.RoleGetVm;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AccountRoleService {
    RoleService roleService;
    AccountServiceImpl accountService;
    AccountRoleRepository accountRoleRepository;

    public List<AccountRole> findAllByAccountId(Long accountId) {
        return accountRoleRepository.findAllByAccountId(accountId);
    }

    @Transactional
    public void updateAccountRoles(Long accountId, List<Long> roleIds) {
        //Check if accountId existed
        accountService.findById(accountId);
        //Check if list of roleIds existed
        List<RoleGetVm> existedRoles = roleService.findAllByIdIn(roleIds);
        if (existedRoles.size() != roleIds.size()) {
            throw new ResourceNotFoundException("One or more roles are invalid");
        }
        //Delete redundant role
        accountRoleRepository.deleteByAccountIdAndUpdateRole(accountId, roleIds);
        //Add new Role
        List<Long> accountRoleIds = accountRoleRepository
                .findAllByAccountId(accountId)
                .stream()
                .map(AccountRole::getRole_id).toList();
        List<AccountRole> accountRoles = roleIds.stream().filter(roleId -> !accountRoleIds.contains(roleId))
                .map((roleId_) -> new AccountRole(roleId_, accountId))
                .toList();
        accountRoleRepository.saveAll(accountRoles);
    }
}
