package it.polito.ezgas;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import it.polito.ezgas.dto.GasStationDto;
import it.polito.ezgas.entity.GasStation;
import it.polito.ezgas.entity.User;
import it.polito.ezgas.repository.GasStationRepository;
import it.polito.ezgas.repository.GasStationRepositoryImpl.SortOrder;
import it.polito.ezgas.service.GasStationService;
import it.polito.ezgas.service.impl.GasStationServiceimpl;
import exception.GPSDataException;
import exception.InvalidGasStationException;
import exception.InvalidGasTypeException;
import exception.InvalidUserException;
import exception.PriceException;

@RunWith(SpringRunner.class)
@SpringBootTest( classes = BootEZGasApplication.class)
public class GasStationServiceTests {
    
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
    
    final private User mockUser = new User("mockUsername", "mockPassword", "mockEmail", 0);
    final private List<GasStation> mockListGasStation = new ArrayList<GasStation>();
    private boolean listed = false;
    
    @Autowired
    GasStationService gsService;

    GasStationService setupGasStationServiceWithMockRepo(){
        
        GasStationRepository mockRepository = mock(GasStationRepository.class);
        mockListGasStation.add(mockGasStation);
        when(mockRepository.findOne(any(Integer.class))).thenReturn(mockGasStation);
        when(mockRepository.findAll()).thenReturn(mockListGasStation);
        when(mockRepository.findAllByGasolineTypeAndCarSharing(any(String.class), any(String.class), any(SortOrder.class))).thenReturn(mockListGasStation);
        when(mockRepository.save(any(GasStation.class))).then(returnsFirstArg());
        when(mockRepository.exists(any(Integer.class))).thenReturn(listed);
        when(mockRepository.findAllByProximity(any(Double.class), any(Double.class), any(SortOrder.class))).thenReturn(mockListGasStation);
        GasStationService gasStationService = new GasStationServiceimpl(mockRepository);
    
        return gasStationService;
        
    }
    // Get by id
    @Test
    public void testGetGasStationByIdNullStep1(){
        
        System.out.println("STARTING testGetGasStationByIdNullStep1");
        
        GasStationService gasStationService = setupGasStationServiceWithMockRepo();
        Integer gasStationId = null;

        try {

            GasStationDto retDto = gasStationService.getGasStationById(gasStationId);
            assertNull("Return value is null", retDto);
            
        }catch (Exception e) {
            System.out.println(e);
        }
        
        System.out.println("ENDING testGetGasStationByIdNullStep1");
         
    }
    
    @Test
    public void testGetGasStationByIdNegativeStep1(){

        System.out.println("STARTING testGetGasStationByIdNegativeStep1");

        Assertions.assertThrows(
        	InvalidGasStationException.class, 
            () -> {
            		GasStationService gasStationService = setupGasStationServiceWithMockRepo();
            		gasStationService.getGasStationById(-1);
                  }
            );
            
        System.out.println("ENDING testGetGasStationByIdNegativeStep1");

    }
    
    @Test
    public void testGetGasStationByIdPositiveStep1(){
        
        System.out.println("STARTING testGetGasStationByIdPositiveStep1");
        
        addMockGasStation();
        Integer gsId = findMockGasStationId();
        
        GasStationService gasStationService = setupGasStationServiceWithMockRepo();
        
        try {
        	GasStationDto retDto = gasStationService.getGasStationById(gsId);
            assertNotNull("Return value is not null", retDto);
        }catch (Exception e) {
            System.out.println(e);
        }
        
        deleteMockGasStation();   
        System.out.println("ENDING testGetGasStationByIdPositiveStep1");
        
    }
    
    @Test
    public void testGetGasStationByIdFinal() {
        
    	System.out.println("STARTING testGetGasStationByIdFinal");
    	
        addMockGasStation();
        Integer gsId = findMockGasStationId();
        
        try {

        	GasStationDto retDto = gsService.getGasStationById(gsId);
            assertNotNull("Return value is not null", retDto);

        } catch (InvalidGasStationException e) {
            e.printStackTrace();
        }
        
        deleteMockGasStation(); 
        System.out.println("ENDING testGetGasStationByIdFinal");
        
    }
    
