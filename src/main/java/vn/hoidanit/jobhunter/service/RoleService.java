package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;
import vn.hoidanit.jobhunter.service.error.RemoteEntityExist;
import vn.hoidanit.jobhunter.service.error.RemoteEntityNotFound;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    public RoleService(RoleRepository roleRepository,PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }
    public Role handleSaveRole(Role newRole) throws RemoteEntityExist{
        if(this.roleRepository.findByName(newRole.getName()) != null){
            throw new RemoteEntityExist("Role already exists");
        }
        List<Long> id = newRole.getPermissions()
            .stream().map(x -> x.getId())
            .collect(Collectors.toList());
        List<Permission> permissions = this.permissionRepository.findByIdIn(id);
        newRole.setPermissions(permissions);
        return this.roleRepository.save(newRole);
    }
    public Role handleUpdateRole(Role updatedRole) throws RemoteEntityNotFound{
        Optional<Role> optionalRole = this.roleRepository.findById(updatedRole.getId());
        if(!optionalRole.isPresent()){
            throw new RemoteEntityNotFound("Role not found");
        }
        Role dbRole = optionalRole.get();
        List<Long> id = updatedRole.getPermissions()
            .stream().map(x -> x.getId())
            .collect(Collectors.toList());
            List<Permission> permissions = this.permissionRepository.findByIdIn(id);
            dbRole.setPermissions(permissions);
            return this.roleRepository.save(dbRole); 
    }

    public void handleDeleteRole(long id) throws RemoteEntityNotFound{
        Optional<Role> optionalRole = this.roleRepository.findById(id);
        if(!optionalRole.isPresent()){
            throw new RemoteEntityNotFound("Role not found");
        }
        this.roleRepository.deleteById(id);
    }

    public Role getRoleById(long id){
        Optional<Role> optionalRole = this.roleRepository.findById(id);
        if(!optionalRole.isPresent()){
            return null;
        }
        return optionalRole.get();
    }
}
