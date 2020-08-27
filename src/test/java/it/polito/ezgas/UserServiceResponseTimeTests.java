package it.polito.ezgas;

import static org.junit.Assert.assertTrue;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Test;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import it.polito.ezgas.dto.IdPw;
import it.polito.ezgas.dto.UserDto;
import it.polito.ezgas.entity.User;
import it.polito.ezgas.service.UserService;
import exception.InvalidLoginDataException;
import exception.InvalidUserException;

@RunWith(SpringRunner.class)
@SpringBootTest( classes = BootEZGasApplication.class)
public class UserServiceResponseTimeTests {

    final private User mockUser = new User("mockUsernameTest", "mockPasswordTest", "mockEmailTest", 0);
    final private UserDto mockUserDto = new UserDto(null, "mockUsernameTest", "mockPasswordTest", "mockEmailTest", 0, false);
    final private long MAX_TIME = 500000000; 
    static boolean firstTime = true;

    @Autowired
    UserService userService;

    @Test
    public void testGetUserById(){
        
        addMockUser();
        Integer userId = findMockUserId();
        
        long startTime = System.nanoTime();
        try {
            userService.getUserById(userId);
        } catch (InvalidUserException e) {
            e.printStackTrace();
        }
        long stopTime = System.nanoTime();
        
        deleteMockUser();       
        assertTrue("NOT PASSED getUserById", stopTime - startTime < MAX_TIME);
    
    }
    
    @Test
    public void testSaveUser(){

        long startTime = System.nanoTime();
        userService.saveUser(mockUserDto);
        long stopTime = System.nanoTime();
        
        deleteMockUser();
        assertTrue("NOT PASSED saveUser", stopTime - startTime < MAX_TIME);
    
    }
    
    @Test
    public void testGetAllUsers(){

        addMockUser();
        
        long startTime = System.nanoTime();
        userService.getAllUsers();
        long stopTime = System.nanoTime();
        
        deleteMockUser();       
        assertTrue("NOT PASSED getAllUsers", stopTime - startTime < MAX_TIME);
    
    }
    
    @Test
    public void testDeleteUser(){

        addMockUser();
        Integer userId = findMockUserId();
        
        long startTime = System.nanoTime();
        try {
            userService.deleteUser(userId);
        } catch (InvalidUserException e) {
            e.printStackTrace();
        }
        long stopTime = System.nanoTime();
        
        deleteMockUser();     
        assertTrue("NOT PASSED getUserById", stopTime - startTime < MAX_TIME);

    }
    
    @Test
    public void testLogin(){

        addMockUser();
        IdPw credentials = new IdPw(mockUser.getEmail(), mockUser.getPassword());
        long startTime = System.nanoTime();
        try {
            userService.login(credentials);
        } catch (InvalidLoginDataException e) {
            e.printStackTrace();
        }
        long stopTime = System.nanoTime();
        
        deleteMockUser();       
        assertTrue("NOT PASSED testLogin", stopTime - startTime < MAX_TIME);
    
    }
    
    @Test
    public void testIncreaseUserReputation(){

        addMockUser();
        Integer userId = findMockUserId();
        
        long startTime = System.nanoTime();
        try {
            userService.increaseUserReputation(userId);
        } catch (InvalidUserException e) {
            e.printStackTrace();
        }
        long stopTime = System.nanoTime();
        
        deleteMockUser();       
        assertTrue("NOT PASSED increaseUserReputation", stopTime - startTime < MAX_TIME);

    }
    
    @Test
    public void testDecreaseUserReputation(){

        addMockUser();
        Integer userId = findMockUserId();
        
        long startTime = System.nanoTime();
        try {
            userService.decreaseUserReputation(userId);
        } catch (InvalidUserException e) {
            e.printStackTrace();
        }
        long stopTime = System.nanoTime();
        
        deleteMockUser();
        assertTrue("NOT PASSED decreaseUserReputation", stopTime - startTime < MAX_TIME);

    }

    void addMockUser() {
                
        try {
            
            Connection conn = DriverManager.getConnection("jdbc:h2:./data/memo", "sa", "password");
            
            if(firstTime){
                mockUser.setAdmin(false);
                firstTime = true;
            }

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

}
