package com.udms.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "sequence")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSequence {
    @Id
    private String id;
    @Field("seq_name")
    private String seqName;
    private long value;
}