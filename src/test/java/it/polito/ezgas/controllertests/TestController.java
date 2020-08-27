package it.polito.ezgas.controllertests;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polito.ezgas.dto.GasStationDto;
import it.polito.ezgas.dto.IdPw;
import it.polito.ezgas.dto.UserDto;
import it.polito.ezgas.dto.LoginDto;

public class TestController{

    final private String host = "http://localhost:8080";
    final private UserDto mockUserDto = new UserDto(null, "mockUsernameTC", "mockPasswordTC", "mockEmailTC", 0);
    final private GasStationDto mockGasStationDto = new GasStationDto(
        null,
        "mockGasStationNameTC", 
        "mockGasStationAddressTC", 
        true, // diesel
        true, // super
        false, // superPlus
        false, // gas
        false, // methane
        "mockCarSharingTC",
        1.0, // lat
        1.0, // lon
        0, // prices are not set
        0,
        0, 
        0,
        0, 
        -1, // reportUser 
        null, // timestamp
        0); // reportDependability
    
    /*
    ASSUMPTIONS: 
    the method addMockUser adds the mockUserDto into the db. Be careful on which attributes you are using in an 
    assertion since some attributes are null because either they are not set or assigned automatically when saved the first
    time into the db.
    
    The mockUserDto has the userId and admin attributes set to null.
    The UserId is assigned automatically by the underling DB when a new user is inserted into the DB.  
    */
    private void setupMockUser() {
       addMockUser();
    }

    private void cleanupMockUser() {
        deleteMockUser();
    }
    
    /*
    ASSUMPTIONS: 
    the method addMockGasStation adds the mockGasStationDto into the db. Be careful on which attributes you are using in an 
    assertion since some attributes are null because either they are not set or assigned automatically when saved the first
    time into the db.
    
    The mockGasStationDto has the *gasStationId* and *timestamp* set to null.
    The mockGasStationDto is assumed to not have a report attached to the gs, thus all prices are set to 0 and reportUser
    is set to -1.
    See mockGasStationDto initialization for more info.
    The mockGasStationId is assigned automatically by the underling DB when a new gs is inserted into the DB.  
    */
    private void setupMockGasStation() {
        addMockGasStation();
    }

     private void cleanupMockGasStation() {
        deleteMockGasStation();
    }

    @Test
 	public void testGetUserById() throws ClientProtocolException, IOException {

 		mockUserDto.setAdmin(false);
 		setupMockUser();

 		HttpGet request = new HttpGet(host + "/user/getUser/" + findMockUserId());
 		HttpResponse response = HttpClientBuilder.create().build().execute(request);
 		String jsonFromResponse = EntityUtils.toString(response.getEntity());
 		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
 		UserDto userDto = mapper.readValue(jsonFromResponse, UserDto.class);

 		try {
 			assert (response.getStatusLine().getStatusCode() == 200);
 			assert (userDto.getUserId().equals(findMockUserId()));
 			assert (userDto.getUserName().equals(mockUserDto.getUserName()));
 			assert (userDto.getPassword().equals(mockUserDto.getPassword()));
 			assert (userDto.getEmail().equals(mockUserDto.getEmail()));
 			assert (userDto.getReputation().equals(mockUserDto.getReputation()));
 			assert (userDto.getAdmin().equals(mockUserDto.getAdmin()));
 		} catch (Exception e) {
 			e.printStackTrace();
 		}

 		cleanupMockUser();
 		mockUserDto.setAdmin(null);

 	}

