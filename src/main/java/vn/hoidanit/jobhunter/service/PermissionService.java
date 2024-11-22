package vn.hoidanit.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.res.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;
import vn.hoidanit.jobhunter.service.error.RemoteEntityExist;
import vn.hoidanit.jobhunter.service.error.RemoteEntityNotFound;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    public PermissionService(RoleRepository roleRepository,PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }
    public Permission handleSavePermission(Permission permission) throws RemoteEntityExist{
        Permission dbPermission = this.permissionRepository.findByApiPathAndMethodAndModule(permission.getApiPath(),permission.getMethod(),permission.getModule());
        if(dbPermission != null){
            throw new RemoteEntityExist("Permission already exists");
        }
        return this.permissionRepository.save(permission);
    }
    public Permission handleUpdatePermission(Permission updatedPermission) throws RemoteEntityExist,RemoteEntityNotFound{
        Optional<Permission> dbPermission = this.permissionRepository.findById(updatedPermission.getId());

        if(!dbPermission.isPresent()){
            throw new RemoteEntityNotFound("Permission not found");
        }
        Permission uniquePermission = this.permissionRepository.findByApiPathAndMethodAndModule(updatedPermission.getApiPath(), updatedPermission.getMethod(), updatedPermission.getModule());
        if(uniquePermission != null){
            throw new RemoteEntityExist("Permission already exists");
        }
        Permission permission = dbPermission.get();
        permission.setName(updatedPermission.getName());
        permission.setApiPath(updatedPermission.getApiPath());
        permission.setMethod(updatedPermission.getMethod());
        permission.setModule(permission.getModule());
        return this.permissionRepository.save(permission);
    }
    
  public ResultPaginationDTO FetchAllPermission(Specification spec, Pageable pageable) {
    Page<Permission> pagePermission = this.permissionRepository.findAll(spec, pageable);
    ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

    meta.setPage(pagePermission.getNumber() + 1);
    meta.setPageSize(pagePermission.getNumberOfElements());
    meta.setPages(pagePermission.getTotalPages());
    meta.setTotal(pagePermission.getTotalElements());
    ResultPaginationDTO res = new ResultPaginationDTO();
    res.setMeta(meta);
    res.setResult(pagePermission.getContent());
    return res;
  }


  public void handleDeletePermission(long id) throws RemoteEntityNotFound{
    Optional<Permission> dbPermission = this.permissionRepository.findById(id);

        if(!dbPermission.isPresent()){
            throw new RemoteEntityNotFound("Permission not found");
        }
        Permission permission = dbPermission.get();
        Role role = permission.getRoles().get(0);
        permission.getRoles().remove(role);
        role.getPermissions().remove(permission);

        this.roleRepository.save(role);
        this.permissionRepository.deleteById(id);

  }
}
