package it.polito.ezgas;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;

import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.polito.ezgas.converter.UserConverter;
import it.polito.ezgas.dto.UserDto;
import it.polito.ezgas.dto.IdPw;
import it.polito.ezgas.dto.LoginDto;
import it.polito.ezgas.entity.User;
import it.polito.ezgas.repository.UserRepository;
import it.polito.ezgas.service.UserService;
import it.polito.ezgas.service.impl.UserServiceimpl;

import exception.InvalidLoginDataException;
import exception.InvalidUserException;
import static org.mockito.AdditionalAnswers.returnsFirstArg;

@RunWith(SpringRunner.class)
@SpringBootTest( classes = BootEZGasApplication.class)
public class UserServiceTests {
    
	@Autowired
	private UserService uService;
    
    private User mockUser = new User("mockUsernameUserSer", "mockPasswordUserSer", "mockEmailUserSer", 0);
    private UserDto mockUserDto = new UserDto(null, "mockUsernameUserSer", "mockPasswordUserSer", "mockEmailUserSer", 0, false);
    private List<User> mockUsers = new ArrayList<>();
    private List<UserDto> mockUsersDto = new ArrayList<>();

    Integer setupAddAndFindMockUser() {
        addMockUser();
        return findMockUserId();
    }
    
    void cleanUpDeleteMockUser() {
        deleteMockUser();
    }    
    
    void setupTestLogin() {
        addMockUser();
    }
    
    void cleanUpTestLogin() {
        deleteMockUser();
    }
    
UserService setupUserServiceWithMockRepo(){
        
    	Iterable<User> mockUsersIterable = (Iterable<User>) mockUsers;
        UserRepository mockRepository = mock(UserRepository.class);
        when(mockRepository.findByEmail(anyString())).thenReturn(mockUser);
        when(mockRepository.findOne(any(Integer.class))).thenReturn(mockUser);
        when(mockRepository.save(any(User.class))).then(returnsFirstArg());
        when(mockRepository.findAll()).thenReturn(mockUsersIterable);
        when(mockRepository.exists(any(Integer.class))).thenReturn(true);
        UserService userService = new UserServiceimpl(mockRepository, null);
    
        return userService;
        
    }
    
    UserService setupUserServiceWithMockRepoAndMockConverter(){
        
    	Iterable<User> mockUsersIterable = (Iterable<User>) mockUsers;
        UserRepository mockRepository = mock(UserRepository.class);
        when(mockRepository.findByEmail(anyString())).thenReturn(mockUser);
        when(mockRepository.findOne(any(Integer.class))).thenReturn(mockUser);
        when(mockRepository.save(any(User.class))).then(returnsFirstArg());
        when(mockRepository.findAll()).thenReturn(mockUsersIterable);
        UserConverter mockConverter = mock(UserConverter.class);
        when(mockConverter.convertToDto(any(User.class))).thenReturn(mockUserDto);
        when(mockConverter.convertToDtos(anyListOf(User.class))).thenReturn(mockUsersDto);
        when(mockConverter.convertToEntity(any(UserDto.class))).thenReturn(mockUser);
        UserService userService = new UserServiceimpl(mockRepository, mockConverter);
    
        return userService;
        
    }
    
    @Test
    public void testGetUserByIdNullIdStep1() {
    	
    	mockUser.setAdmin(false);
        
        UserService userService = setupUserServiceWithMockRepoAndMockConverter();
        
        try {
        	UserDto userDtoResult = userService.getUserById(null);
        	assert(userDtoResult == null);
        } catch (InvalidUserException e) {
        	System.out.println(e);
        }
        
        mockUser.setAdmin(null);
        
    }
    
    @Test
    public void testGetUserByIdNegativeIdStep1() {
    	
    	mockUser.setAdmin(false);
    	
    	UserService userService = setupUserServiceWithMockRepoAndMockConverter();
    	    	
    	Assertions.assertThrows(InvalidUserException.class,
    			() -> { userService.getUserById(-1); });
    	
    	mockUser.setAdmin(null);
    	
    }

