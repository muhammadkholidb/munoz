package hu.pe.munoz.commondata.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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

import hu.pe.munoz.common.exception.DataException;
import hu.pe.munoz.common.exception.ExceptionCode;
import hu.pe.munoz.common.helper.CommonUtils;

/**
 *
 * @author eatonmunoz
 */
@Component
public class DataImporter {
    
    private static final Logger LOG = LoggerFactory.getLogger(DataImporter.class);
    
    @Value("${importer.sql.ResetSequencePreformatted}")
    private String sqlResetSequencePreformatted;
    
    @Value("${importer.sql.SequencePostfix}")
    private String sqlSequencePostfix;
    
    @Value("${importer.sql.SequencePrefix}")
    private String sqlSequencePrefix;

    @Value("${importer.sql.DoResetSequence}")
    private Boolean doResetSequence;
    
    @Autowired
    private DataSource dataSource;
    
    private String sqlSequenceName;
    
    private String sqlResetSequence;
    
    private List<File> fileDataSets;

    private List<InputStream> streamDataSets;

    public void importAll() throws DataException {
    	LOG.debug("Import dataset ...");
        try {
            if ((fileDataSets != null) && !fileDataSets.isEmpty()) {
                for (File file : fileDataSets) {
                    addStreamDataSet(new FileInputStream(file));
                }
                fileDataSets = null;
            }
            if ((streamDataSets != null) && !streamDataSets.isEmpty()) {
            	LOG.debug(streamDataSets.size() + " dataset(s) found");
                IDatabaseConnection connection = new DatabaseDataSourceConnection(dataSource);
                CleanInsertOperation operation = new CleanInsertOperation();
                for (int i = 0; i < streamDataSets.size(); i++) {
                	LOG.debug("Processing " + (i + 1));
                    IDataSet ids = new FlatXmlDataSetBuilder().build(streamDataSets.get(i));
                    operation.execute(connection, ids);
                }
                streamDataSets = null;
            }
            LOG.debug("Import done.");
        } catch (Exception ex) {
        	String message = CommonUtils.getExceptionMessage(ex);
        	LOG.error(message, ex);
            throw new DataException(ExceptionCode.E1007, message);
        }
    }
    
    public void addFileDataSet(File dataSet) {
        if (this.fileDataSets == null) {
            this.fileDataSets = new ArrayList<File>();
        }
        this.fileDataSets.add(dataSet);
    }

    public void addStreamDataSet(InputStream inputStreamDataSet) {
        if (this.streamDataSets == null) {
            this.streamDataSets = new ArrayList<InputStream>();
        }
        this.streamDataSets.add(inputStreamDataSet);
    }

    public void deleteAll() throws DataException {
    	try {
            if ((fileDataSets != null) && !fileDataSets.isEmpty()) {
                for (File file : fileDataSets) {
                    addStreamDataSet(new FileInputStream(file));
                }
                fileDataSets = null;
            }
            if ((streamDataSets != null) && !streamDataSets.isEmpty()) {
                IDatabaseConnection connection = new DatabaseDataSourceConnection(dataSource);
                for (InputStream is : streamDataSets) {
                    IDataSet ids = new FlatXmlDataSetBuilder().build(is);
                    DatabaseOperation.DELETE_ALL.execute(connection, ids);
                }
                streamDataSets = null;
            }
        } catch (Exception ex) {
        	String message = CommonUtils.getExceptionMessage(ex);
        	LOG.error(message, ex);
            throw new DataException(ExceptionCode.E1007, message);
        }
    }
    
    private class CleanInsertOperation extends DatabaseOperation {

        private final String REPLACEMENT_KEY_DATE = "currentDate";
        private final String REPLACEMENT_KEY_MILLISECOND = "currentMillis";

        @Override
        public void execute(IDatabaseConnection idc, IDataSet ids) throws DatabaseUnitException, SQLException {

            ReplacementDataSet rds = new ReplacementDataSet(ids);

            rds.addReplacementObject("{" + REPLACEMENT_KEY_DATE + "}", new Date());
            rds.addReplacementObject("{" + REPLACEMENT_KEY_MILLISECOND + "}", System.currentTimeMillis());

            if (doResetSequence) {
            	// Reset sequences
                Statement statement = idc.getConnection().createStatement();
                String[] tables = ids.getTableNames();
                for (String table : tables) {
                    try {
                        String query = null;
                        if ((sqlResetSequence != null) && !sqlResetSequence.isEmpty()) {
                        	query = sqlResetSequence;
                        } else if ((sqlSequenceName != null) && !sqlSequenceName.isEmpty()) {
                    		query = String.format(sqlResetSequence, sqlSequenceName);
                        } else {
                    		query = String.format(sqlResetSequence, (sqlSequencePrefix + table + sqlSequencePostfix));
                    	}
                        statement.execute(query);
                    } catch (Exception e) {
                        LOG.error(e.toString(), e);
                    }
                }
            }
            
            DatabaseOperation.CLEAN_INSERT.execute(idc, rds);
        }
        
    }

    public String getSqlResetSequencePreformatted() {
		return sqlResetSequencePreformatted;
	}

	public void setSqlResetSequencePreformatted(String sqlResetSequencePreformatted) {
		this.sqlResetSequencePreformatted = sqlResetSequencePreformatted;
	}

	public String getSqlSequencePostfix() {
		return sqlSequencePostfix;
	}

	public void setSqlSequencePostfix(String sqlSequencePostfix) {
		this.sqlSequencePostfix = sqlSequencePostfix;
	}

	public String getSqlSequencePrefix() {
		return sqlSequencePrefix;
	}

	public void setSqlSequencePrefix(String sqlSequencePrefix) {
		this.sqlSequencePrefix = sqlSequencePrefix;
	}

	public Boolean getDoResetSequence() {
		return doResetSequence;
	}

	public void setDoResetSequence(Boolean doResetSequence) {
		this.doResetSequence = doResetSequence;
	}

	public String getSqlSequenceName() {
		return sqlSequenceName;
	}

	public void setSqlSequenceName(String sqlSequenceName) {
		this.sqlSequenceName = sqlSequenceName;
	}

	public String getSqlResetSequence() {
		return sqlResetSequence;
	}

	public void setSqlResetSequence(String sqlResetSequence) {
		this.sqlResetSequence = sqlResetSequence;
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
