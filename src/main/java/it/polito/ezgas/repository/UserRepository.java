package it.polito.ezgas.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import it.polito.ezgas.entity.User;

/* methods inherited from CrudRepository
 *+count() : long 
 *+delete(T entity) : void
 *+deleteAll(T entity) : void
 *+deleteAll(Iterable<T> entities): void
 *+deleteById(ID id) : void
 *+existsById(ID id) : Boolean
 *+findAll() : Iterable<T>
 *+findAllById(Iterable<ID> ids) : Iterable<T>
 *+findById(ID id) : Optional<T>
 *+save(T entity) : T
 *+saveAll(Iterable<T> entities) : Iterable<T>
**/

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
	User findByEmail(String email);
}