 	@Test
 	public void testGetAllUsers() throws ClientProtocolException, IOException {

 		mockUserDto.setAdmin(false);
 		setupMockUser();

 		HttpGet request = new HttpGet(host + "/user/getAllUsers");
 		HttpResponse response = HttpClientBuilder.create().build().execute(request);
 		String jsonFromResponse = EntityUtils.toString(response.getEntity());
 		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
 		UserDto[] usersDto = mapper.readValue(jsonFromResponse, UserDto[].class);
 		List<UserDto> usersDtoResult = Arrays.asList(usersDto);
 		List<UserDto> usersDtoDB = new ArrayList<>();

 		try {
 			usersDtoDB = listOfUsersDto();
 			assert (response.getStatusLine().getStatusCode() == 200);
 			for (int i = 0; i < usersDtoDB.size(); i++) {
 				assert (usersDtoResult.get(i).getUserId().equals(usersDtoDB.get(i).getUserId()));
 				assert (usersDtoResult.get(i).getUserName().equals(usersDtoDB.get(i).getUserName()));
 				assert (usersDtoResult.get(i).getPassword().equals(usersDtoDB.get(i).getPassword()));
 				assert (usersDtoResult.get(i).getEmail().equals(usersDtoDB.get(i).getEmail()));
 				assert (usersDtoResult.get(i).getReputation().equals(usersDtoDB.get(i).getReputation()));
 				assert (usersDtoResult.get(i).getAdmin().equals(usersDtoDB.get(i).getAdmin()));
 			}

 		} catch (Exception e) {
 			e.printStackTrace();
 		}

 		cleanupMockUser();
 		mockUserDto.setAdmin(null);

 	}

 	@Test
 	public void testSaveUser() throws ClientProtocolException, IOException {

 		mockUserDto.setReputation(null);
 		mockUserDto.setAdmin(false);

 		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
 		String jsonObject = mapper.writeValueAsString(mockUserDto);
 		StringEntity requestEntity = new StringEntity(jsonObject, ContentType.APPLICATION_JSON);
 		HttpPost request = new HttpPost(host + "/user/saveUser");
 		request.setEntity(requestEntity);
 		HttpResponse response = HttpClientBuilder.create().build().execute(request);
 		String jsonFromResponse = EntityUtils.toString(response.getEntity());
 		UserDto userDto = mapper.readValue(jsonFromResponse, UserDto.class);

 		try {
 			assert (response.getStatusLine().getStatusCode() == 200);
 			// cannot test UserId in response body because it's not passed for privacy
 			// reasons
 			assert (userDto.getUserName().equals(mockUserDto.getUserName()));
 			assert (userDto.getPassword().equals(mockUserDto.getPassword()));
 			assert (userDto.getEmail().equals(mockUserDto.getEmail()));
 			assert (userDto.getReputation().equals(0));
 			assert (userDto.getAdmin().equals(mockUserDto.getAdmin()));
 		} catch (Exception e) {
 			e.printStackTrace();
 		}

 		cleanupMockUser();
 		mockUserDto.setReputation(0);
 		mockUserDto.setAdmin(null);

 	}

 	@Test
 	public void testDeleteUser() throws ClientProtocolException, IOException {

 		mockUserDto.setAdmin(false);
 		setupMockUser();

 		HttpDelete request = new HttpDelete(host + "/user/deleteUser/" + findMockUserId());
 		HttpResponse response = HttpClientBuilder.create().build().execute(request);
 		String jsonFromResponse = EntityUtils.toString(response.getEntity()); //
 		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
 		Boolean result = mapper.readValue(jsonFromResponse, Boolean.class);

 		try {
 			assert (response.getStatusLine().getStatusCode() == 200);
 			assert (result == true);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}

 		mockUserDto.setAdmin(null);

 	}
     