    @Test
    public void testGetUserByIdValidIdStep1() {
    	
    	mockUser.setUserId(1);
    	mockUser.setAdmin(false);
    	mockUserDto.setUserId(1);
    	UserService userService = setupUserServiceWithMockRepoAndMockConverter();
        
        try {
        	UserDto userDtoResult = userService.getUserById(1);
        	assert(userDtoResult.getUserId().equals(mockUserDto.getUserId()));
        	assert(userDtoResult.getUserName().equals(mockUserDto.getUserName()));
        	assert(userDtoResult.getPassword().equals(mockUserDto.getPassword()));
        	assert(userDtoResult.getEmail().equals(mockUserDto.getEmail()));
        	assert(userDtoResult.getReputation().equals(mockUserDto.getReputation()));
        	assert(userDtoResult.getAdmin().equals(mockUserDto.getAdmin()));
        } catch (InvalidUserException e) {
        	System.out.println(e);
        }
        
        mockUser.setUserId(null);
    	mockUser.setAdmin(null);
    	mockUserDto.setUserId(null);
    	
    }
    
    @Test
    public void testGetUserByIdValidIdStep2() {
    	
    	mockUser.setUserId(1);
    	mockUser.setAdmin(false);
    	mockUserDto.setUserId(1);
        
        UserService userService = setupUserServiceWithMockRepo();
        
        try {
        	UserDto userDtoResult = userService.getUserById(1);
        	assert(userDtoResult.getUserId().equals(mockUserDto.getUserId()));
        	assert(userDtoResult.getUserName().equals(mockUserDto.getUserName()));
        	assert(userDtoResult.getPassword().equals(mockUserDto.getPassword()));
        	assert(userDtoResult.getEmail().equals(mockUserDto.getEmail()));
        	assert(userDtoResult.getReputation().equals(mockUserDto.getReputation()));
        	assert(userDtoResult.getAdmin().equals(mockUserDto.getAdmin()));
        } catch (InvalidUserException e) {
        	System.out.println(e);
        }
        
        mockUser.setUserId(null);
    	mockUser.setAdmin(null);
    	mockUserDto.setUserId(null);
    	
    }
    
    @Test
    public void testGetUserByIdValidIdStepFinal() {
    	
    	mockUser.setAdmin(false);
    	addMockUser();
    	mockUserDto.setUserId(findMockUserId());
    	
        try {
        	UserDto userDtoResult = uService.getUserById(findMockUserId());
        	assert(userDtoResult.getUserId().equals(mockUserDto.getUserId()));
        	assert(userDtoResult.getUserName().equals(mockUserDto.getUserName()));
        	assert(userDtoResult.getPassword().equals(mockUserDto.getPassword()));
        	assert(userDtoResult.getEmail().equals(mockUserDto.getEmail()));
        	assert(userDtoResult.getReputation().equals(mockUserDto.getReputation()));
        	assert(userDtoResult.getAdmin().equals(mockUserDto.getAdmin()));
        } catch (InvalidUserException e) {
        	System.out.println(e);
        }
        
        deleteMockUser();
        mockUserDto.setUserId(null);
        mockUser.setAdmin(null);
    	
    }
    
    @Test
    public void testSaveUserNullUserDtoStep1() {
    	    	
    	mockUser.setAdmin(false);
        
        UserService userService = setupUserServiceWithMockRepoAndMockConverter();
        
        try {
        	UserDto UserDtoResult = userService.saveUser(null);
        	assert(UserDtoResult == null);
        } catch (Exception e) {
        	System.out.println(e);
        }
        
        mockUser.setAdmin(null);
    	
    }
    
    @Test
    public void testSaveUserValidExistingUserDtoStep1() {
    	
    	mockUser.setUserId(1);
    	mockUser.setAdmin(false);
    	mockUserDto.setUserId(1);
    	
        UserService userService = setupUserServiceWithMockRepoAndMockConverter();
        
        try {
        	UserDto userDtoResult = userService.saveUser(mockUserDto);
        	assert(userDtoResult.getUserId().equals(mockUserDto.getUserId()));
        	assert(userDtoResult.getUserName().equals(mockUserDto.getUserName()));
        	assert(userDtoResult.getPassword().equals(mockUserDto.getPassword()));
        	assert(userDtoResult.getEmail().equals(mockUserDto.getEmail()));
        	assert(userDtoResult.getReputation().equals(mockUserDto.getReputation()));
        	assert(userDtoResult.getAdmin().equals(mockUserDto.getAdmin()));
        } catch (Exception e) {
        	System.out.println(e);
        }
        
        mockUser.setUserId(null);
    	mockUser.setAdmin(null);
    	mockUserDto.setUserId(null);
    	
    }
    
