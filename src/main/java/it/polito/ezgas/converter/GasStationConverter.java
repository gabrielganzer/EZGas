package it.polito.ezgas.converter;

import java.util.ArrayList;
import java.util.List;

import it.polito.ezgas.dto.GasStationDto;
import it.polito.ezgas.dto.UserDto;
import it.polito.ezgas.entity.GasStation;

/**
 * 
 */

public class GasStationConverter implements CustomConverter<GasStation, GasStationDto>{
	
	@Override
	public GasStationDto convertToDto( GasStation aGasStation ) {

        GasStationDto gsDto = null;      

        if(aGasStation != null){
            gsDto = new GasStationDto(aGasStation.getGasStationId(), aGasStation.getGasStationName(), aGasStation.getGasStationAddress() , 
                                    aGasStation.getHasDiesel(), aGasStation.getHasSuper(), aGasStation.getHasSuperPlus(), aGasStation.getHasGas() , 
                                    aGasStation.getHasMethane(), aGasStation.getCarSharing(), aGasStation.getLat(), aGasStation.getLon(),
                                    aGasStation.getDieselPrice(), aGasStation.getSuperPrice(), aGasStation.getSuperPlusPrice(), aGasStation.getGasPrice() , 
                                    aGasStation.getMethanePrice(), aGasStation.getReportUser(), aGasStation.getReportTimestamp(), aGasStation.getReportDependability() ) ;
            
            if(aGasStation.getUser() != null) {
                UserConverter userConverter = new UserConverter();
                UserDto userDto = userConverter.convertToDto(aGasStation.getUser());
                gsDto.setUserDto(userDto);
            };
            
        }

		return gsDto;

	}
	
	@Override
	public GasStation convertToEntity( GasStationDto aGasStationDto ) {
		
        GasStation gs = null;

		if(aGasStationDto != null) {
			gs = new GasStation(aGasStationDto.getGasStationName(), aGasStationDto.getGasStationAddress() , 
                                aGasStationDto.getHasDiesel(), aGasStationDto.getHasSuper(), aGasStationDto.getHasSuperPlus(), aGasStationDto.getHasGas() , 
                                aGasStationDto.getHasMethane(), aGasStationDto.getCarSharing(), aGasStationDto.getLat(), aGasStationDto.getLon(),
                                aGasStationDto.getDieselPrice(), aGasStationDto.getSuperPrice(), aGasStationDto.getSuperPlusPrice(), aGasStationDto.getGasPrice() , 
                                aGasStationDto.getMethanePrice(), aGasStationDto.getReportUser(), aGasStationDto.getReportTimestamp(), aGasStationDto.getReportDependability() ) ;
    
			if(aGasStationDto.getGasStationId() != null) { 
			    gs.setGasStationId(aGasStationDto.getGasStationId());
			}
		}
		
		return gs;

	}
	
	@Override
	public List<GasStationDto> convertToDtos(List<GasStation> gasStations) {

		List<GasStationDto> arrayListGSDtos = new ArrayList<>();
		
		if(gasStations != null) {
    		for( GasStation gs : gasStations ) {
    			arrayListGSDtos.add(this.convertToDto(gs));
    		}
    	}
		
		return arrayListGSDtos;

	}
}
