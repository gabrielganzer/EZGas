package it.polito.ezgas.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polito.ezgas.entity.GasStation;

public class GasStationRepositoryImpl implements CustomizedGasStationRepository {
	
    public enum SortOrder{ASC, DESC, NULL};

    private String availableGasolines[] = {"diesel", "super", "superplus", "gas", "methane"};    
	//radius of the earth
	private final Integer R = 6371;

	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
    @Override
	public List<GasStation> findAllByGasolineTypeAndCarSharing(String gasolineType, String carSharing, SortOrder order) {
		
	    //SHOULD NOT get in this situation. Ideally the caller of this function MUST HAVE done some checks on the passed paramters
        if(gasolineType == null || carSharing == null) {
            System.out.println("function findAllByGasolineTypeAndCarSharing(): parameters are null");
            // It's a placeholder. It probably should THROW an exception here but it doesn't matter since WE SHOULD NEVER get here.
            return new ArrayList<GasStation>();
        }

        List<GasStation> listOfGasStations = new ArrayList<>();
		carSharing = carSharing.toLowerCase();
		boolean addAND = false;
        String orderByParameter = "";
		
		String baseQuery = "SELECT gs "
                         + "FROM GasStation gs "
                         + "WHERE ";
		
		StringBuilder query = new StringBuilder(baseQuery);

		if(carSharing.compareToIgnoreCase("") != 0) {
		    query.append("LOWER(carSharing) = '" + carSharing + "' ");
		    orderByParameter = "gasStationName";
		    addAND = true;
		}

		if(gasolineType.compareToIgnoreCase("") != 0) {
            
            boolean foundGasoline = false;
            // need this part for the hasGasolineType variable. If gasolineType is different than the ones
            // in availableGasoline, it would generate a query exception at run time
            for(String gasoline: availableGasolines){
                if(gasoline.compareToIgnoreCase(gasolineType) == 0) {
                    foundGasoline = true;
                    break;
                }
            }

            if(foundGasoline){
                String gasolineTypePrice = gasolineType.toLowerCase() + "Price";
                String hasGasolineType = "has" + gasolineType.substring(0, 1).toUpperCase() + gasolineType.substring(1).toLowerCase();

                if(addAND)
                    query.append("AND ");

                query.append(hasGasolineType + " = TRUE ");
                orderByParameter = gasolineTypePrice;
            }

		}
        
        if(orderByParameter.compareToIgnoreCase("") != 0){
            query.append("ORDER BY " + orderByParameter + " " + order);
        
            listOfGasStations = this.entityManager.createQuery(query.toString()).getResultList();
        }

		return listOfGasStations;
	
	}

	@Override
	public List<GasStation> findAllByProximity(double lat, double lon, SortOrder order) {

		// https://developers.google.com/maps/solutions/store-locator/clothing-store-locator#findnearsql

		String query = "SELECT gs " +  
					   "FROM GasStation gs " + 
					   "WHERE (" + R + " * acos(" + 
					                       " cos( radians(" + lat + ") ) " + 
					                       " * cos( radians( lat ) ) " + 
					                       " * cos( radians( lon ) - radians(" + lon + ") ) " + 
					                       " + sin( radians(" + lat + ") ) " + 
					                       " * sin( radians( lat ) ) " + 
										   ") " + 
							  ") <= 1 " +
					   "ORDER BY (" + R + " * acos(" + 
                                               " cos( radians(" + lat + ") ) " + 
                                               " * cos( radians( lat ) ) " + 
                                               " * cos( radians( lon ) - radians(" + lon + ") ) " + 
                                               " + sin( radians(" + lat + ") ) " + 
                                               " * sin( radians( lat ) ) " + 
                                               ") " + 
                                 ") " + order;
		
		@SuppressWarnings("unchecked")
		List<GasStation> listOfGasStations = this.entityManager.createQuery(query).getResultList();
		
		return listOfGasStations;	
	
	}

}