    @Test
    public void testSaveUserValidNewUserDtoStep1() {
    	
    	mockUser.setAdmin(false);
    	
    	UserRepository mockRepository = mock(UserRepository.class);
    	// to get into the if condition a null user is needed when searching by email
        when(mockRepository.findByEmail(anyString())).thenReturn(null);
        when(mockRepository.save(any(User.class))).then(returnsFirstArg());
        UserConverter mockConverter = mock(UserConverter.class);
        when(mockConverter.convertToEntity(any(UserDto.class))).thenReturn(mockUser);
        when(mockConverter.convertToDto(any(User.class))).thenReturn(mockUserDto);
        UserService userService = new UserServiceimpl(mockRepository, mockConverter);
        
        try {
        	UserDto userDtoResult = userService.saveUser(mockUserDto);
        	// used == because .equal() with null-null fails
        	assert(userDtoResult.getUserId() == mockUserDto.getUserId());
        	assert(userDtoResult.getUserName().equals(mockUserDto.getUserName()));
        	assert(userDtoResult.getPassword().equals(mockUserDto.getPassword()));
        	assert(userDtoResult.getEmail().equals(mockUserDto.getEmail()));
        	assert(userDtoResult.getReputation().equals(mockUserDto.getReputation()));
        	assert(userDtoResult.getAdmin().equals(mockUserDto.getAdmin()));
        } catch (Exception e) {
        	System.out.println(e);
        }
    	
    	mockUser.setAdmin(null);
    	
    }
    
    @Test
    public void testSaveUserValidNewUserDtoStep2() {
    	
    	mockUser.setAdmin(false);
    	
    	UserRepository mockRepository = mock(UserRepository.class);
    	// to get into the if condition a null user is needed when searching by email
        when(mockRepository.findByEmail(anyString())).thenReturn(null);
        when(mockRepository.save(any(User.class))).then(returnsFirstArg());
        UserService userService = new UserServiceimpl(mockRepository, null);
        
        try {
        	UserDto userDtoResult = userService.saveUser(mockUserDto);
        	assert(userDtoResult.getUserId() == mockUserDto.getUserId());
        	assert(userDtoResult.getUserName().equals(mockUserDto.getUserName()));
        	assert(userDtoResult.getPassword().equals(mockUserDto.getPassword()));
        	assert(userDtoResult.getEmail().equals(mockUserDto.getEmail()));
        	assert(userDtoResult.getReputation().equals(mockUserDto.getReputation()));
        	assert(userDtoResult.getAdmin().equals(mockUserDto.getAdmin()));
        } catch (Exception e) {
        	System.out.println(e);
        }
        
    	mockUser.setAdmin(null);
    	
    }
    
    @Test
    public void testSaveUserValidNewUserDtoStepFinal() {
    	
    	try {
    		UserDto userDtoResult = uService.saveUser(mockUserDto);
            assert(userDtoResult.getUserId().equals(findMockUserId()));
        	assert(userDtoResult.getUserName().equals(mockUserDto.getUserName()));
        	assert(userDtoResult.getPassword().equals(mockUserDto.getPassword()));
        	assert(userDtoResult.getEmail().equals(mockUserDto.getEmail()));
        	assert(userDtoResult.getReputation().equals(mockUserDto.getReputation()));
        	assert(userDtoResult.getAdmin().equals(mockUserDto.getAdmin()));
    	} catch (Exception e) {
    		System.out.println(e);
    	} finally{
        	deleteMockUser();
        }
    	
    	
    }
    
