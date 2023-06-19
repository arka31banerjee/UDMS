package com.udms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.udms.aspect.LogExecutionTime;
import com.udms.dto.UserDTO;
import com.udms.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
		name = "Create/Update/Search/Delete UserDTO",
		description = "REST-APIs to manage CRUD operations on UDMS application"
	)
@RestController
@RequestMapping("/udms/{appId}/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    @LogExecutionTime
    @Operation(summary = "Search All Users",description = "Fetch List of Users from DB")
    @ApiResponse(responseCode = "200",description = "200 - OK")
    public List<UserDTO> getAllUsers(@PathVariable("appId") String appId) {
        return userService.getAllUsers(appId);
    }

    @GetMapping("/{id}")
    @LogExecutionTime
    @Operation(summary = "Search a USER",description = "Fetch USER by userId and APPID")
    @ApiResponse(responseCode = "200",description = "200 - OK")
    public ResponseEntity<UserDTO> getUserById(@PathVariable long id,@PathVariable("appId") String appId) {
        UserDTO UserDTO = userService.getUserById(id);
        if (UserDTO != null) {
            return ResponseEntity.ok(UserDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value="/save",consumes = "multipart/form-data",produces = "application/json")
    @LogExecutionTime
    @Operation(summary = "Save User",description = "Save User and file reference in DB")
    @ApiResponse(responseCode = "200",description = "201 - CREATED")
    public ResponseEntity<UserDTO> createUser(@Validated @RequestPart("dto") UserDTO userDTO,
    		@RequestPart("file") MultipartFile file,@PathVariable("appId") String appId) {
        UserDTO createdUser = userService.createUser(userDTO,file);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    
    @GetMapping("/download/{id}")
    @LogExecutionTime
    @Operation(summary = "Download User's Doc",description = "Download File")
    @ApiResponse(responseCode = "200",description = "200 - OK")
    public ResponseEntity<Resource> downloadFile(@PathVariable long id,@PathVariable("appId") String appId) {
    	Resource fileResource = userService.downloadDoc(id);
    	String fileName = fileResource.getFilename();
    	String originalFileName = fileName.substring(6);
    	return ResponseEntity.ok()
    			.contentType(MediaType.parseMediaType("application/octet-stream"))
    			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalFileName + "\"")
    			.body(fileResource);
    }

    @PutMapping("/update/{id}")
    @LogExecutionTime
    @Operation(summary = "Update User",description = "Update User's attributre")
    @ApiResponse(responseCode = "200",description = "200 - OK")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable long id,
            @Validated @RequestBody UserDTO userDTO,@PathVariable("appId") String appId
    ) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    @LogExecutionTime
    @Operation(summary = "Delete User",description = "Delete User permanently")
    @ApiResponse(responseCode = "200",description = "200 - OK")
    public ResponseEntity<String> deleteUser(@PathVariable long id,@PathVariable("appId") String appId) {
        UserDTO UserDTO = userService.getUserById(id);
        if (UserDTO != null) {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted with ID : "+id);
        }
        return ResponseEntity.notFound().build();
    }
}
