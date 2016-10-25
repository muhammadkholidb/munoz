#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.data.dao;

import ${package}.data.entity.ExampleEntity;
import hu.pe.munoz.commondata.dao.GenericDao;

public interface ExampleDao extends GenericDao<ExampleEntity> {

    ExampleEntity findOneByColumn(String exampleColumn);
}