    // Save gas station
    @Test
    public void testSaveGasStationNullStep1() {
        
    	System.out.println("STARTING testSaveGasStationNullStep1");
    	
        GasStationService gasStationService = setupGasStationServiceWithMockRepo();
        
        try {

            GasStationDto retDto = gasStationService.saveGasStation(null);
            assertNull("Return value is null", retDto);
            
		} catch (PriceException | GPSDataException e) {
			e.printStackTrace();
		}
        
        System.out.println("ENDING testSaveGasStationNullStep1");
        
    }
    
    @Test
    public void testSaveGasStationNewStep1() {
        
    	System.out.println("STARTING testSaveGasStationNewStep1");
    	
        GasStationService gasStationService = setupGasStationServiceWithMockRepo();
        
        try {

            GasStationDto retDto = gasStationService.saveGasStation(mockGasStationDto);
            assertNotNull("Return value is not null", retDto);

		} catch (PriceException | GPSDataException e) {
			e.printStackTrace();
		}
        
        System.out.println("ENDING testGetGasStationByIdPositiveStep1");
        
    }
    
    @Test
    public void testSaveGasStationExistingStep1() {
        
    	System.out.println("STARTING testSaveGasStationExistingStep1");
    	
    	addMockGasStation();
    	
        GasStationService gasStationService = setupGasStationServiceWithMockRepo();
        
        try {

            GasStationDto retDto = gasStationService.saveGasStation(mockGasStationDto);
            assertNotNull("Return value is not null", retDto);

		} catch (PriceException | GPSDataException e) {
			e.printStackTrace();
		}
        
        deleteMockGasStation();
        System.out.println("ENDING testSaveGasStationExistingStep1");
        
    }
    
    @Test
    public void testSaveGasStationFinal() {
        
    	System.out.println("STARTING testSaveGasStationFinal");
        
        try {

        	GasStationDto retDto = gsService.saveGasStation(mockGasStationDto);
            assertNotNull("Return value is not null", retDto);

		} catch (PriceException | GPSDataException e) {
			e.printStackTrace();
		}
        
        System.out.println("ENDING testSaveGasStationFinal");
        
    }
	
    //getAllGasStations
	@Test
	public void testGetAllGasStationsStep1() {
		
    	System.out.println("STARTING testGetAllGasStationsStep1");
    	
    	addMockGasStation();
    	
        GasStationService gasStationService = setupGasStationServiceWithMockRepo();
        
        try {

        	List<GasStationDto> retDto = gasStationService.getAllGasStations();
            assertFalse(retDto.isEmpty());

		} catch (Exception e) {
			e.printStackTrace();
		}
        
        deleteMockGasStation(); 
        System.out.println("ENDING testGetAllGasStationsStep1");
	}
	
	@Test
	public void testGetAllGasStationsFinal() {
		
    	System.out.println("STARTING testGetAllGasStationsFinal");
    	
    	addMockGasStation();
        
        try {

        	List<GasStationDto> retDto = gsService.getAllGasStations();
            assertFalse(retDto.isEmpty());

		} catch (Exception e) {
			e.printStackTrace();
		}
        
        deleteMockGasStation(); 
        System.out.println("ENDING testGetAllGasStationsFinal");
	}
    
    //deleteGasStation
	@Test
	public void testDeleteGasStationIdNullStep1() {
		
    	System.out.println("STARTING testDeleteGasStationIdNullStep1");
    	
        GasStationService gasStationService = setupGasStationServiceWithMockRepo();
        
        try {
        	Boolean retBoolean = gasStationService.deleteGasStation(null);
        	assertNull("Return value is null", retBoolean);

		} catch (InvalidGasStationException e) {
			e.printStackTrace();
		}
        
        deleteMockGasStation(); 
        System.out.println("ENDING testDeleteGasStationIdNullStep1");
	}
	
