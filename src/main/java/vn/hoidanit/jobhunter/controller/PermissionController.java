package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.res.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.PermissionService;
import vn.hoidanit.jobhunter.service.annotation.ApiMessage;
import vn.hoidanit.jobhunter.service.error.HandleNumber;
import vn.hoidanit.jobhunter.service.error.IdInvalidException;
import vn.hoidanit.jobhunter.service.error.RemoteEntityExist;
import vn.hoidanit.jobhunter.service.error.RemoteEntityNotFound;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }
    @PostMapping("/permissions")
    @ApiMessage("Create Permission")
    public ResponseEntity<Permission> createNewPermission(@RequestBody Permission permission) throws RemoteEntityExist{    
         return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.handleSavePermission(permission));
    }
    @PutMapping("/permissions")
  @ApiMessage("Update Permission")
  public ResponseEntity<Permission> updatePermission(@RequestBody Permission permission)
      throws RemoteEntityNotFound, RemoteEntityExist {
    return ResponseEntity.status(HttpStatus.OK)
        .body(this.permissionService.handleUpdatePermission(permission));
  }

   @GetMapping("/permissions")
    @ApiMessage("Fetch All Permissions")
    public ResponseEntity<ResultPaginationDTO> getAllPermission(@Filter Specification<Permission> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.permissionService.FetchAllPermission(spec, pageable));
    }

     @DeleteMapping("/permissions/{id}")
    @ApiMessage("DeleteU Permissions")
    public ResponseEntity<Void> deletePermission(@PathVariable("id") String id)
            throws IdInvalidException, RemoteEntityNotFound {
        // Check if the ID is numeric
        if (!HandleNumber.isNumberic(id)) {
            throw new IdInvalidException("The provided ID must be a numeric value.");
        }
        Long realId = Long.valueOf(id);

        // Validate if the ID is within an acceptable range
        if (realId >= 1500) {
            throw new IdInvalidException("Invalid ID. The ID must be less than 1500.");
        }

        // Call the service to handle user deletion
        this.permissionService.handleDeletePermission(realId);

        return ResponseEntity.ok(null);
    }
}
