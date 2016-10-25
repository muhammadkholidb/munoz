#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.data.bo;

import hu.pe.munoz.commondata.helper.Dto;
import java.util.List;

public interface ExampleBo {

    List<Dto> getAllExamples(Dto dtoInput);

}
