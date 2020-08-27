package it.polito.ezgas;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.polito.ezgas.converter.GasStationConverter;
import it.polito.ezgas.dto.GasStationDto;
import it.polito.ezgas.entity.GasStation;

@RunWith(SpringRunner.class)
@SpringBootTest

public class GasStationConverterTests {
	
	GasStation gs1 = new GasStation("GasStation 1", "Address 1", true, true, true, true, true, "Enjoy", 10.1, 10.1, 1.1, 1.1, 1.1, 1.1, 1.1, 5, "2020-05-20 18:05:24", 90);
	GasStation gs2 = new GasStation("GasStation 2", "Address 2", true, true, true, true, true, "Enjoy", 10.2, 10.2, 1.2, 1.2, 1.2, 1.2, 1.2, 5, "2020-05-20 18:05:24", 95);
	GasStation gsTest;
	List<GasStation> gsArray = new ArrayList<>();
	
	GasStationDto gsDto = new GasStationDto(1, "GasStationDto 3", "Address 3", true, true, true, true, true, "Car2Go", 10.3, 10.3, 1.3, 1.3, 1.3, 1.3, 1.3, 5, "2020-05-20 18:05:24", 100);
	GasStationDto gsDtoTest;
	List<GasStationDto> gsDtoArray = new ArrayList<>();
	
	GasStationConverter gsConverter = new GasStationConverter();

	@Test
	public void testConvertToDto() {
		gsDtoTest = gsConverter.convertToDto(gs1);
		assert(gsDtoTest.getGasStationName().equals(gs1.getGasStationName()));
		assert(gsDtoTest.getGasStationAddress().equals(gs1.getGasStationAddress()));
		assertTrue(gsDtoTest.getHasDiesel());
		assertTrue(gsDtoTest.getHasGas());
		assertTrue(gsDtoTest.getHasMethane());
		assertTrue(gsDtoTest.getHasSuper());
		assertTrue(gsDtoTest.getHasSuperPlus());
		assert(gsDtoTest.getCarSharing().equals("Enjoy"));
		assert(gsDtoTest.getLat()==10.1);
		assert(gsDtoTest.getLon()==10.1);
		assert(gsDtoTest.getDieselPrice()==1.1);
		assert(gsDtoTest.getGasPrice()==1.1);
		assert(gsDtoTest.getMethanePrice()==1.1);
		assert(gsDtoTest.getSuperPlusPrice()==1.1);
		assert(gsDtoTest.getSuperPrice()==1.1);
		assert(gsDtoTest.getReportUser()==5);
		assert(gsDtoTest.getReportTimestamp().equals(gs1.getReportTimestamp()));
		assert(gsDtoTest.getReportDependability()==90);
	}
	
	@Test
	public void testConvertToDtoNull() {
		gsDtoTest = gsConverter.convertToDto(null);
		assertNull(gsDtoTest);
	}
	
	@Test
	public void testConvertToEntity() {
		gsTest = gsConverter.convertToEntity(gsDto);
		assert(gsTest.getGasStationName().equals(gsDto.getGasStationName()));
		assert(gsTest.getGasStationAddress().equals(gsDto.getGasStationAddress()));
		assertTrue(gsTest.getHasDiesel());
		assertTrue(gsTest.getHasGas());
		assertTrue(gsTest.getHasMethane());
		assertTrue(gsTest.getHasSuper());
		assertTrue(gsTest.getHasSuperPlus());
		assert(gsTest.getCarSharing().equals("Car2Go"));
		assert(gsTest.getLat()==10.3);
		assert(gsTest.getLon()==10.3);
		assert(gsTest.getDieselPrice()==1.3);
		assert(gsTest.getGasPrice()==1.3);
		assert(gsTest.getMethanePrice()==1.3);
		assert(gsTest.getSuperPlusPrice()==1.3);
		assert(gsTest.getSuperPrice()==1.3);
		assert(gsTest.getReportUser()==5);
		assert(gsTest.getReportTimestamp().equals(gsDto.getReportTimestamp()));
		assert(gsTest.getReportDependability()==100);
	}
	
	@Test 
	public void testConvertToEntityNull() {
		gsTest = gsConverter.convertToEntity(null);
		assertNull(gsTest);
	}
	
	@Test
	public void testConvertToDtos() {
		gsArray.add(gs1);
		gsArray.add(gs2);
		
		gsDtoArray = gsConverter.convertToDtos(gsArray);
		
		int i=0;
		for( GasStationDto res : gsDtoArray ) {
			assert(res.getGasStationName().equals(gsArray.get(i).getGasStationName()));
			assert(res.getGasStationAddress().equals(gsArray.get(i).getGasStationAddress()));
			assertTrue(res.getHasDiesel());
			assertTrue(res.getHasGas());
			assertTrue(res.getHasMethane());
			assertTrue(res.getHasSuper());
			assertTrue(res.getHasSuperPlus());
			assert(res.getCarSharing().equals(gsArray.get(i).getCarSharing()));
			assert(res.getLat()==gsArray.get(i).getLat());
			assert(res.getLon()==gsArray.get(i).getLon());
			assert(res.getDieselPrice()==gsArray.get(i).getDieselPrice());
			assert(res.getGasPrice()==gsArray.get(i).getGasPrice());
			assert(res.getMethanePrice()==gsArray.get(i).getMethanePrice());
			assert(res.getSuperPlusPrice()==gsArray.get(i).getSuperPlusPrice());
			assert(res.getSuperPrice()==gsArray.get(i).getSuperPrice());
			assert(res.getReportUser()==gsArray.get(i).getReportUser());
			assert(res.getReportTimestamp().equals(gsArray.get(i).getReportTimestamp()));
			assert(res.getReportDependability()==gsArray.get(i).getReportDependability());
			i++;
		}
	}
	
	@Test
	public void testConvertToDtosNull() {
		gsDtoArray = gsConverter.convertToDtos(null);
		for( GasStationDto res : gsDtoArray )
			assertNull(res);
		assert(gsDtoArray.size()==0);
	}
}
