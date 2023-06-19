package com.udms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udms.entity.User;
import com.udms.entity.UserSequence;
import com.udms.repository.UserSequenceRepository;

@Service
public class SequenceGenerationService {

	private final UserSequenceRepository userSequenceRepository;

	@Autowired
	public SequenceGenerationService(UserSequenceRepository userSequenceRepository) {
		this.userSequenceRepository = userSequenceRepository;

	}

	public User setUserWithId(User user) {
		UserSequence sequence = userSequenceRepository.findBySeqName("USER_SEQUENCE");
		long seq_value = sequence.getValue();
		user.setUserId(seq_value);
		long seq_value_inc = sequence.getValue()+1;
		sequence.setValue(seq_value_inc);
		userSequenceRepository.save(sequence);
		return user;
	}
}
