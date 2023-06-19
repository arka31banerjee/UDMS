package com.udms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.udms.entity.UserSequence;

public interface UserSequenceRepository extends MongoRepository<UserSequence, String> {

	UserSequence findBySeqName(String seq_name);

}
