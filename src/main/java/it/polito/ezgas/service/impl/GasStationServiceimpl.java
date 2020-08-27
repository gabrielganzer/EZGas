package it.polito.ezgas.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import exception.GPSDataException;
import exception.InvalidGasStationException;
import exception.InvalidGasTypeException;
import exception.InvalidUserException;
import exception.PriceException;
import it.polito.ezgas.converter.CustomConverter;
import it.polito.ezgas.converter.GasStationConverter;
import it.polito.ezgas.converter.UserConverter;
import it.polito.ezgas.dto.GasStationDto;
import it.polito.ezgas.dto.UserDto;
import it.polito.ezgas.entity.GasStation;
import it.polito.ezgas.entity.User;
import it.polito.ezgas.service.GasStationService;
import it.polito.ezgas.service.UserService;
import it.polito.ezgas.repository.*;
import it.polito.ezgas.repository.GasStationRepositoryImpl.SortOrder;
/**
 * Created by softeng on 27/4/2020.
 * 
 */

@Service
public class GasStationServiceimpl implements GasStationService {
	
    static final double DEFAULT_PRICE_NOT_SET = 0;

    @Autowired
	private GasStationRepository gasStationRepository;
    private CustomConverter<User, UserDto> userConverter = new UserConverter();
    private final CustomConverter<GasStation, GasStationDto> gasStationConverter = new GasStationConverter();
	@Autowired
	private UserService userService;
	
	public GasStationServiceimpl(GasStationRepository repository) {
		this.gasStationRepository = repository;
	}


	@Override		//		OK
	public GasStationDto getGasStationById(Integer gasStationId) throws InvalidGasStationException {
	    
	    if(gasStationId == null)
	        return null;
				
		if( gasStationId<0 ) 
            throw new InvalidGasStationException("Method getGasStationById(): errate GasStationId");
			
		GasStation gs = gasStationRepository.findOne(gasStationId);
		GasStationDto gs_Dto = gasStationConverter.convertToDto(updateDependability(gs));
		
		return gs_Dto;

	}

	
	@Override		// 		OK
	public GasStationDto saveGasStation(GasStationDto gasStationDto) throws PriceException, GPSDataException {
	    
	    if(gasStationDto == null)
            return null;
	    
        if(isAPriceNegative(gasStationDto)){
			throw new PriceException("Method saveGasStation(): one or more prices are negative");
        }

		if(gasStationDto.getLat() > 90 || gasStationDto.getLat() < -90 || gasStationDto.getLon() > 180 || gasStationDto.getLon() < -180)
			throw new GPSDataException("Method saveGasStation(): errate Lat/Lon");
		
        GasStationDto gsDto = gasStationDto;

        // update an existing gas station. The fuel's prices of "gasStationDto" are equals to the ones displayed in admin -> "List of gas stations"
        if(gsDto.getGasStationId() != null && gasStationRepository.exists(gsDto.getGasStationId())){
            GasStation gs = gasStationRepository.findOne(gasStationDto.getGasStationId());
            if(!gsDto.getHasDiesel()){
                gsDto.setDieselPrice(DEFAULT_PRICE_NOT_SET);
            }
            if(!gsDto.getHasSuper()){
                gsDto.setSuperPrice(DEFAULT_PRICE_NOT_SET);
            }
            if(!gsDto.getHasSuperPlus()){
                gsDto.setSuperPlusPrice(DEFAULT_PRICE_NOT_SET);
            }
            if(!gsDto.getHasMethane()){
                gsDto.setMethanePrice(DEFAULT_PRICE_NOT_SET);
            }
            if(!gsDto.getHasGas()){
                gsDto.setGasPrice(DEFAULT_PRICE_NOT_SET);
            }
            gsDto.setReportDependability(gs.getReportDependability());
            gsDto.setReportTimestamp(gs.getReportTimestamp());
            gsDto.setReportUser(gs.getReportUser());
            gsDto.setUserDto(userConverter.convertToDto(gs.getUser()));
        }
        // add a new gas station
        else{
            gsDto.setDieselPrice(DEFAULT_PRICE_NOT_SET);
            gsDto.setSuperPrice(DEFAULT_PRICE_NOT_SET);
            gsDto.setSuperPlusPrice(DEFAULT_PRICE_NOT_SET);
            gsDto.setMethanePrice(DEFAULT_PRICE_NOT_SET);
            gsDto.setGasPrice(DEFAULT_PRICE_NOT_SET);
            gsDto.setReportUser(null);	// Added for CR4
        }
        
        if (gsDto.getCarSharing().compareTo("null") == 0)
        	gsDto.setCarSharing(null);
        
        GasStation gs = gasStationRepository.save(gasStationConverter.convertToEntity(gsDto));
		
		return gasStationConverter.convertToDto(gs);
		
	}

	
	@Override		//		OK	
	public List<GasStationDto> getAllGasStations() {
		
		List<GasStation> lst = new ArrayList<>();
		
        Iterable<GasStation> iterable = gasStationRepository.findAll();
		iterable.forEach(station -> lst.add(station));
		lst.forEach(station -> updateDependability(station));
		
        return gasStationConverter.convertToDtos(lst);
	    
	}

	
	@Override      //		OK				
	public Boolean deleteGasStation(Integer gasStationId) throws InvalidGasStationException {
	    
	    if(gasStationId == null)
            return null;
				
		if( gasStationId<0 ) 
            throw new InvalidGasStationException("Method deleteGasStation(): gasStationId is negative");
		
		if( gasStationRepository.exists(gasStationId) ) {
			gasStationRepository.delete(gasStationId);
			return true; 
		}
		
		return null;

	}

	
	@Override      //		OK
	public List<GasStationDto> getGasStationsByGasolineType(String gasolinetype) throws InvalidGasTypeException {
		
		if(gasolinetype == null)
		    return new ArrayList<GasStationDto>();
		
		if(gasolinetype.toLowerCase().compareTo("methane") != 0 	&& 
		   gasolinetype.toLowerCase().compareTo("gas") != 0 		&& 
		   gasolinetype.toLowerCase().compareTo("diesel") != 0 	    && 
		   gasolinetype.toLowerCase().compareTo("super") != 0		&& 
		   gasolinetype.toLowerCase().compareTo("superplus") != 0
           )
        {
			throw new InvalidGasTypeException("Method getGasStationsByGasolineType(): gasolineType not found");
        }

		List<GasStation> lst = gasStationRepository.findAllByGasolineTypeAndCarSharing(gasolinetype, "", SortOrder.ASC);
        lst.forEach(station -> updateDependability(station));
		
		return gasStationConverter.convertToDtos(lst);

	}

	
	@Override		//		OK
	public List<GasStationDto> getGasStationsByProximity(double lat, double lon) throws GPSDataException {
		
		if( lat>90 || lat<-90 || lon>180 || lon<-180 )	// (DD)  23.444 12.442
			throw new GPSDataException("Method getGasStationsByProximity(): errate Lat/Lon");
		
		List<GasStation> lst = gasStationRepository.findAllByProximity(lat, lon, SortOrder.ASC);
        lst.forEach(station -> updateDependability(station));
        
        return gasStationConverter.convertToDtos(lst);
		
	}