    @Test
    public void testGetAllUsersStep1() {
    	
    	mockUser.setUserId(1);
    	mockUser.setAdmin(false);
    	mockUserDto.setUserId(1);
    	mockUsers.add(mockUser);
    	mockUsersDto.add(mockUserDto);
    	List<UserDto> usersDtoResult = new ArrayList<>();
    	
    	UserService userService = setupUserServiceWithMockRepoAndMockConverter();
    	
    	try {
    		usersDtoResult = userService.getAllUsers();
    		for (int i=0; i<mockUsersDto.size(); i++) {
    			assert(usersDtoResult.get(i).getUserId().equals(mockUsersDto.get(i).getUserId()));
        		assert(usersDtoResult.get(i).getUserName().equals(mockUsersDto.get(i).getUserName()));
        		assert(usersDtoResult.get(i).getPassword().equals(mockUsersDto.get(i).getPassword()));
        		assert(usersDtoResult.get(i).getEmail().equals(mockUsersDto.get(i).getEmail()));
        		assert(usersDtoResult.get(i).getReputation().equals(mockUsersDto.get(i).getReputation()));
        		assert(usersDtoResult.get(i).getAdmin().equals(mockUsersDto.get(i).getAdmin()));
    		}
    	} catch (Exception e) {
    		System.out.println(e);
    	}
    	
    	mockUsers.clear();
    	mockUsersDto.clear();
    	mockUser.setUserId(null);
    	mockUser.setAdmin(null);
    	mockUserDto.setUserId(null);
    	
    }
    
    @Test
    public void testGetAllUsersStep2() {
    	
    	mockUser.setUserId(1);
    	mockUser.setAdmin(false);
    	mockUserDto.setUserId(1);
    	mockUsers.add(mockUser);
    	mockUsersDto.add(mockUserDto);
    	List<UserDto> usersDtoResult = new ArrayList<>();
    	
    	UserService userService = setupUserServiceWithMockRepo();
    	
    	try {
    		usersDtoResult = userService.getAllUsers();
    		for (int i=0; i<mockUsersDto.size(); i++) {
    			assert(usersDtoResult.get(i).getUserId().equals(mockUsersDto.get(i).getUserId()));
        		assert(usersDtoResult.get(i).getUserName().equals(mockUsersDto.get(i).getUserName()));
        		assert(usersDtoResult.get(i).getPassword().equals(mockUsersDto.get(i).getPassword()));
        		assert(usersDtoResult.get(i).getEmail().equals(mockUsersDto.get(i).getEmail()));
        		assert(usersDtoResult.get(i).getReputation().equals(mockUsersDto.get(i).getReputation()));
        		assert(usersDtoResult.get(i).getAdmin().equals(mockUsersDto.get(i).getAdmin()));
    		}
    	} catch (Exception e) {
    		System.out.println(e);
    	}
    	
    	mockUsers.clear();
    	mockUsersDto.clear();
    	mockUser.setUserId(null);
    	mockUser.setAdmin(null);
    	mockUserDto.setUserId(null);
    	
    }
    
    @Test
    public void testGetAllUsersStepFinal() {
    	
    	List<UserDto> usersDtoResult = new ArrayList<>();
    	List<UserDto> usersDtoDB = new ArrayList<>();
    	
    	try {
    		usersDtoResult = uService.getAllUsers();
    		usersDtoDB = listOfUsersDto();
    		for (int i=0; i<usersDtoDB.size(); i++) {
    			assert(usersDtoResult.get(i).getUserId().equals(usersDtoDB.get(i).getUserId()));
        		assert(usersDtoResult.get(i).getUserName().equals(usersDtoDB.get(i).getUserName()));
        		assert(usersDtoResult.get(i).getPassword().equals(usersDtoDB.get(i).getPassword()));
        		assert(usersDtoResult.get(i).getEmail().equals(usersDtoDB.get(i).getEmail()));
        		assert(usersDtoResult.get(i).getReputation().equals(usersDtoDB.get(i).getReputation()));
        		assert(usersDtoResult.get(i).getAdmin().equals(usersDtoDB.get(i).getAdmin()));
    		}
    	} catch (Exception e) {
    		System.out.println(e);
    	}
    	
    }
    
    @Test
    public void testDeleteUserNullIdStep1() {
    	
    	mockUser.setAdmin(false);
        
        UserService userService = setupUserServiceWithMockRepo();
        
        try {
        	assert(userService.deleteUser(null) == null);
        } catch (InvalidUserException e) {
        	System.out.println(e);
        }
        
        mockUser.setAdmin(null);
        
    }
    
