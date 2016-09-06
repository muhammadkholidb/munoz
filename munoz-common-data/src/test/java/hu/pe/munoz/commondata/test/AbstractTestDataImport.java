package hu.pe.munoz.commondata.test;

import hu.pe.munoz.commondata.helper.DataImporter;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author eatonmunoz
 */
public class AbstractTestDataImport {

    private final static Logger LOG = LoggerFactory.getLogger(AbstractTestDataImport.class);
    
    @Autowired
    protected DataImporter importer;
    
    /**
     * Process the dataset XML file.
     * @param paths Dataset path relative to directory /src/test/resources
     * @throws java.lang.Exception
     */
    protected void processDataSet(String... paths) throws Exception {
        List<File> datasets = new ArrayList<>();
        for (String path : paths) {
            URL url = AbstractTestDataImport.class.getClassLoader().getResource(path);
            File file = new File(url.getFile());
            datasets.add(file);
            LOG.debug("Dataset file: " + file.getAbsolutePath());
        }
        try {
            importer.setFileDataSets(datasets);
            importer.process();
        } catch (Exception e) {
            LOG.error(e.toString(), e);
            throw e;
        }
    }
    
}