	@Override		//		OK
	public List<GasStationDto> getGasStationsWithCoordinates(double lat, double lon, String gasolinetype,
			String carsharing) throws InvalidGasTypeException, GPSDataException {
	    
	    if( lat>90 || lat<-90 || lon>180 || lon<-180 )
			throw new GPSDataException("Method getGasStationsWithCoordinates(): Errate Lat/Lon");
	    
	    if(gasolinetype == null || carsharing == null)
	            return new ArrayList<GasStationDto>();
		
	    if (gasolinetype.toLowerCase().compareTo("select gasoline type") == 0 ) {
	    	gasolinetype = "null";
	    }
	    
	    if(gasolinetype.toLowerCase().compareTo("methane")!=0 	&& 
		   gasolinetype.toLowerCase().compareTo("gas")!=0 		&& 
		   gasolinetype.toLowerCase().compareTo("diesel")!=0 	&& 
		   gasolinetype.toLowerCase().compareTo("super")!=0		&& 
		   gasolinetype.toLowerCase().compareTo("superplus") != 0 &&
		   gasolinetype.toLowerCase().compareTo("null") != 0
        )
        {
			throw new InvalidGasTypeException("Method getGasStationsWithCoordinates(): gasolineType Not Found");
        }

        Boolean filterByGasolineType = gasolinetype.compareToIgnoreCase("null") != 0 ? true : false;
        Boolean filterByCarsharing = carsharing.compareToIgnoreCase("null") != 0 ? true : false;

        List<GasStationDto> lstGasStationDtos = getGasStationsByProximity(lat, lon);
        
        if(filterByCarsharing) {
            lstGasStationDtos = lstGasStationDtos.stream().filter((station) -> 
            {
                if(station.getCarSharing() != null && station.getCarSharing().compareToIgnoreCase(carsharing) == 0)
                    return true;
                
                return false;
            }).collect(Collectors.toList());
        }
        
        if(filterByGasolineType){
                switch(gasolinetype.toLowerCase()){
                    case "methane":
                        lstGasStationDtos = lstGasStationDtos.stream().filter(station -> station.getHasMethane())
                                                                      .collect(Collectors.toList());
                        break;
                    case "gas":
                        lstGasStationDtos = lstGasStationDtos.stream().filter(station -> station.getHasGas())
                                                                      .collect(Collectors.toList());
                        break;
                    case "diesel":
                        lstGasStationDtos = lstGasStationDtos.stream().filter(station -> station.getHasDiesel())
                                                                      .collect(Collectors.toList());
                        break;
                    case "super":
                        lstGasStationDtos = lstGasStationDtos.stream().filter(station -> station.getHasSuper())
                                                                      .collect(Collectors.toList());
                        break;
                    case "superplus":
                        lstGasStationDtos = lstGasStationDtos.stream().filter(station -> station.getHasSuperPlus())
                                                                      .collect(Collectors.toList());
                        break;
                    default:
                        System.out.println("IMPOSSIBLE CASE getGasStationsWithCoordinates(): gasolinetype not found");
                        break;
                }
        }
        
        lstGasStationDtos.replaceAll(stationDto -> 
        { 
            GasStation gs = updateDependability(gasStationConverter.convertToEntity(stationDto));
            return gasStationConverter.convertToDto(gs);
        });
        
        return new ArrayList<GasStationDto>(lstGasStationDtos);

	}


