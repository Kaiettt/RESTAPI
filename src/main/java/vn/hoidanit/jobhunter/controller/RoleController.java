package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.service.RoleService;
import vn.hoidanit.jobhunter.service.annotation.ApiMessage;
import vn.hoidanit.jobhunter.service.error.HandleNumber;
import vn.hoidanit.jobhunter.service.error.IdInvalidException;
import vn.hoidanit.jobhunter.service.error.RemoteEntityExist;
import vn.hoidanit.jobhunter.service.error.RemoteEntityNotFound;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleSerice;
    public RoleController(RoleService roleSerice) {
        this.roleSerice = roleSerice;
    }
    @PostMapping("/roles")
    @ApiMessage("Create Role")
    public ResponseEntity<Role> createNewRole(@Valid @RequestBody Role newRole) throws RemoteEntityExist{    
         return ResponseEntity.status(HttpStatus.CREATED).body(this.roleSerice.handleSaveRole(newRole));
    }
    
  @PutMapping("/roles")
  @ApiMessage("Update Role")
  public ResponseEntity<Role> updateRole(@RequestBody Role updatedRole)
      throws RemoteEntityNotFound {
    return ResponseEntity.status(HttpStatus.OK)
        .body(this.roleSerice.handleUpdateRole(updatedRole));
  }
    @DeleteMapping("/roles/{id}")
    @ApiMessage("Delete Role")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") String id)
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
        this.roleSerice.handleDeleteRole(realId);

        return ResponseEntity.ok(null);
    }

}