    @Test
    public void testLogin() throws IOException{

        setupMockUser();

        String URI = host + "/user/login";
        String email = mockUserDto.getEmail();
        String password = mockUserDto.getPassword();
        IdPw credentials = new IdPw(email, password);
        
        try {
            
            // create a http post object
            HttpPost post = new HttpPost(URI);
            
            // transform IdPW into a JSON string
            ObjectMapper mapper = new ObjectMapper().configure(
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, 
                    false);
            String jsonObject = mapper.writeValueAsString(credentials);
            // create a new parameter
            StringEntity requestEntity = new StringEntity(jsonObject, ContentType.APPLICATION_JSON);
            // set the parameter to the http post
            post.setEntity(requestEntity);
            
            HttpResponse response = HttpClientBuilder.create().build().execute(post);

            LoginDto retLoginDto = mapper.readValue(EntityUtils.toString(response.getEntity()), LoginDto.class);
            
            assertTrue(
                    retLoginDto.getUserName().compareTo(mockUserDto.getUserName()) == 0 &&
                    retLoginDto.getEmail().compareTo(mockUserDto.getEmail()) == 0 &&
                    retLoginDto.getReputation() == mockUserDto.getReputation()
                    );
            
        } catch (IOException e) {
            throw e;
        } finally{
            cleanupMockUser();
        }

    }

    @Test
    public void  testDecreaseUserReputation() throws IOException{

        setupMockUser();
        String URI = host + "/user/decreaseUserReputation/" + findMockUserId();

        try {
            
            // create a http post object
            HttpPost post = new HttpPost(URI);
            
            HttpResponse response = HttpClientBuilder.create().build().execute(post);
            Integer retReputation = Integer.parseInt(EntityUtils.toString(response.getEntity()));
           
            assertTrue(retReputation == -1);

        } catch (IOException e) {
            throw e;
        } finally{
            cleanupMockUser();
        }

    }
    
    @Test
    public void  testIncreaseUserReputation() throws IOException{

        setupMockUser();
        String URI = host + "/user/increaseUserReputation/" + findMockUserId();

        try {
            
            HttpPost post = new HttpPost(URI);
            
            HttpResponse response = HttpClientBuilder.create().build().execute(post);
            Integer retReputation = Integer.parseInt(EntityUtils.toString(response.getEntity()));
           
            assertTrue(retReputation == 1);

        } catch (IOException e) {
            throw e;
        } finally {
            cleanupMockUser();
        }

    }
	@Test
	public void testGetGasStationById() throws IOException {
		
		setupMockGasStation();
		String URI = host + "/gasstation/getGasStation/" + findMockGasStationId();
		
        try {            
        	HttpUriRequest request = new HttpGet(URI);
        	
    		HttpResponse response = HttpClientBuilder.create().build().execute(request);
    		
    		String jsonFromResponse = EntityUtils.toString(response.getEntity());
    		
    		assert(jsonFromResponse.contains("mockGasStationName"));

        } catch (IOException e) {
            throw e;
        } finally{
            cleanupMockGasStation();
        }
		
	}
	
	@Test
	public void testGetAllGasStations() throws IOException {
		
		setupMockGasStation();
		String URI = host + "/gasstation/getAllGasStations" ;
		boolean found = false;

        try {            
        	HttpUriRequest request = new HttpGet(URI);
        	
        	HttpResponse response = HttpClientBuilder.create().build().execute(request);
    		
    		String jsonFromResponse = EntityUtils.toString(response.getEntity());
    		
    		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    		
    		GasStationDto[] gasStationArray = mapper.readValue(jsonFromResponse, GasStationDto[].class);
    		
    		for(GasStationDto gs: gasStationArray){
                if(gs.getLat() == mockGasStationDto.getLat() && gs.getLon() == mockGasStationDto.getLon()){
                    found = true;
                    assertTrue(isEqualToReference(gs, mockGasStationDto) == true);
                }
            }

            if(!found)
                Assert.fail("testGetAllGasStations: mockGasStation not found in the response");

        } catch (IOException e) {
            throw e;
        } finally{
            cleanupMockGasStation();
        }
		
	}
	
