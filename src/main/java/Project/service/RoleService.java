package Project.service;

import Project.domain.Role;
import Project.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "roleService")
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findByName(String name) {
        Role role = roleRepository.findByName(name);
        return role;
    }
    public Role CreateRole(Role role) {
        role = roleRepository.save(role);
        return role;
    }
}
