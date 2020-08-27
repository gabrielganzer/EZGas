package it.polito.ezgas.converter;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.polito.ezgas.dto.UserDto;
import it.polito.ezgas.entity.User;

@Component
public class UserConverter implements CustomConverter<User, UserDto>{

	@Override
	public UserDto convertToDto(User aUser) {
	    
	    UserDto userDto = null;
	    
	    if(aUser != null) {
	        userDto = new UserDto(
	                        aUser.getUserId(), 
	                        aUser.getUserName(), 
	                        aUser.getPassword(),
	                        aUser.getEmail(), 
	                        aUser.getReputation(), 
	                        aUser.getAdmin()
                            );
	    }
	    
		return userDto;
		
	}

	@Override
	public User convertToEntity(UserDto aUserDto) {
		
		User user = null;
		
		if(aUserDto != null) {
			user = new User(
			                aUserDto.getUserName(),
			                aUserDto.getPassword(),
			                aUserDto.getEmail(),
			                aUserDto.getReputation()
			                );
			if(aUserDto.getAdmin() != null) {
				user.setAdmin(aUserDto.getAdmin());
			}
			if(aUserDto.getUserId() != null) {
				user.setUserId(aUserDto.getUserId());
			}
		}
		
		return user;
		
	}

	@Override
	public List<UserDto> convertToDtos(List<User> listUsers) {
	    
		List<UserDto> listDtos = new ArrayList<UserDto>();
		UserDto newUserDto;
		
		if(listUsers != null) {
    		for(User user: listUsers) {
    			newUserDto = convertToDto(user);
    			listDtos.add(newUserDto);
    		}
		}
		
		return listDtos;
		
	}

}
