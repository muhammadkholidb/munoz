package hu.pe.munoz.commondata.dao;

import java.util.List;

/**
 * 
 * Read: 
 * http://www.codeproject.com/Articles/251166/The-Generic-DAO-pattern-in-Java-with-Spring-3-and
 * http://www.baeldung.com/2011/12/08/simplifying-the-data-access-layer-with-spring-and-java-generics/
 */
public interface GenericDao<E> {
    
    E insert(E e);
    
    E update(E e);
    
    void deleteById(Object id);

	void delete(E e);
        
    E findById(Object id);
    
    List<E> findAll();

}