	@Test
	public void testSaveGasStation() throws IOException {
		
		String URI = host + "/gasstation/saveGasStation";
		
        try {
            HttpPost post = new HttpPost(URI);

            ObjectMapper mapper = new ObjectMapper();
            String jsonObject = mapper.writeValueAsString(mockGasStationDto);
            StringEntity requestEntity = new StringEntity(jsonObject, ContentType.APPLICATION_JSON);

            post.setEntity(requestEntity);
            
            HttpResponse response = HttpClientBuilder.create().build().execute(post);
            
            assert(response.getStatusLine().getStatusCode() == 200);

        } catch (IOException e) {
            throw e;
        } finally{
            cleanupMockGasStation();
        }
		
	}
	
	@Test
	public void testDeleteGasStation() throws IOException {
		
		setupMockGasStation();
		String URI = host + "/gasstation/deleteGasStation/" + findMockGasStationId();
		
        try {
        	HttpUriRequest request = new HttpDelete(URI);

            HttpResponse response = HttpClientBuilder.create().build().execute(request);
            
            assert(response.getStatusLine().getStatusCode() == 200);
            
        } catch (IOException e) {
            throw e;
        } finally{
            cleanupMockGasStation();
        }
	}
	
	@Test
	public void testGetGasStationsByGasolineType() throws IOException {
		
		setupMockGasStation();
		String URI = host + "/gasstation/searchGasStationByGasolineType/diesel" ;
        boolean found = false;
		
        try {            
        	HttpUriRequest request = new HttpGet(URI);
        	
        	HttpResponse response = HttpClientBuilder.create().build().execute(request);
    		
    		String jsonFromResponse = EntityUtils.toString(response.getEntity());
    		
    		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    		
    		GasStationDto[] gasStationArray = mapper.readValue(jsonFromResponse, GasStationDto[].class);
    		
            for(GasStationDto gs: gasStationArray){
                if(gs.getLat() == mockGasStationDto.getLat() && gs.getLon() == mockGasStationDto.getLon()){
                    found = true;
                    assertTrue(isEqualToReference(gs, mockGasStationDto) == true);
                }
            }

            if(!found)
                Assert.fail("testGetGasStationsByGasolineType: mockGasStation not found in the response");

        } catch (IOException e) {
            throw e;
        } finally{
            cleanupMockGasStation();
        }
		
	}
	
	@Test
	public void testGetGasStationsByProximity() throws IOException {
		
		setupMockGasStation();
		String URI = host + "/gasstation/searchGasStationByProximity/" + mockGasStationDto.getLat() +"/"+ mockGasStationDto.getLon();
		boolean found = false;

        try {            
        	HttpUriRequest request = new HttpGet(URI);
        	
        	HttpResponse response = HttpClientBuilder.create().build().execute(request);
    		
    		String jsonFromResponse = EntityUtils.toString(response.getEntity());
    		
    		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    		
    		GasStationDto[] gasStationArray = mapper.readValue(jsonFromResponse, GasStationDto[].class);
    		
            for(GasStationDto gs: gasStationArray){
                if(gs.getLat() == mockGasStationDto.getLat() && gs.getLon() == mockGasStationDto.getLon()){
                    found = true;
                    assertTrue(isEqualToReference(gs, mockGasStationDto) == true);
                }
            }

            if(!found)
                Assert.fail("testGetGasStationsByGasolineType: mockGasStation not found in the response");

        } catch (IOException e) {
            throw e;
        } finally{
            cleanupMockGasStation();
        }
		
	}
	
    @Test
    public void  testGetGasStationsWithCoordinates() throws IOException{
        
        setupMockGasStation();
        
        String URI = 
                host + 
                "/gasstation/getGasStationsWithCoordinates/" + 
                 String.valueOf(mockGasStationDto.getLat()) + "/" +
                 String.valueOf(mockGasStationDto.getLon()) + 
                 "/diesel/" + 
                 mockGasStationDto.getCarSharing();
        boolean found = false;

        try {
            // create a http post object
            HttpUriRequest getRequest = new HttpGet(URI);
            HttpResponse response = HttpClientBuilder.create().build().execute(getRequest);
            
            ObjectMapper mapper = new ObjectMapper().configure(
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, 
                    false);

            String retResponseJSON = EntityUtils.toString(response.getEntity());
            List<GasStationDto> retListGasStations = mapper.readValue(
                    retResponseJSON, 
                    new TypeReference<List<GasStationDto>>(){}
                    );

            for(GasStationDto gs: retListGasStations){
                if(gs.getLat() == mockGasStationDto.getLat() && gs.getLon() == mockGasStationDto.getLon()){
                    found = true;
                    assertTrue(isEqualToReference(gs, mockGasStationDto) == true);
                }
            }

            if(!found)
                Assert.fail("testGetGasStationsByGasolineType: mockGasStation not found in the response");

            
        } catch (IOException e) {
            throw e;
        } finally {
            cleanupMockGasStation();
        }

    }
    