    @Test
    public void testDeleteUserdNegativeIdStep1() {
    	
    	mockUser.setAdmin(false);
    	
    	UserService userService = setupUserServiceWithMockRepo();
    	    	
    	Assertions.assertThrows(InvalidUserException.class,
    			() -> { userService.deleteUser(-1); });
    	
    	mockUser.setAdmin(null);
    	
    }
    
    @Test
    public void testDeleteUserValidIdNotExistsStep1() {
    	
    	mockUser.setAdmin(false);
    	
    	UserRepository mockRepository = mock(UserRepository.class);
        when(mockRepository.exists(any(Integer.class))).thenReturn(false);
        UserService userService = new UserServiceimpl(mockRepository, null);
        
        try {
        	assert(userService.deleteUser(1) == false);        	
        } catch (InvalidUserException e) {
        	System.out.println(e);
        }
      
    	mockUser.setAdmin(null);
    	
    }
    
    @Test
    public void testDeleteUserValidIdExistsStep1() {
    	
    	mockUser.setUserId(1);
    	mockUser.setAdmin(false);
    	
    	UserService userService = setupUserServiceWithMockRepo();
        
        try {
        	assert(userService.deleteUser(1) == true);        	
        } catch (InvalidUserException e) {
        	System.out.println(e);
        }
        
        mockUser.setUserId(null);
    	mockUser.setAdmin(null);
    	
    }
    
    @Test
    public void testDeleteUserValidIdExistsStepFinal() {
    	
    	mockUser.setAdmin(false);
    	addMockUser();
    	
        
        try {
        	assert(uService.deleteUser(findMockUserId()) == true);        	
        } catch (InvalidUserException e) {
        	System.out.println(e);
        }
            	
    	deleteMockUser();
    	mockUser.setAdmin(null);
    	
    }
    
    @Test
    public void testLoginNullCredentialsStep1() {

        System.out.println("STARTING testLoginNullCredentialsStep1");

        UserService userService = setupUserServiceWithMockRepo();

        IdPw credentials = null;
        LoginDto retLoginDto;

        try {

            retLoginDto = userService.login(credentials);
            assertNull(retLoginDto);

        } catch (InvalidLoginDataException e) {
            e.printStackTrace();
        }

        System.out.println("STARTING testLoginNullCredentialsStep1");
            
    }

    @Test
    public void testLoginNullFieldsCredentialsStep1() {

        System.out.println("STARTING testLoginNullFieldsCredentialsStep1");

        UserService userService = setupUserServiceWithMockRepo();

        IdPw credentials = new IdPw(null, null);
        LoginDto retLoginDto;

        try {

            retLoginDto = userService.login(credentials);
            assertNull(retLoginDto);

        } catch (InvalidLoginDataException e) {
            e.printStackTrace();
        }
        

        System.out.println("STARTING testLoginNullFieldsCredentialsStep1");

    }

    @Test
    public void testLoginWrongCredentialsStep1() {

        System.out.println("STARTING testLoginWrongCredentialsStep1");

        UserService userService = setupUserServiceWithMockRepo();

        Assertions.assertThrows(
            InvalidLoginDataException.class, 
            () -> {
                    IdPw wrongCredentials = new IdPw(mockUser.getEmail(), "mockWrongPassword");
                    userService.login(wrongCredentials);
                  }
            );
            
        System.out.println("STARTING testLoginWrongCredentialsStep1");

    }

    // TODO change assertTrue to check for the userId?; ATTENTION mockUser does not have the id
    @Test
    public void testLoginCorrectCredentialsStep1() {
        
        System.out.println("STARTING testLoginCorrectCredentialsStep1");

        UserService userService = setupUserServiceWithMockRepo();
        IdPw credentials = new IdPw(mockUser.getEmail(), mockUser.getPassword());

        try {

            LoginDto retLoginDto = userService.login(credentials);
            assertNotNull(retLoginDto);
            assertTrue(retLoginDto.getEmail().equals(mockUser.getEmail()) && retLoginDto.getUserName().equals(mockUser.getUserName()));

        }catch (Exception e){
            System.out.println("SONO IL CATCH testLoginCorrectCredentialsStep1 " + e);
        }
        
        System.out.println("ENDING testLoginCorrectCredentialsStep1");
        
        return;
        
    }
    
