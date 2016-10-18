package hu.pe.munoz.commondata;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import hu.pe.munoz.commondata.helper.DataImporter;

/**
 *
 * @author eatonmunoz
 */
public abstract class AbstractTestDataImport {

    private final static Logger LOG = LoggerFactory.getLogger(AbstractTestDataImport.class);
    
    @Autowired
    protected DataImporter importer;
    
    private List<File> dataSets;
    
    /**
     * Process the dataset XML file(s).
     * @param paths Dataset path relative to directory /src/test/resources
     * @throws java.lang.Exception
     */
    protected void processDataSets(String... paths) throws Exception {
        dataSets = new ArrayList<>();
        for (String path : paths) {
            URL url = AbstractTestDataImport.class.getClassLoader().getResource(path);
            File file = new File(url.getFile());
            dataSets.add(file);
            LOG.debug("Dataset file: " + file.getAbsolutePath());
        }
        
        try {
            importer.setFileDataSets(dataSets);
            importer.importAll();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw e;
        }
    }
    
    protected void clearDataSets() throws Exception {
    	try {
            importer.setFileDataSets(dataSets);
			importer.deleteAll();
		} catch (Exception e) {
			LOG.debug(e.getMessage(), e);
			throw e;
		}
    }
    
}