	@Override		//		OK
	public List<GasStationDto> getGasStationsWithoutCoordinates(String gasolinetype, String carsharing)
			throws InvalidGasTypeException {
	    
	    if(gasolinetype == null || carsharing == null)
	        return new ArrayList<GasStationDto>();
		
		if(gasolinetype.toLowerCase().compareTo("methane")!=0 	&& 
		   gasolinetype.toLowerCase().compareTo("gas")!=0 		&& 
		   gasolinetype.toLowerCase().compareTo("diesel")!=0 	&& 
		   gasolinetype.toLowerCase().compareTo("super")!=0		&& 
		   gasolinetype.toLowerCase().compareTo("superplus")!=0 &&
           gasolinetype.toLowerCase().compareTo("null")!=0 
        )
        {
			throw new InvalidGasTypeException("Method getGasStationsWithoutCoordinates(): gasolineType not found");
        }

		gasolinetype = gasolinetype.compareToIgnoreCase("null") == 0 ? "" : gasolinetype;
        carsharing = carsharing.compareToIgnoreCase("null") == 0 ? "" : carsharing; 

		List<GasStation> lstGC = gasStationRepository.findAllByGasolineTypeAndCarSharing(gasolinetype, carsharing, SortOrder.ASC);
		lstGC.forEach(station -> updateDependability(station));
		List<GasStationDto> list_Dto = gasStationConverter.convertToDtos(lstGC);
		
		return list_Dto;

	}

	
	@Override		// the prices of gasoline types which are not available to a gas station are set to DEFAULT_PRICE_NOT_SET
	public void setReport(Integer gasStationId, double dieselPrice, double superPrice, double superPlusPrice,
			double gasPrice, double methanePrice, Integer userId)
			throws InvalidGasStationException, PriceException, InvalidUserException {
	    
	    if(gasStationId == null || userId == null)
	        return;
	    
	    if(gasStationId < 0)
	        throw new InvalidGasStationException("Method setReport(): gasStationId is negative");
	    
	    if(userId < 0) {
            throw new InvalidUserException("Method setReport(): userId is negative");
        }

		UserDto userDto = userService.getUserById(userId);
		GasStation gs = gasStationRepository.findOne(gasStationId);
        // if user or gas station is not found we simply return and do nothing
        // this case should be impossible since whoever called this function must have got the userId/gasStationId from the DB
        if(userDto == null || gs == null)
            return;

		if( dieselPrice < 0 && gs.getHasDiesel() || 
            superPrice < 0 && gs.getHasSuper() || 
            superPlusPrice < 0 && gs.getHasSuperPlus() || 
            gasPrice < 0 && gs.getHasGas() || 
            methanePrice < 0 && gs.getHasMethane() 
        )
        {
			throw new PriceException("Method setReport(): one or more prices are negative");
        }
	    
		// Setup dataPattern
		String datePattern = "MM-dd-yyyy";
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
		String todayDate = dateFormatter.format(LocalDate.now());
	    
		Integer ID = gs.getReportUser();
		double trustLevel = 50*( userDto.getReputation() + 5 )/10 + 50*1;
		
		
		//		CR4
		if(ID != null) {		// gasStation already has a report
			UserDto userDto_2 = userService.getUserById(ID);
			LocalDate then = LocalDate.parse(gs.getReportTimestamp(), dateFormatter);
		    LocalDate now = LocalDate.now();
		    long diffInDays = ChronoUnit.DAYS.between(then, now);
		    
			if(userDto.getReputation() > userDto_2.getReputation()) {	// Variant 1
				// the previous price list is overwritten
				if(gs.getHasDiesel())
				    gs.setDieselPrice(dieselPrice);
				if(gs.getHasSuper())
				    gs.setSuperPrice(superPrice);
				if(gs.getHasSuperPlus())
				    gs.setSuperPlusPrice(superPlusPrice);
				if(gs.getHasGas())
				    gs.setGasPrice(gasPrice);
				if(gs.getHasMethane())
				    gs.setMethanePrice(methanePrice);
				
				gs.setReportUser(userId);
				gs.setReportDependability(trustLevel);
				gs.setReportTimestamp(todayDate);
				gs.setUser(userConverter.convertToEntity(userDto));

				gs = gasStationRepository.save(gs);
				return;
			}
			else if(userDto.getReputation() < userDto_2.getReputation()  &&  diffInDays > 4 ) {		// Variant 2
				// the previous price list is overwritten
				if(gs.getHasDiesel())
				    gs.setDieselPrice(dieselPrice);
				if(gs.getHasSuper())
				    gs.setSuperPrice(superPrice);
				if(gs.getHasSuperPlus())
				    gs.setSuperPlusPrice(superPlusPrice);
				if(gs.getHasGas())
				    gs.setGasPrice(gasPrice);
				if(gs.getHasMethane())
				    gs.setMethanePrice(methanePrice);
				
				gs.setReportUser(userId);
				gs.setReportDependability(trustLevel);
				gs.setReportTimestamp(todayDate);
				gs.setUser(userConverter.convertToEntity(userDto));

				gs = gasStationRepository.save(gs);
				return;
			}
		}
		
		// first Report
	
        // double trustLevel = 50*( userDto.getReputation() + 5 )/10 + 50*1;	// 50*obsolence
        
		if(gs.getHasDiesel())
		    gs.setDieselPrice(dieselPrice);
		if(gs.getHasSuper())
		    gs.setSuperPrice(superPrice);
		if(gs.getHasSuperPlus())
		    gs.setSuperPlusPrice(superPlusPrice);
		if(gs.getHasGas())
		    gs.setGasPrice(gasPrice);
		if(gs.getHasMethane())
		    gs.setMethanePrice(methanePrice);
		
		gs.setReportUser(userId);
		gs.setReportDependability(trustLevel);
		gs.setReportTimestamp(todayDate);
		gs.setUser(userConverter.convertToEntity(userDto));

		gs = gasStationRepository.save(gs);
		
        return;

	}

	
	@Override		//		OK
	public List<GasStationDto> getGasStationByCarSharing(String carSharing) {
	    
	    if(carSharing == null)
	      return new ArrayList<GasStationDto>();
	   
		List<GasStation> lst = gasStationRepository.findAllByGasolineTypeAndCarSharing("", carSharing, SortOrder.ASC);
		
		lst.forEach(station -> updateDependability(station));
        
		List<GasStationDto> lst_Dto = gasStationConverter.convertToDtos(lst);
		
		return lst_Dto;
        
	}
	
