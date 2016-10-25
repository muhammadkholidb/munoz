#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.data.dao;

import ${package}.data.entity.ExampleEntity;
import hu.pe.munoz.commondata.dao.GenericDaoImpl;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;


@Repository
public class ExampleDaoImpl extends GenericDaoImpl<ExampleEntity> implements ExampleDao {

    @Override
    public ExampleEntity findOneByColumn(String exampleColumn) {
        Query query = em.createQuery("SELECT a FROM ExampleEntity a WHERE a.exampleColumn = :exampleColumn");
        query.setParameter("exampleColumn", exampleColumn);
        try {
            return (ExampleEntity) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    
}
