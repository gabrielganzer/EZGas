package it.polito.ezgas;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.polito.ezgas.converter.UserConverter;
import it.polito.ezgas.dto.UserDto;
import it.polito.ezgas.entity.User;

@RunWith(SpringRunner.class)
@SpringBootTest

public class UserConverterTests {
	User u1 = new User("User1", "pass1", "user1@polito.it", 3);
	User u2 = new User("User2", "pass2", "user2@polito.it", 4);
	User uTest;
	List<User> userArray = new ArrayList<>();
	
	UserDto uDto = new UserDto(3, "User3", "pass3", "user3@polito.it", 5, false);
	UserDto uDtoTest;
	List<UserDto> userDtoArray = new ArrayList<>();
	
	UserConverter uConverter = new UserConverter();
	
	@Test
	public void testConvertToDto() {
		uDtoTest = uConverter.convertToDto(u1);
		assert(uDtoTest.getUserName().equals(u1.getUserName()));
		assert(uDtoTest.getPassword().equals(u1.getPassword()));
		assert(uDtoTest.getEmail().equals(u1.getEmail()));
		assert(uDtoTest.getReputation()==u1.getReputation());
	}
	
	@Test 
	public void testConvertToDtoNull() {
		uDtoTest = uConverter.convertToDto(null);
		assertNull(uDtoTest);
	}
	
	@Test
	public void testConvertToEntity() {
		uTest = uConverter.convertToEntity(uDto);
		assert(uTest.getUserName().equals(uDto.getUserName()));
		assert(uTest.getPassword().equals(uDto.getPassword()));
		assert(uTest.getEmail().equals(uDto.getEmail()));
		assert(uTest.getReputation()==uDto.getReputation());
	}
	
	@Test
	public void testConvertToEntityNull() {
		uTest = uConverter.convertToEntity(null);
		assertNull(null);
	}
	
	@Test
	public void testConvertToDtos() {
		userArray.add(u1);
		userArray.add(u2);
		
		userDtoArray = uConverter.convertToDtos(userArray);
		int i=0;
		for(UserDto res : userDtoArray) {
			assert(res.getUserName().equals(userArray.get(i).getUserName()));
			assert(res.getPassword().equals(userArray.get(i).getPassword()));
			assert(res.getEmail().equals(userArray.get(i).getEmail()));
			assert(res.getReputation()==userArray.get(i).getReputation());
			i++;
		}
	}
	
	@Test
	public void testConvertToDtosNull() {
		userDtoArray = uConverter.convertToDtos(null);
		for(UserDto res : userDtoArray) 
			assertNull(res);
		assert(userDtoArray.size()==0);
	}
	
}
