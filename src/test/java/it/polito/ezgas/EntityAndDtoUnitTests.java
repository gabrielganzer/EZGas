package it.polito.ezgas;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.polito.ezgas.entity.*;
import it.polito.ezgas.dto.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EntityAndDtoUnitTests {

	@Test
	public void testCase1() {
		GasStation gs = new GasStation();
		gs.setGasStationId(null);
		assert(gs.getGasStationId() == null);
	}
	
	@Test
	public void testCase2() {
		GasStation gs = new GasStation();
		gs.setGasStationId(1);
		assert(gs.getGasStationId() == 1);
	}
	
	@Test
	public void testCase3() {
		GasStation gs = new GasStation();
		gs.setGasStationId(Integer.MIN_VALUE);
		assert(gs.getGasStationId() == Integer.MIN_VALUE);
	}
	
	@Test
	public void testCase4() {
		GasStation gs = new GasStation();
		gs.setGasStationId(Integer.MAX_VALUE);
		assert(gs.getGasStationId() == Integer.MAX_VALUE);
	}
	
	@Test
	public void testCase5() {
		PriceReport pr = new PriceReport();
		pr.setDieselPrice(1.0);
		assert(pr.getDieselPrice() == 1.0);
	}
	
	@Test
	public void testCase6() {
		PriceReport pr = new PriceReport();
		pr.setDieselPrice(Double.MIN_VALUE);
		assert(pr.getDieselPrice() == Double.MIN_VALUE);
	}
	
	@Test
	public void testCase7() {
		PriceReport pr = new PriceReport();
		pr.setDieselPrice(Double.MAX_VALUE);
		assert(pr.getDieselPrice() == Double.MAX_VALUE);
	}

	@Test
	public void testCase8() {
		User u = new User();
		u.setUserName(null);
		assert(u.getUserName() == null);
	}
	
	@Test
	public void testCase9() {
		User u = new User();
		u.setUserName("");
		assert(u.getUserName() == "");
	}
	
	@Test
	public void testCase10() {
		User u = new User();
		u.setUserName("userName");
		assert(u.getUserName() == "userName");
	}
	
	@Test
	public void testCase11() {
		GasStationDto gsd = new GasStationDto();
		gsd.setUserDto(null);
		assert(gsd.getUserDto() == null);
	}
	
	@Test
	public void testCase12() {
		GasStationDto gsd = new GasStationDto();
		UserDto ud = new UserDto();
		gsd.setUserDto(ud);
		assert(gsd.getUserDto() == ud);
	}
	
	@Test
	public void testCase13() {
		LoginDto ld = new LoginDto();
		ld.setAdmin(null);
		assert(ld.getAdmin() == null);
	}
	
	@Test
	public void testCase14() {
		LoginDto ld = new LoginDto();
		ld.setAdmin(true);
		assert(ld.getAdmin() == true);
	}
	
	@Test
	public void testCase15() {
		LoginDto ld = new LoginDto();
		ld.setAdmin(false);
		assert(ld.getAdmin() == false);
	}
	
	@Test
	public void testCase16() {
		PriceReportDto prd = new PriceReportDto();
		prd.setUser(null);
		assert(prd.getUser() == null);
	}
	
	@Test
	public void testCase17() {
		PriceReportDto prd = new PriceReportDto();
		User u = new User();
		prd.setUser(u);
		assert(prd.getUser() == u);
	}

}