    // TODO change assertTrue to check for the userId?; ATTENTION mockUser does not have the id
    @Test
    public void testLoginStepFinal() {
        
        System.out.println("STARTING testLoginStepFinal");

        setupTestLogin();
        
        IdPw credentials = new IdPw(mockUser.getEmail(), mockUser.getPassword());

        try {
            
            LoginDto retLoginDto = uService.login(credentials);
            assertNotNull("testLoginStepFinal: loginDto is null with correct credentials", retLoginDto);
            assertTrue(retLoginDto.getEmail().equals(mockUser.getEmail()) && retLoginDto.getUserName().equals(mockUser.getUserName()));

        }catch (Exception e){
            System.out.println("SONO IL CATCH testLoginStepFinal " + e);
        }finally {
            cleanUpTestLogin();
        }
        
        System.out.println("ENDING testLoginStepFinal");
        
        return;
        
    }

    @Test
    public void testIncreaseUserReputationUserIdNullStep1(){
        
        System.out.println("STARTING testIncreaseUserReputationUserIdNullStep1");
        
        UserService userService = setupUserServiceWithMockRepo();
        Integer userId = null;

        try {

            Integer retInteger = userService.increaseUserReputation(userId);
            assertNull("testIncreaseUserReputationUserIdNullStep1: ret value is not null with a null userId",
                        retInteger);

        }catch (Exception e) {
            System.out.println(e);
        }
        
        System.out.println("ENDING testIncreaseUserReputationUserIdNullStep1");
         
    }

    @Test
    public void testIncreaseUserReputationUserIdPositiveStep1(){
        
        System.out.println("STARTING testIncreaseUserReputationUserIdPositiveStep1");

        UserService userService = setupUserServiceWithMockRepo();
        Integer userId = setupAddAndFindMockUser();
        
        try {

            Integer retInteger = userService.increaseUserReputation(userId);
            assertTrue("testIncreaseUserReputationUserIdPositiveStep1: reputation is not 1",
                        retInteger == 1);

        }catch (Exception e) {
            System.out.println(e);
        }
        
        cleanUpDeleteMockUser();
        System.out.println("ENDING testIncreaseUserReputationUserIdPositiveStep1");
        
    }
    
    @Test
    public void testIncreaseUserReputationUserIdNegativeStep1(){
        
        System.out.println("STARTING testIncreaseUserReputationUserIdNegativeStep1");

        Assertions.assertThrows(
                    InvalidUserException.class, 
                    () -> {
                            UserService userService = setupUserServiceWithMockRepo();
                            userService.increaseUserReputation(-1);
                          },
                    "testIncreaseUserReputationUserIdNegativeStep1: didn't throw an exception with negative userId"
                    );
        
        System.out.println("ENDING testIncreaseUserReputationUserIdNegativeStep1");
    
    }

    @Test
    public void testIncreaseUserReputationFinal() {
        
        Integer userId = setupAddAndFindMockUser();
        
        Integer retVal;

        try {

            retVal = uService.increaseUserReputation(userId);
            assertTrue(retVal == 1);
        
        } catch (InvalidUserException e) {
            e.printStackTrace();
        }
        
        cleanUpDeleteMockUser();
        
    }
    
    
    @Test
    public void testDecreaseUserReputationUserIdNullStep1(){
        
        System.out.println("STARTING testDecreaseUserReputationUserIdNullStep1");
        
        UserService userService = setupUserServiceWithMockRepo();
        Integer userId = null;

        try {

            Integer retInteger = userService.decreaseUserReputation(userId);
            assertNull("testDecreaseUserReputationUserIdNullStep1: ret value is not null with a null userId",
                        retInteger);

        }catch (Exception e) {
            System.out.println(e);
        }
        
        System.out.println("ENDING testDecreaseUserReputationUserIdNullStep1");
         
    }