	public GasStation updateDependability( GasStation gS ) {
		
			if(gS != null && gS.getReportTimestamp() != null) {
			  
                String datePattern = "MM-dd-yyyy";
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
                LocalDate then = LocalDate.parse(gS.getReportTimestamp(), dateFormatter);
			    LocalDate now = LocalDate.now();
                long diffInDays = ChronoUnit.DAYS.between(then, now);

			    if(diffInDays >= 7)
			        diffInDays = 7;

			    // FORMULA
			    // after 1  day ---> dependability1 = dependability - J*1/7
			    // after 7+ day ---> dependability7 = dependability - J*7/7
			    // dependability = K + J*1 --> K = 50*(U.trust_level + 5)/10; J = 50
			    double newDependability = gS.getReportDependability() - 50 * (double) diffInDays / 7;
			    gS.setReportDependability(newDependability);

			}
			
			return gS;
			
	}

    private Boolean isAPriceNegative(GasStationDto gasStationDto){
         
        Boolean isPriceNegative = false;
        double dieselPrice = gasStationDto.getDieselPrice();
        double gasPrice = gasStationDto.getGasPrice();
        double methanePrice = gasStationDto.getMethanePrice();
        double superPlusPrice = gasStationDto.getSuperPlusPrice();
        double superPrice = gasStationDto.getSuperPrice();
        
            if(gasStationDto.getGasStationId() == null) 
            {
                if((dieselPrice < 0 && dieselPrice != -1)       || 
                   (gasPrice < 0 && gasPrice != -1)             ||
                   (methanePrice < 0 && methanePrice != -1)     ||
                   (superPlusPrice < 0 && superPlusPrice != -1) ||
                   (superPrice < 0 && superPrice != -1)
                  ) 
                {
                    isPriceNegative = true;
                }
            }
            else
            {
                if(dieselPrice < 0      || 
                   gasPrice < 0         || 
                   methanePrice < 0     || 
                   superPlusPrice < 0   || 
                   superPrice < 0
                   )
                {
                    isPriceNegative = true;
                }
            }
   
        
         return isPriceNegative;

    }

}

	
	