    @Test
    public void testDeleteGasStationByIdNegativeStep1(){

        System.out.println("STARTING testDeleteGasStationByIdNegativeStep1");

        Assertions.assertThrows(
        	InvalidGasStationException.class, 
            () -> {
            		GasStationService gasStationService = setupGasStationServiceWithMockRepo();
            		gasStationService.deleteGasStation(-1);
                  }
            );
            
        System.out.println("ENDING testDeleteGasStationByIdNegativeStep1");

    }

	@Test
	public void testDeleteGasStationIdListedStep1() {
		
    	System.out.println("STARTING testDeleteGasStationIdListedStep1");
    	
    	addMockGasStation();
    	Integer gsId  = findMockGasStationId();
    	listed = true;
        GasStationService gasStationService = setupGasStationServiceWithMockRepo();
        
        try {

        	Boolean retBoolean = gasStationService.deleteGasStation(gsId);
        	assertTrue(retBoolean);

		} catch (InvalidGasStationException e) {
			e.printStackTrace();
		}
        
        deleteMockGasStation(); 
        System.out.println("ENDING testDeleteGasStationIdListedStep1");
	}
	
	@Test
	public void testDeleteGasStationIdUnlistedStep1() {
		
    	System.out.println("STARTING testDeleteGasStationIdUnlistedStep1");
    	listed = false;
        GasStationService gasStationService = setupGasStationServiceWithMockRepo();
        
        try {
        	Boolean retBoolean = gasStationService.deleteGasStation(5);
        	assertNull("Return value is null", retBoolean);
		} catch (InvalidGasStationException e) {
			e.printStackTrace();
		}

        System.out.println("ENDING testDeleteGasStationIdUnlistedStep1");
	}
	
	@Test
	public void testDeleteGasStationFinal() {
		
    	System.out.println("STARTING testDeleteGasStationFinal");
    	
    	addMockGasStation();
    	Integer gsId  = findMockGasStationId();
        
        try {

        	Boolean retBoolean = gsService.deleteGasStation(gsId);
        	assertTrue(retBoolean);

		} catch (InvalidGasStationException e) {
			e.printStackTrace();
		}
        
        deleteMockGasStation(); 
        System.out.println("ENDING testDeleteGasStationFinal");
	}
    
    @Test
    public void testGetGasStationsByGasolineTypeNullStep1() {
    	
    	System.out.println("STARTING testGetGasStationsByGasolineTypeNullStep1");
        GasStationService gasStationService = setupGasStationServiceWithMockRepo();

        try {
            List<GasStationDto> retList = gasStationService.getGasStationsByGasolineType(null);
            assertTrue(retList.isEmpty());
        } catch (InvalidGasTypeException e) {
            e.printStackTrace();
        }
      
        System.out.println("ENDING testGetGasStationsByGasolineTypeNullStep1");
  
    }
    
    @Test
    public void testGetGasStationsByGasolineTypeInvalidStep1() {

        System.out.println("STARTING testGetGasStationsByGasolineTypeInvalidStep1");

        Assertions.assertThrows(
        	InvalidGasTypeException.class, 
            () -> {
            		GasStationService gasStationService = setupGasStationServiceWithMockRepo();
            		gasStationService.getGasStationsByGasolineType("mockFuelType");
                  }
            );
            
        System.out.println("ENDING testGetGasStationsByGasolineTypeInvalidStep1");

    }

    @Test
    public void testGetGasStationsByGasolineTypeValidStep1() {
    	
    	System.out.println("STARTING testGetGasStationsByGasolineTypeValidStep1");
    	addMockGasStation();
        GasStationService gasStationService = setupGasStationServiceWithMockRepo();

        try {
            List<GasStationDto> retList = gasStationService.getGasStationsByGasolineType("diesel");
            assertFalse(retList.isEmpty());
        } catch (InvalidGasTypeException e) {
            e.printStackTrace();
        }
        deleteMockGasStation();
        System.out.println("ENDING testGetGasStationsByGasolineTypeValidStep1");
  
    }
    
