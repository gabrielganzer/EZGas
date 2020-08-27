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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import it.polito.ezgas.dto.GasStationDto;
import it.polito.ezgas.entity.GasStation;
import it.polito.ezgas.entity.User;
import it.polito.ezgas.service.GasStationService;
import exception.GPSDataException;
import exception.InvalidGasStationException;
import exception.InvalidGasTypeException;
import exception.InvalidUserException;
import exception.PriceException;

@RunWith(SpringRunner.class)
@SpringBootTest( classes = BootEZGasApplication.class)
public class GasStationServiceResponseTimeTests {

    private String datePattern = "MM-dd-yyyy";
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
    private String todayDate = dateFormatter.format(LocalDate.now());

    final private GasStation mockGasStation = new GasStation(
        "mockGasStationName", 
        "mockGasStationAddress", 
        true, 
        false, 
        false,
        false,
        false,
        "mockCarSharing",
        1.0,
        1.0,
        1,
        2,
        3, 
        4,
        5, 
        null, 
        todayDate,
        100);
        
    final private GasStationDto mockGasStationDto = new GasStationDto(
        null, //gas station id
        "mockGasStationName", 
        "mockGasStationAddress", 
        true, 
        false, 
        false,
        false,
        false,
        "mockCarSharing",
        1.0,
        1.0,
        1,
        2,
        3, 
        4,
        5, 
        null, 
        todayDate,
        100);
    
    final private User mockUser = new User("mockUsernameTest2", "mockPasswordTest2", "mockEmailTest2", 0);
    //  private boolean firstTime = true;
    final private long MAX_TIME = 500000000; 
    
    @Autowired
    GasStationService gsService;

    @Test
    public void testGetGasStationById(){
        
        addMockGasStation();
        Integer gsId = findMockGasStationId();
        
        long startTime = System.nanoTime();
        try {
            gsService.getGasStationById(gsId);
        } catch (InvalidGasStationException e) {
            e.printStackTrace();
        }
        long stopTime = System.nanoTime();
        
        deleteMockGasStation();       
        assertTrue("NOT PASSED getGasStationById", stopTime - startTime < MAX_TIME);
        
    }
    
    @Test
    public void testSaveGasStation(){

        long startTime = System.nanoTime();
        try {
            gsService.saveGasStation(mockGasStationDto);
        } catch (PriceException | GPSDataException e) {
            e.printStackTrace();
        }
        long stopTime = System.nanoTime();
        
        deleteMockGasStation();
        assertTrue("NOT PASSED saveGasStation", stopTime - startTime < MAX_TIME);
    
    }
    
    @Test
    public void testGetAllGasStations(){

        addMockGasStation();
        
        long startTime = System.nanoTime();
        gsService.getAllGasStations();
        long stopTime = System.nanoTime();
        
        deleteMockGasStation();       
        assertTrue("NOT PASSED getAllGasStations", stopTime - startTime < MAX_TIME);
        
    
    }
    
    @Test
    public void testDeleteGasStation(){

        addMockGasStation();
        Integer gsId = findMockGasStationId();

        long startTime = System.nanoTime();
        try {
            gsService.deleteGasStation(gsId);
        } catch (InvalidGasStationException e) {
            e.printStackTrace();
        }
        long stopTime = System.nanoTime();
        
        assertTrue("NOT PASSED deleteGasStation", stopTime - startTime < MAX_TIME);
        
    }
    
    @Test
    public void testGetGasStationsByGasolineType(){

        addMockGasStation();

        long startTime = System.nanoTime();
        try {
            gsService.getGasStationsByGasolineType("diesel");
        } catch (InvalidGasTypeException e) {
            e.printStackTrace();
        }
        long stopTime = System.nanoTime();
        
        deleteMockGasStation();       
        assertTrue("NOT PASSED getGasStationsByGasolineType", stopTime - startTime < MAX_TIME);
        
    
    }
    
    @Test
    public void testGetGasStationsByProximity(){

        addMockGasStation();
        
        long startTime = System.nanoTime();
        try {
            gsService.getGasStationsByProximity(1.0, 1.0);
        } catch (GPSDataException e) {
            e.printStackTrace();
        }
        long stopTime = System.nanoTime();
        
        deleteMockGasStation();
        assertTrue("NOT PASSED getGasStationsByProximity", stopTime - startTime < MAX_TIME);
        

    }

    @Test
    public void testGetGasStationsWithCoordinates(){

        addMockGasStation();
        
        long startTime = System.nanoTime();
        try {
            gsService.getGasStationsWithCoordinates(1.0, 1.0, "diesel", "mockCarSharing");
        } catch (InvalidGasTypeException | GPSDataException e) {
            e.printStackTrace();
        }
        long stopTime = System.nanoTime();
        
        deleteMockGasStation();       
        assertTrue("NOT PASSED getGasStationsWithCoordinates", stopTime - startTime < MAX_TIME);

    }

