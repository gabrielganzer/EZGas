package it.polito.ezgas.repository;

import it.polito.ezgas.entity.GasStation;
import it.polito.ezgas.repository.GasStationRepositoryImpl.SortOrder;

import java.util.List;

public interface CustomizedGasStationRepository {
	
	List<GasStation> findAllByGasolineTypeAndCarSharing(String gasolineType, String carSharing, SortOrder order);
	List<GasStation> findAllByProximity(double lat, double lon, SortOrder order);
	
}
