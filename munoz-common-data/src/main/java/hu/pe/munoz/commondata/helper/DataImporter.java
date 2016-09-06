package hu.pe.munoz.commondata.helper;

import hu.pe.munoz.common.exception.DataException;
import hu.pe.munoz.common.exception.ExceptionCode;
import hu.pe.munoz.common.helper.CommonUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author eatonmunoz
 */
@Component
public class DataImporter {
    
    private static final Logger LOG = LoggerFactory.getLogger(DataImporter.class);
    
    @Value("${sql.template.ResetSequence}")
    private String sqlResetSequence;
    
    @Value("${sql.template.SequencePostfix}")
    private String sqlSequencePostfix;
    
    @Value("${sql.template.SequencePrefix}")
    private String sqlSequencePrefix;
    
    @Autowired
    private DataSource dataSource;
    
    private List<File> fileDataSets;

    private List<InputStream> streamDataSets;

    public void process() throws DataException {
        try {
            if ((fileDataSets != null) && !fileDataSets.isEmpty()) {
                for (File file : fileDataSets) {
                    addStreamDataSet(new FileInputStream(file));
                }
                fileDataSets = null;
            }
            if ((streamDataSets != null) && !streamDataSets.isEmpty()) {
                IDatabaseConnection connection = new DatabaseDataSourceConnection(dataSource);
                CleanInsertOperation operation = new CleanInsertOperation();
                for (InputStream is : streamDataSets) {
                    IDataSet ids = new FlatXmlDataSetBuilder().build(is);
                    operation.execute(connection, ids);
                }
                streamDataSets = null;
            }
        } catch (SQLException | IOException | DatabaseUnitException ex) {
            throw new DataException(ExceptionCode.E1007, CommonUtils.getExceptionMessage(ex));
        }
    }
    
    public void addFileDataSet(File dataSet) {
        if (this.fileDataSets == null) {
            this.fileDataSets = new ArrayList<>();
        }
        this.fileDataSets.add(dataSet);
    }

    public void addStreamDataSet(InputStream inputStreamDataSet) {
        if (this.streamDataSets == null) {
            this.streamDataSets = new ArrayList<>();
        }
        this.streamDataSets.add(inputStreamDataSet);
    }

    private class CleanInsertOperation extends DatabaseOperation {

        private final String REPLACEMENT_KEY_DATE = "currentDate";
        private final String REPLACEMENT_KEY_MILLISECOND = "currentMillis";

        @Override
        public void execute(IDatabaseConnection idc, IDataSet ids) throws DatabaseUnitException, SQLException {

            ReplacementDataSet rds = new ReplacementDataSet(ids);

            rds.addReplacementObject("{" + REPLACEMENT_KEY_DATE + "}", new Date());
            rds.addReplacementObject("{" + REPLACEMENT_KEY_MILLISECOND + "}", System.currentTimeMillis());

            // Reset sequences
            Statement statement = idc.getConnection().createStatement();
            String[] tables = ids.getTableNames();
            for (String table : tables) {
                try {
                    String query = String.format(sqlResetSequence, (sqlSequencePrefix + table + sqlSequencePostfix));
                    statement.execute(query);
                } catch (Exception e) {
                    LOG.error(e.toString(), e);
                }
            }
            
            DatabaseOperation.CLEAN_INSERT.execute(idc, rds);
        }
        
    }

    public List<File> getFileDataSets() {
        return fileDataSets;
    }

    public void setFileDataSets(List<File> fileDataSets) {
        this.fileDataSets = fileDataSets;
    }

    public List<InputStream> getStreamDataSets() {
        return streamDataSets;
    }

    public void setStreamDataSets(List<InputStream> streamDataSets) {
        this.streamDataSets = streamDataSets;
    }
    
}
