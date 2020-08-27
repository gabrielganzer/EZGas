package it.polito.ezgas.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import exception.InvalidLoginDataException;
import exception.InvalidUserException;
import it.polito.ezgas.converter.UserConverter;
import it.polito.ezgas.dto.IdPw;
import it.polito.ezgas.dto.LoginDto;
import it.polito.ezgas.dto.UserDto;
import it.polito.ezgas.entity.User;
import it.polito.ezgas.repository.UserRepository;
import it.polito.ezgas.service.UserService;

/**
 * Created by softeng on 27/4/2020.
 */
@Service
public class UserServiceimpl implements UserService {

	private UserRepository userRepository;
	private UserConverter userConverter;
	
	public UserServiceimpl(UserRepository repository, UserConverter converter) {
	    this.userRepository = repository;
	    if (converter == null) 
	    	this.userConverter = new UserConverter();
	    else
	    	this.userConverter = converter;
	}
	
	@Override
	public UserDto getUserById(Integer userId) throws InvalidUserException {
	    
	    if(userId == null)
	        return null;
		
        if(userId < 0) {
            String invalidType = "negative";
            throw new InvalidUserException("Function deleteUser(): userId is " + invalidType);
        }
        
		User aUser = userRepository.findOne(userId);
		
		return userConverter.convertToDto(aUser);
		
	}

	@Override
	public UserDto saveUser(UserDto userDto) {
	    
        if(userDto == null)
            return null;

        UserDto uDto = userDto;

	    // saveUser is called to modify an account
	    if(uDto.getUserId() != null) {
	        // update the existing account only if there is an account in the DB and their IDs match
	        User aUser = userRepository.findOne(uDto.getUserId());
	        if(aUser != null) {
                User userByEmail = userRepository.findByEmail(uDto.getEmail());
                if(userByEmail == null || (userByEmail != null && aUser.getUserId() == userByEmail.getUserId())) {
                    User user = userRepository.save(userConverter.convertToEntity(uDto));
                    uDto = userConverter.convertToDto(user);
                }
	        }
	    }
	    // saveUser is called to create a new account
	    else {
	        User aUser = userRepository.findByEmail(uDto.getEmail());
	        if(aUser == null) {
	            uDto.setReputation(0);
	            User user = userRepository.save(userConverter.convertToEntity(uDto));
                uDto = userConverter.convertToDto(user);
	        }
	    }
	    
	    return uDto;
		
	}

	@Override
	public List<UserDto> getAllUsers() {
	    
	    List<User> listUsers = new ArrayList<>();
		userRepository.findAll().forEach(user -> listUsers.add(user));
		return userConverter.convertToDtos(listUsers);
	
	}

	@Override
	public Boolean deleteUser(Integer userId) throws InvalidUserException {
	    
	    if(userId == null)
	        return null;
	    
		if(userId < 0) {
		    String invalidType = "negative";
			throw new InvalidUserException("Function deleteUser(): userId is " + invalidType);
		}
		
		if(userRepository.exists(userId)) {
			userRepository.delete(userId);
			return true;
		}
		
		// in case the user was not found (null or not existing account) return null or false?
		return false;
		
	}
		
	//TODO (francesco) da implementare token di loginDto
	@Override
	public LoginDto login(IdPw credentials) throws InvalidLoginDataException {
	    
	    if(credentials == null)
	        return null;
	    
	    if(credentials.getUser() == null || credentials.getPw() == null) {
	       return null;
	    }
	    
		LoginDto loginDto = null;
		Boolean isSuccess = false;
		String errorMessage = "Function login(): ";
		
		User aUser = userRepository.findByEmail(credentials.getUser());
		if(aUser != null) {
			 if(aUser.getPassword().compareTo(credentials.getPw()) == 0) {
				 loginDto = new LoginDto(aUser.getUserId(), aUser.getUserName(), "TODO", aUser.getEmail(), aUser.getReputation());
				 loginDto.setAdmin(aUser.getAdmin());
				 isSuccess = true;
			 }
			 else {
			     errorMessage = errorMessage.concat("password invalid");
			 }
		}
		else {
		    errorMessage = errorMessage.concat("email invalid");
		}
		
		if(!isSuccess)
		    throw new InvalidLoginDataException(errorMessage);
		
		return loginDto;
		
	}

	@Override
	public Integer increaseUserReputation(Integer userId) throws InvalidUserException {
        
        if(userId == null)
            return null;

        if(userId < 0) {
            String invalidType = "negative";
            throw new InvalidUserException("Function increaseUserReputation(): userId is " + invalidType);
        }
	    
	    Integer newReputation = null;
		User user = userRepository.findOne(userId);
		if(user != null) {
			newReputation = user.getReputation();
			if(newReputation < 5) {
    			newReputation += 1;
    			user.setReputation(newReputation);
    			userRepository.save(user);
			}
		}
        else{
            throw new InvalidUserException("Function increaseUserReputation(): user not found");
        }
		
		return newReputation;
		
	}

	@Override
	public Integer decreaseUserReputation(Integer userId) throws InvalidUserException {

        if(userId == null)
            return null;
		
	    if(userId < 0) {
		    String invalidType = "negative";
			throw new InvalidUserException("Function decreaseUserReputation(): userId is " + invalidType);
		}
	    
        Integer newReputation = null;
        User user = userRepository.findOne(userId);
        if(user != null) {
            newReputation = user.getReputation();
            if(newReputation > -5) {
                newReputation -= 1;
                user.setReputation(newReputation);
                userRepository.save(user);
            }
        }
        else{
            throw new InvalidUserException("Function decreaseUserReputation(): user not found");
        }
        
		return newReputation;
		
	}
	
}
