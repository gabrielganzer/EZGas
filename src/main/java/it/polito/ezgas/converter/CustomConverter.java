package it.polito.ezgas.converter;

import java.util.List;

public interface CustomConverter<E, D> {
	D convertToDto(E aEntity);
	E convertToEntity(D aDto);
	List<D> convertToDtos(List<E> aListOfEntities);
}