    @Test
	public void testSetGasStationReport() throws ClientProtocolException, IOException {

		mockUserDto.setAdmin(false);
		setupMockUser();
		setupMockGasStation();

		HttpPost request = new HttpPost(host + "/gasstation/setGasStationReport/" + findMockGasStationId()
				+ "/1.5/1.6/-1/-1/-1/" + findMockUserId());
		HttpResponse response = HttpClientBuilder.create().build().execute(request);

		try {
			assert (response.getStatusLine().getStatusCode() == 200);
		} catch (Exception e) {
			e.printStackTrace();
		}

		cleanupMockGasStation();
		cleanupMockUser();
		mockUserDto.setAdmin(null);

	}

    void addMockUser() {
        
        try {
            
            Connection conn = DriverManager.getConnection("jdbc:h2:./data/memo;AUTO_SERVER=TRUE", "sa", "password");
            
            String queryAddMockUser = "INSERT INTO USER (USER_NAME, PASSWORD, EMAIL, REPUTATION, ADMIN) "
                                    + "VALUES ('" + mockUserDto.getUserName() + "', '" 
                                                  + mockUserDto.getPassword() + "', '"
                                                  + mockUserDto.getEmail() + "', " 
                                                  + mockUserDto.getReputation() + ", " 
                                                  + mockUserDto.getAdmin() + ")";
                
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(queryAddMockUser);
            
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            System.out.println("addMockUser" + e);
        }
        
    }
    
    Integer findMockUserId() {
        
        Integer retVal = -1;
        
        try {
            
            Connection conn = DriverManager.getConnection("jdbc:h2:./data/memo;AUTO_SERVER=TRUE", "sa", "password");
            String queryFindMockUserId = "SELECT USER_ID " + 
                                         "FROM USER " +
                                         "WHERE EMAIL = '" + mockUserDto.getEmail() + "'";
         
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryFindMockUserId);
            
            if(rs.next()) {
               retVal = rs.getInt("USER_ID");
            }
            
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            System.out.println("findMockUserId" + e);
        }
        
