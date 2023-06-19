package com.udms.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.udms.utility.ValidInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Schema(description = "Request object structure to onboard a user in UDMS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	@Schema(accessMode = AccessMode.READ_ONLY)
    private long userId;
	
    @NotBlank(message = "First name is required")
    @Schema(description = "First Name of User")
    private String firstName;
    
	@NotBlank(message = "Last name is required")
    @Schema(description = "Last Name of User")
    private String lastName;

    @Email(message = "Invalid email address")
    @Schema(description = "email address of User")
    private String email;
    
    @ValidInfo
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String,Object> infoMap;
    
}