    @Test
    public void testDecreaseUserReputationUserIdPositiveStep1(){
        
        System.out.println("STARTING testDecreaseUserReputationUserIdPositiveStep1");

        UserService userService = setupUserServiceWithMockRepo();
        Integer userId = setupAddAndFindMockUser();
        
        try {

            Integer retInteger = userService.decreaseUserReputation(userId);
            assertTrue("testDecreaseUserReputationUserIdPositiveStep1: reputation is not -1",
                        retInteger == -1);
                        
        }catch (Exception e) {
            System.out.println(e);
        }
        
        cleanUpDeleteMockUser();
        System.out.println("ENDING testDecreaseUserReputationUserIdPositiveStep1");
        
    }
    
    @Test
    public void testDecreaseUserReputationUserIdNegativeStep1(){
        
        System.out.println("STARTING testDecreaseUserReputationUserIdNegativeStep1");

        Assertions.assertThrows(
                    InvalidUserException.class, 
                    () -> {
                            UserService userService = setupUserServiceWithMockRepo();
                            userService.decreaseUserReputation(-1);
                          },
                    "testDecreaseUserReputationUserIdNegativeStep1: didn't raise an exception with a negative userId"
                    );
    
        System.out.println("ENDING testDecreaseUserReputationUserIdNegativeStep1");
        
    }
    
    @Test
    public void testDecreaseUserReputationFinal() {
        
        Integer userId = setupAddAndFindMockUser();
        
        Integer retVal;
        try {

            retVal = uService.decreaseUserReputation(userId);
            assertTrue(retVal == -1);

        } catch (InvalidUserException e) {
            e.printStackTrace();
        }
        
        cleanUpDeleteMockUser();
        
    }

    void addMockUser() {
        
        try {
            
            Connection conn = DriverManager.getConnection("jdbc:h2:./data/memo", "sa", "password");
            
            String queryAddMockUser = "INSERT INTO USER (USER_NAME, PASSWORD, EMAIL, REPUTATION, ADMIN) "
                                    + "VALUES ('" + mockUser.getUserName() + "', '" 
                                                  + mockUser.getPassword() + "', '"
                                                  + mockUser.getEmail() + "', " 
                                                  + mockUser.getReputation() + ", " 
                                                  + mockUser.getAdmin() + ")";
                
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(queryAddMockUser);
            
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    Integer findMockUserId() {
        
        Integer retVal = -1;
        
        try {
            
            Connection conn = DriverManager.getConnection("jdbc:h2:./data/memo", "sa", "password");
            String queryFindMockUserId = "SELECT USER_ID " + 
                                         "FROM USER " +
                                         "WHERE EMAIL = '" + mockUser.getEmail() + "'";
         
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryFindMockUserId);
            
            if(rs.next()) {
               retVal = rs.getInt("USER_ID");
            }
            
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return retVal;
        
    }
    
    void deleteMockUser() {
        
        try {
            
            Connection conn = DriverManager.getConnection("jdbc:h2:./data/memo", "sa", "password");
            
            String queryDeleteMockUser = "DELETE FROM USER WHERE EMAIL = '" + mockUser.getEmail() + "'";
            
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(queryDeleteMockUser);
            
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

List<UserDto> listOfUsersDto() throws SQLException {
        
        List<UserDto> usersDto = new ArrayList<>();
        Connection conn = DriverManager.getConnection("jdbc:h2:./data/memo", "sa", "password");
        Statement stmt = null;
        String queryFindAll = "SELECT * FROM USER ";
        
        try {
        	stmt = conn.createStatement();
    		ResultSet rs = stmt.executeQuery(queryFindAll);
    		
    		while(rs.next()) {
    			int userId = rs.getInt("USER_ID");
    		    boolean isAdmin = rs.getBoolean("ADMIN");
    		    String password = rs.getString("PASSWORD");
    		    int reputation = rs.getInt("REPUTATION");
    		    String username = rs.getString("USER_NAME");
    		    String email = rs.getString("EMAIL");
    		    usersDto.add(new UserDto(userId, username, password, email, reputation, isAdmin));    			
    		}            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	stmt.close();           
            conn.close();
        }
        
        return usersDto;
        
    }
    
}