    @Test
    public void testGetGasStationsByGasolineTypeFinal() {
    	
    	System.out.println("STARTING testGetGasStationsByGasolineTypeFinal");
    	addMockGasStation();

        try {
            List<GasStationDto> retList = gsService.getGasStationsByGasolineType("diesel");
            assertFalse(retList.isEmpty());
        } catch (InvalidGasTypeException e) {
            e.printStackTrace();
        }
        deleteMockGasStation();
        System.out.println("ENDING testGetGasStationsByGasolineTypeFinal");
  
    }
    
    //getGasStationsByProximity
    @Test
    public void testGetGasStationsByProximityInvalidStep1() {

        System.out.println("STARTING testGetGasStationsByProximityInvalidStep1");

        Assertions.assertThrows(
        	GPSDataException.class, 
            () -> {
            		GasStationService gasStationService = setupGasStationServiceWithMockRepo();
            		gasStationService.getGasStationsByProximity(91.0, -181);
                  }
            );
            
        System.out.println("ENDING testGetGasStationsByProximityInvalidStep1");

    }

    @Test
    public void testGetGasStationsByProximityValidStep1() {
    	
    	System.out.println("STARTING testGetGasStationsByProximityValidStep1");
    	addMockGasStation();
        GasStationService gasStationService = setupGasStationServiceWithMockRepo();

        try {
            List<GasStationDto> retList = gasStationService.getGasStationsByProximity(1.0, 1.0);
            assertFalse(retList.isEmpty());
        } catch (GPSDataException e) {
            e.printStackTrace();
        }
        deleteMockGasStation();
        System.out.println("ENDING testGetGasStationsByProximityValidStep1");
  
    }
    
    @Test
    public void testGetGasStationsByProximityFinal() {
    	
    	System.out.println("STARTING testGetGasStationsByProximityFinal");
    	addMockGasStation();

        try {
        	List<GasStationDto> retList = gsService.getGasStationsByProximity(1.0, 1.0);
            assertFalse(retList.isEmpty());
        } catch (GPSDataException e) {
            e.printStackTrace();
        }
        deleteMockGasStation();
        System.out.println("ENDING testGetGasStationsByProximityFinal");
  
    }
    
    //getGasStationsWithCoordinates
    @Test
    public void testGasStationsWithCoordinatesNullStep1() {
    	
    	System.out.println("STARTING testGasStationsWithCoordinatesNullStep1");
    	
        GasStationService gasStationService = setupGasStationServiceWithMockRepo();

        try {
            List<GasStationDto> retDto = gasStationService.getGasStationsWithCoordinates(1.0, 1.0, null, null);
            assertTrue(retDto.isEmpty());
        } catch (InvalidGasTypeException | GPSDataException e) {
            e.printStackTrace();
        }

        System.out.println("ENDING testGasStationsWithCoordinatesNullStep1");
  
    }
    
    @Test
    public void testGasStationsWithCoordinatesInvalidCoordinatesStep1() {

        System.out.println("STARTING testGasStationsWithCoordinatesInvalidCoordinatesStep1");

        Assertions.assertThrows(
        	GPSDataException.class, 
            () -> {
            		GasStationService gasStationService = setupGasStationServiceWithMockRepo();
            		gasStationService.getGasStationsWithCoordinates(91, -181, "diesel", "mockCarSharing");
                  }
            );
            
        System.out.println("ENDING testGasStationsWithCoordinatesInvalidCoordinatesStep1");

    }
    
    @Test
    public void testGasStationsWithCoordinatesInvalidFuelTypeStep1() {

        System.out.println("STARTING testGasStationsWithCoordinatesInvalidFuelTypeStep1");

        Assertions.assertThrows(
        	InvalidGasTypeException.class, 
            () -> {
            		GasStationService gasStationService = setupGasStationServiceWithMockRepo();
            		gasStationService.getGasStationsWithCoordinates(1.0, 1.0, "mockFuelType", "mockCarSharing");
                  }
            );
            
        System.out.println("ENDING testGasStationsWithCoordinatesInvalidFuelTypeStep1");

    }
    
