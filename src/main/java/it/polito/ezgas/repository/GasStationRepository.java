package it.polito.ezgas.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import it.polito.ezgas.entity.GasStation;

@Repository
public interface GasStationRepository extends CrudRepository<GasStation, Integer>, CustomizedGasStationRepository {

}
