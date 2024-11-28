package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.exception.DuplicatedException;
import com.sonnguyen.iamservice2.model.Role;
import com.sonnguyen.iamservice2.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RoleService {
    RoleRepository roleRepository;
    public boolean existsByName(String name){
        return roleRepository.existsByName(name);
    }
    public void save(Role role){
        if(existsByName(role.getName())){
            throw new DuplicatedException("Existed role has the same name");
        }
        roleRepository.save(role);
    }
    public void softDeleteById(Long roleId){
        roleRepository.softDeleteById(roleId);
    }
    public List<Role> findAllByIdsIn(Collection<Long> ids){
        return roleRepository.findAllByIdIn(ids);
    }
}