    @Test
    public void testGasStationsWithCoordinatesValidFieldsStep1() {
    	
    	System.out.println("STARTING testGasStationsWithCoordinatesValidFieldsStep1");
    	addMockGasStation();
        GasStationService gasStationService = setupGasStationServiceWithMockRepo();

        try {
            List<GasStationDto> retDto = gasStationService.getGasStationsWithCoordinates(1.0, 1.0, "diesel", "mockCarSharing");
            assertFalse(retDto.isEmpty());
        } catch (InvalidGasTypeException | GPSDataException e) {
            e.printStackTrace();
        }
        deleteMockGasStation();
        System.out.println("ENDING testGasStationsWithCoordinatesValidFieldsStep1");
  
    }
    
    @Test
    public void testGasStationsWithCoordinatesFinal() {
    	
    	System.out.println("STARTING testGasStationsWithCoordinatesFinal");
    	addMockGasStation();

        try {
            List<GasStationDto> retDto = gsService.getGasStationsWithCoordinates(1.0, 1.0, "diesel", "mockCarSharing");
            assertFalse(retDto.isEmpty());
        } catch (InvalidGasTypeException | GPSDataException e) {
            e.printStackTrace();
        }
        
        deleteMockGasStation();
        System.out.println("ENDING testGasStationsWithCoordinatesFinal");
  
    }
    
    //getGasStationsWithoutCoordinate
    @Test
    public void testGasStationsWithoutCoordinatesNullStep1() {
    	
    	System.out.println("STARTING testGasStationsWithoutCoordinatesNullStep1");
    	
        GasStationService gasStationService = setupGasStationServiceWithMockRepo();

        try {
            List<GasStationDto> retDto = gasStationService.getGasStationsWithoutCoordinates(null, null);
            assertTrue(retDto.isEmpty());
        } catch (InvalidGasTypeException e) {
            e.printStackTrace();
        }

        System.out.println("ENDING testGasStationsWithoutCoordinatesNullStep1");
  
    }
    
    @Test
    public void testGasStationsWithoutCoordinatesInvalidFuelTypeStep1() {

        System.out.println("STARTING testGasStationsWithoutCoordinatesInvalidFuelTypeStep1");

        Assertions.assertThrows(
        	InvalidGasTypeException.class, 
            () -> {
            		GasStationService gasStationService = setupGasStationServiceWithMockRepo();
            		gasStationService.getGasStationsWithoutCoordinates("mockFuelType", "mockCarSharing");
                  }
            );
            
        System.out.println("ENDING testGasStationsWithoutCoordinatesInvalidFuelTypeStep1");

    }
    
    @Test
    public void testGasStationsWithoutCoordinatesValidFieldsStep1() {
    	
    	System.out.println("STARTING testGasStationsWithoutCoordinatesValidFieldsStep1");
    	addMockGasStation();
        GasStationService gasStationService = setupGasStationServiceWithMockRepo();

        try {
            List<GasStationDto> retDto = gasStationService.getGasStationsWithoutCoordinates("diesel", "mockCarSharing");
            assertFalse(retDto.isEmpty());
        } catch (InvalidGasTypeException e) {
            e.printStackTrace();
        }
        deleteMockGasStation();
        System.out.println("ENDING testGasStationsWithoutCoordinatesValidFieldsStep1");
  
    }
    
    @Test
    public void testGasStationsWithoutCoordinatesFinal() {
    	
    	System.out.println("STARTING testGasStationsWithoutCoordinatesFinal");
    	addMockGasStation();

        try {
            List<GasStationDto> retDto = gsService.getGasStationsWithoutCoordinates("diesel", "mockCarSharing");
            assertFalse(retDto.isEmpty());
        } catch (InvalidGasTypeException e) {
            e.printStackTrace();
        }
        
        deleteMockGasStation();
        System.out.println("ENDING testGasStationsWithoutCoordinatesFinal");
  
    }
    
