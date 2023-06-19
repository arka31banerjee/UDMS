package com.udms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.udms.entity.User;
@Transactional
public interface UserRepository extends MongoRepository<User, String> {

	boolean existsByEmail(String email);
	@Query("{'userId' : ?0, 'application' : ?1 }")
	Optional<User> findByUserIdAndApplication(long userId,String application);

	void deleteByUserId(long id);
	@Query("{'email' : ?0, 'application' : ?1 }")
	User findByEmailAndApplication(String email,String application);
	List<User> findByApplication(String appId);
}