    @Test
    public void testSetReport(){

        addMockUser();
        addMockGasStation();
        Integer userId = findMockUserId();
        Integer gsId = findMockGasStationId();

        long startTime = System.nanoTime();
        try {
            gsService.setReport(gsId, 1.0, 2.0, 3.0, 4.0, 5.0, userId);
        } catch (InvalidGasStationException | PriceException | InvalidUserException e) {
            e.printStackTrace();
        }

        long stopTime = System.nanoTime();
        
        deleteMockGasStation();
        deleteMockUser();
        
        assertTrue("NOT PASSED setReport", stopTime - startTime < MAX_TIME);

    }
    
    @Test
    public void testGetGasStationByCarSharing(){

        addMockGasStation();
        
        long startTime = System.nanoTime();
        gsService.getGasStationByCarSharing("mockCarSharing");
        long stopTime = System.nanoTime();
        
        deleteMockGasStation();       
        assertTrue("NOT PASSED getGasStationByCarSharing", stopTime - startTime < MAX_TIME);

    }
    
    void addMockGasStation() {
        
        try {
            
            Connection conn = DriverManager.getConnection("jdbc:h2:./data/memo", "sa", "password");
            
            /*
            if(firstTime) {
                addMockUser();
                Integer userId = findMockUserId();
                mockUser.setUserId(userId);
                mockGasStation.setReportUser(userId);
                mockGasStationDto.setReportUser(userId);
                firstTime = false;
            }
            
            Integer userId = findMockUserId();
            */

            String queryAddMockGasStation =
        "INSERT INTO GAS_STATION (" +
        "               CAR_SHARING, " +
        "               DIESEL_PRICE, " +
        "               GAS_PRICE, " +
        "               GAS_STATION_ADDRESS, " + 
        "               GAS_STATION_NAME, " + 
        "               HAS_DIESEL, " + 
        "               HAS_GAS, " + 
        "               HAS_METHANE, " + 
        "               HAS_SUPER, " + 
        "               HAS_SUPER_PLUS, " + 
        "               LAT, " + 
        "               LON, " + 
        "               METHANE_PRICE, " + 
        "               REPORT_DEPENDABILITY, " + 
        "               REPORT_TIMESTAMP, " + 
        "               REPORT_USER, " + 
        "               SUPER_PLUS_PRICE, " + 
        "               SUPER_PRICE, " + 
        "               USER_ID) "
        + "VALUES ('" + mockGasStation.getCarSharing() + "', " + 
                        mockGasStation.getDieselPrice() + ", " +
                        mockGasStation.getGasPrice() + ", '" +
                        mockGasStation.getGasStationAddress() + "', '" + 
                        mockGasStation.getGasStationName() + "', " +
                        mockGasStation.getHasDiesel() + ", " + 
                        mockGasStation.getHasGas() + ", " + 
                        mockGasStation.getHasMethane() + ", " + 
                        mockGasStation.getHasSuper() + ", " + 
                        mockGasStation.getHasSuperPlus() + ", " +
                        mockGasStation.getLat() + ", " +
                        mockGasStation.getLon() + ", " + 
                        mockGasStation.getMethanePrice() + ", " + 
                        mockGasStation.getReportDependability() + ", '" +
                        mockGasStation.getReportTimestamp() + "', " +
                        mockGasStation.getReportUser() + ", " + 
                        mockGasStation.getSuperPlusPrice() + ", " + 
                        mockGasStation.getSuperPrice() + ", " +
                        mockGasStation.getUser() +  
                        ")";
            
            
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(queryAddMockGasStation);
            
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    Integer findMockGasStationId() {
        
        Integer retVal = -1;
        
        try {
            
            Connection conn = DriverManager.getConnection("jdbc:h2:./data/memo", "sa", "password");
            String queryFindMockUserId = "SELECT GAS_STATION_ID " + 
                                         "FROM GAS_STATION " +
                                         "WHERE GAS_STATION_NAME = '" + mockGasStation.getGasStationName() + "'"; 
         
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryFindMockUserId);
            
            if(rs.next()) {
               retVal = rs.getInt("GAS_STATION_ID");
            }
            
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return retVal;
        
    }
    
    void deleteMockGasStation() {
        
        try {
            
            Connection conn = DriverManager.getConnection("jdbc:h2:./data/memo", "sa", "password");
            
            String queryDeleteMockUser = "DELETE FROM GAS_STATION " +
                                         "WHERE GAS_STATION_NAME = '" + mockGasStation.getGasStationName() + "'"; 
            
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(queryDeleteMockUser);
            
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

}
