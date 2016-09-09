package hu.pe.munoz.commondata.test.helper;

import hu.pe.munoz.commondata.helper.DataImporter;
import java.io.File;
import java.net.URL;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ContextConfiguration(locations = "classpath:munoz-common-data-context-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestDataImporter {

    private static final Logger LOG = LoggerFactory.getLogger(TestDataImporter.class);
    
    @Autowired
    private DataImporter importer;
    
    @Test
    public void testImport() {
        LOG.debug("Test import ...");
        // Read: http://jlorenzen.blogspot.co.id/2007/06/proper-way-to-access-file-resources-in.html
        URL url1 = this.getClass().getResource("/dataset/test-importer-system.dataset.xml");
        URL url2 = this.getClass().getResource("/dataset/test-importer-user-group.dataset.xml");
        try {
            importer.addFileDataSet(new File(url1.getFile()));
            importer.addFileDataSet(new File(url2.getFile()));
            importer.importAll();
        } catch (Exception e) {
            LOG.error(e.toString(), e);
        }
    }

}
