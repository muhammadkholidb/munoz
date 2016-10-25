#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.data.bo;

import ${package}.data.dao.ExampleDao;
import ${package}.data.entity.ExampleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.pe.munoz.commondata.helper.Dto;
import hu.pe.munoz.commondata.helper.DtoUtils;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ExampleBoImpl implements ExampleBo {

    @Autowired
    private ExampleDao exampleDao;

    @Override
    public List<Dto> getAllExamples(Dto dtoInput) {
        
        // No validation
        
        List<ExampleEntity> list = exampleDao.findAll();
        List<Dto> listDto = new ArrayList<Dto>();
        for (ExampleEntity example : list) {
            Dto dtoExample = DtoUtils.toDto(example);
            Dto dto = DtoUtils.omit(dtoExample, "createdAt", "modifiedAt");
            listDto.add(dto);
        }
        
        return listDto;
    }

}
