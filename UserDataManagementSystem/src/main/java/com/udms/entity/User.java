package com.udms.entity;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Document(collection = "userDataRepo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String _id;
    private long userId;
    private String firstName;
    private String lastName;
    private String email;
    @Setter(AccessLevel.NONE)
    private String application;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime lastUpdated;
    @Setter(AccessLevel.NONE)
    private String fileReference;
    private List<AdditionalInfo> infoTags;
	public void setFileReference(String fileReference) {
		this.fileReference = fileReference;
	}
	public void setApplication(String application) {
		this.application = (String) RequestContextHolder.getRequestAttributes()
				.getAttribute("appId",RequestAttributes.SCOPE_REQUEST);;
	}
}
