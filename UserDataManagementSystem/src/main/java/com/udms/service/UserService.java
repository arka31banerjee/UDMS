package com.udms.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.udms.dto.UserDTO;
import com.udms.entity.AdditionalInfo;
import com.udms.entity.User;
import com.udms.exception.ValidationAndBusinessException;
import com.udms.repository.UserRepository;
import com.udms.utility.UdmsUtility;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final SequenceGenerationService sequenceGenerationService;
	private final ModelMapper mapper;

	@Autowired
	public UserService(UserRepository userRepository,SequenceGenerationService sequenceGenerationService,ModelMapper mapper) {
		this.userRepository = userRepository;
		this.sequenceGenerationService = sequenceGenerationService;
		this.mapper = mapper;
	}
	
	@Transactional(readOnly = true)
	public List<UserDTO> getAllUsers(String appId) {
		List<User> users = userRepository.findByApplication(appId);
		if(users.isEmpty()) {
			throw new ValidationAndBusinessException("No User present in DB for appId : "+UdmsUtility.fetchAppName());
		}
		List<UserDTO> dtos = users.parallelStream().map(user -> convertToDTO(user)).collect(Collectors.toList());
		return dtos;
	}
	
	@Transactional(readOnly = true)
	public UserDTO getUserById(long userId) {
		User user = userRepository.findByUserIdAndApplication(userId,UdmsUtility.fetchAppName()).orElse(null);
		if (user==null) {
			throw new ValidationAndBusinessException("No Such User Present with ID : "+userId+" for appId : "+UdmsUtility.fetchAppName());
		}
		UserDTO dto = convertToDTO(user);
		return dto;
	}

	public UserDTO createUser(UserDTO userDTO, MultipartFile file) {
		User user = convertToEntity(userDTO);
		if (!file.isEmpty()) {
			String fileReference = saveFile(file);
			if(!fileReference.isEmpty())
				user.setFileReference(fileReference);
		}
		if (userRepository.findByEmailAndApplication(userDTO.getEmail(),UdmsUtility.fetchAppName())!=null) {
			throw new ValidationAndBusinessException("Email ID already exists for appId : "+UdmsUtility.fetchAppName());
		}
		User userWithId = sequenceGenerationService.setUserWithId(user);
		User savedUser = userRepository.save(userWithId);
		UserDTO dto = convertToDTO(savedUser);
		return dto;
	}

	private String saveFile(MultipartFile file) {
		try {
			String filename = UUID.randomUUID().toString().subSequence(0, 5) + "-" + file.getOriginalFilename();
			String directory = "C:\\Temp\\";
			String filePath = directory + filename;
			file.transferTo(new File(filePath));
			return filePath;
		} catch (IOException e) {
			throw new RuntimeException("Failed to save the file : " +e.getMessage());
		}
	}

	public UserDTO updateUser(long id, UserDTO userDTO) {
		User existingUser = userRepository.findByUserIdAndApplication(id,UdmsUtility.fetchAppName()).orElse(null);

		if (existingUser==null) {
			throw new ValidationAndBusinessException("No Such User Present with ID : "+id+" for appId : "+UdmsUtility.fetchAppName());
		}
		existingUser.setFirstName(userDTO.getFirstName());
		existingUser.setLastName(userDTO.getLastName());
		existingUser.setEmail(userDTO.getEmail());
		existingUser.setInfoTags(convertToAdditionalInfoList(userDTO.getInfoMap()));
		User updatedUser = userRepository.save(existingUser);
		UserDTO updatedDto =convertToDTO(updatedUser);

		return updatedDto;
	}
	
	public void deleteUser(long id) {
		userRepository.deleteByUserId(id);
	}

	private User convertToEntity(UserDTO userDTO) {
		TypeMap<UserDTO, User> typeMap = mapper.getTypeMap(UserDTO.class, User.class);
		if (typeMap == null) {
			mapper.createTypeMap(UserDTO.class, User.class)
			.addMappings(mapper -> mapper.skip(User::setInfoTags));
		}
		User user = mapper.map(userDTO, User.class);
		user.setInfoTags(convertToAdditionalInfoList(userDTO.getInfoMap()));
		return user;
	}

	private List<AdditionalInfo> convertToAdditionalInfoList(Map<String, Object> infoMap) {
		List<String> validParams = Arrays.asList("age","occupation","contact","likings");
		Map<String, Object> filteredMap = infoMap.entrySet().stream().filter(e -> validParams.contains(e.getKey()))
				.collect(HashMap::new, (m, entry) -> m.put(entry.getKey(), entry.getValue()), Map::putAll);
		return filteredMap.entrySet()
				.stream()
				.map(entry -> new AdditionalInfo(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());
	}

	private UserDTO convertToDTO(User user) {
		TypeMap<User, UserDTO> typeMap = mapper.getTypeMap(User.class, UserDTO.class);
		if (typeMap == null) {
			mapper.createTypeMap(User.class, UserDTO.class)
			.addMappings(mapper -> mapper.skip(UserDTO::setInfoMap));
		}
		UserDTO userDTO = mapper.map(user, UserDTO.class);
		userDTO.setInfoMap(convertToInfoMap(user.getInfoTags()));
		return userDTO;
	}

	private Map<String, Object> convertToInfoMap(List<AdditionalInfo> infoTags) {
		return infoTags!=null?infoTags.stream()
				.collect(Collectors.toMap(AdditionalInfo::getInfoKey, AdditionalInfo::getInfoValue)):null;
	}

	public Resource downloadDoc(long id) {
		User userInfo = userRepository.findByUserIdAndApplication(id,UdmsUtility.fetchAppName()).orElse(null);
		if(userInfo==null) throw new ValidationAndBusinessException("No Doc found for Id "+id+" APPID :"+UdmsUtility.fetchAppName());
		Resource fileResource = new FileSystemResource(userInfo.getFileReference());
		if (fileResource.exists())
			return fileResource;
		else
			throw new ValidationAndBusinessException("No Resource found for Id : "+id); 	
	}

}
