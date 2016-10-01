package hu.pe.munoz.common.test.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.pe.munoz.common.helper.CommonUtils;
import hu.pe.munoz.common.helper.PasswordUtils;

public class TestPasswordUtils {

    private static final Logger LOG = LoggerFactory.getLogger(TestPasswordUtils.class);
    
    @Test
    public void testStirWithSalt() {
        LOG.debug("Test stir with salt ...");
        String password = "12345678";
        String salt = CommonUtils.getRandomAlphanumeric(32);
        LOG.debug("Password: " + password);
        LOG.debug("Salt: " + salt);
        String result = PasswordUtils.stirWithSalt(password, salt);
        LOG.debug("Result: " + result);
        assertEquals(PasswordUtils.DEFAULT_LEGTH, result.length());
    }

    @Test
    public void testStirWithoutSalt() {
        LOG.debug("Test stir without salt ...");
        String password = "pwd!@#A";
        LOG.debug("Password: " + password);
        String result = PasswordUtils.stir(password);
        LOG.debug("Result: " + result);
        assertEquals(PasswordUtils.DEFAULT_LEGTH, result.length());
    }
    
}