        return retVal;
        
    }
    
    void deleteMockUser() {
        
        try {
            
            Connection conn = DriverManager.getConnection("jdbc:h2:./data/memo;AUTO_SERVER=TRUE", "sa", "password");
            
            String queryDeleteMockUser = "DELETE FROM USER WHERE EMAIL = '" + mockUserDto.getEmail() + "'";
            
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(queryDeleteMockUser);
            
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            System.out.println("deleteMockUser" + e);
        }
    }

    void addMockGasStation() {
        
        try {
            
            Connection conn = DriverManager.getConnection("jdbc:h2:./data/memo;AUTO_SERVER=TRUE", "sa", "password");
         
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
        "               SUPER_PRICE )" 
        + "VALUES ('" + mockGasStationDto.getCarSharing() + "', " + 
                        mockGasStationDto.getDieselPrice() + ", " +
                        mockGasStationDto.getGasPrice() + ", '" +
                        mockGasStationDto.getGasStationAddress() + "', '" + 
                        mockGasStationDto.getGasStationName() + "', " +
                        mockGasStationDto.getHasDiesel() + ", " + 
                        mockGasStationDto.getHasGas() + ", " + 
                        mockGasStationDto.getHasMethane() + ", " + 
                        mockGasStationDto.getHasSuper() + ", " + 
                        mockGasStationDto.getHasSuperPlus() + ", " +
                        mockGasStationDto.getLat() + ", " +
                        mockGasStationDto.getLon() + ", " + 
                        mockGasStationDto.getMethanePrice() + ", " + 
                        mockGasStationDto.getReportDependability() + ", " +
                        mockGasStationDto.getReportTimestamp() + ", " +
                        mockGasStationDto.getReportUser() + ", " + 
                        mockGasStationDto.getSuperPlusPrice() + ", " + 
                        mockGasStationDto.getSuperPrice() +
                        ")";
            
            
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(queryAddMockGasStation);
            
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            System.out.println("addMockGasStation" + e);
        }
        
    }
    
    Integer findMockGasStationId() {
        
        Integer retVal = -1;
        
        try {
            
            Connection conn = DriverManager.getConnection("jdbc:h2:./data/memo;AUTO_SERVER=TRUE", "sa", "password");
            String queryFindMockUserId = "SELECT GAS_STATION_ID " + 
                                         "FROM GAS_STATION " +
                                         "WHERE GAS_STATION_NAME = '" + mockGasStationDto.getGasStationName() + "'"; 
         
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryFindMockUserId);
            
            if(rs.next()) {
               retVal = rs.getInt("GAS_STATION_ID");
            }
            
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            System.out.println("findMockGasStationId" + e);
        }
        
        return retVal;
        
    }
    
    void deleteMockGasStation() {
        
        try {
            
            Connection conn = DriverManager.getConnection("jdbc:h2:./data/memo;AUTO_SERVER=TRUE", "sa", "password");
            
            String queryDeleteMockUser = "DELETE FROM GAS_STATION " +
                                         "WHERE GAS_STATION_NAME = '" + mockGasStationDto.getGasStationName() + "'"; 
            
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(queryDeleteMockUser);
            
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            System.out.println("deleteMockGasStation" + e);
        }
    }

    Boolean isEqualToReference(GasStationDto gs, GasStationDto gsReference){
        
        Boolean areEqual = false;
        
        if(
                gs.getGasStationName().compareTo(gsReference.getGasStationName()) == 0 &&
                gs.getGasStationAddress().compareTo(gsReference.getGasStationAddress()) == 0 &&
                gs.getHasDiesel() == gsReference.getHasDiesel()  &&
                gs.getHasSuper() == gsReference.getHasSuper() &&
                gs.getHasSuperPlus() == gsReference.getHasSuperPlus() &&
                gs.getHasGas() == gsReference.getHasGas() &&
                gs.getHasMethane() == gsReference.getHasMethane() &&
                gs.getLat() == gsReference.getLat() &&
                gs.getLon() == gsReference.getLon() &&
                gs.getDieselPrice() == gsReference.getDieselPrice() &&
                gs.getSuperPrice() == gsReference.getSuperPrice() &&
                gs.getSuperPlusPrice() == gsReference.getSuperPlusPrice() &&
                gs.getGasPrice() == gsReference.getGasPrice() &&
                gs.getMethanePrice() == gsReference.getMethanePrice() &&
                gs.getReportUser() == gsReference.getReportUser() &&
                gs.getReportTimestamp() == null && // can't use compareTo since the left operand is null
                gs.getReportDependability() == gsReference.getReportDependability()
        )
            areEqual = true;

        return areEqual;

    }
    
    List<UserDto> listOfUsersDto() throws SQLException {

		List<UserDto> usersDto = new ArrayList<>();
		Connection conn = DriverManager.getConnection("jdbc:h2:./data/memo;AUTO_SERVER=TRUE", "sa", "password");
		Statement stmt = null;
		String queryFindAll = "SELECT * FROM USER ";

		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(queryFindAll);

			while (rs.next()) {
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