    //setReport
    @Test
    public void testSetReportGasStationIdNegativeStep1() {

        System.out.println("STARTING testSetReportGasStationIdNegativeStep1");

        Assertions.assertThrows(
        	InvalidGasStationException.class, 
            () -> {
            		GasStationService gasStationService = setupGasStationServiceWithMockRepo();
            		gasStationService.setReport(-1, 2.0, 0.0, 0.0, 0.0, 0.0, 1);
                  }
            );
            
        System.out.println("ENDING testSetReportGasStationIdNegativeStep1");

    }
    
    @Test
    public void testSetReportUserIdNegativeStep1() {

        System.out.println("STARTING testSetReportUserIdNegativeStep1");

        Assertions.assertThrows(
        	InvalidUserException.class, 
            () -> {
            		GasStationService gasStationService = setupGasStationServiceWithMockRepo();
            		gasStationService.setReport(1, 2.0, 0.0, 0.0, 0.0, 0.0, -1);
                  }
            );
            
        System.out.println("ENDING testSetReportUserNegativeStep1");

    }
    
    @Test
    public void testSetReportPriceNegativeStep1() {

        System.out.println("STARTING testSetReportPriceNegativeStep1");
        
        addMockUser();
        addMockGasStation();
        Integer userId = findMockUserId();
        Integer gsId = findMockGasStationId();
        
        Assertions.assertThrows(
        	PriceException.class, 
            () -> {
            		gsService.setReport(gsId, -1.0, 0.0, 0.0, 0.0, 0.0, userId);
                  }
            );
        
        deleteMockGasStation();
        deleteMockUser();
            
        System.out.println("ENDING testSetReportPriceNegativeStep1");

    }
    
    @Test
    public void testSetReportFinal() {

        System.out.println("STARTING testSetReportFinal");
        
        addMockUser();
        addMockGasStation();
        Integer userId = findMockUserId();
        Integer gsId = findMockGasStationId();
        
        try {
            gsService.setReport(gsId, 1.0, 2.0, 3.0, 4.0, 5.0, userId);
        } catch (InvalidGasStationException | PriceException | InvalidUserException e) {
            e.printStackTrace();
        }
        
        deleteMockGasStation();
        deleteMockUser();
            
        System.out.println("ENDING testSetReportFinal");

    }
    
    @Test
    public void testGetGasStationByCarSharingNullStep1(){
        
        System.out.println("STARTING testGetGasStationByCarSharingNullStep1");
        
        GasStationService gasStationService = setupGasStationServiceWithMockRepo();
       
        try {
        	List<GasStationDto> retDto = gasStationService.getGasStationByCarSharing(null);
            assertTrue(retDto.isEmpty());

        } catch (Exception e) {
        	e.printStackTrace();
        }
        System.out.println("ENDING testGetGasStationByCarSharingNullStep1");
 
    }
    
    @Test
    public void testGetGasStationByCarSharingNotNullStep1(){
        
        System.out.println("STARTING testGetGasStationByCarSharingNotNullStep1");
        
        addMockGasStation();
        GasStationService gasStationService = setupGasStationServiceWithMockRepo();
        
        try {
        	List<GasStationDto> retDto = gasStationService.getGasStationByCarSharing("mockCarSharing");
            assertFalse(retDto.isEmpty());

        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        deleteMockGasStation();
        System.out.println("ENDING testGetGasStationByCarSharingNotNullStep1");
    }
    
    @Test
    public void testGetGasStationByCarSharingFinal(){
        
        System.out.println("STARTING testGetGasStationByCarSharingFinal");
        
        addMockGasStation();
        
        try {
        	List<GasStationDto> retDto = gsService.getGasStationByCarSharing("mockCarSharing");
            assertFalse(retDto.isEmpty());

        } catch (Exception e) {
        	e.printStackTrace();
        } 
        
        deleteMockGasStation();
        System.out.println("ENDING testGetGasStationByCarSharingFinal");
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

