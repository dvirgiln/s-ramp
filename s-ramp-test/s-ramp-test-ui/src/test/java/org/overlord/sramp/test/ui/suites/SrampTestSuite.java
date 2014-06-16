package org.overlord.sramp.test.ui.suites;

import org.apache.commons.configuration.ConfigurationException;
import org.overlord.commons.test.ui.AbstractTestSuite;
import org.overlord.commons.test.ui.SuiteProperties;
import org.testng.annotations.BeforeSuite;

public class SrampTestSuite extends AbstractTestSuite {

    public static final String FILE_NAME = "sramp.properties";

    @BeforeSuite
    public static void initializeSuite() throws ConfigurationException {
        SuiteProperties.initialize(FILE_NAME);
    }

    /*
     * @Test public void initialize() throws ConfigurationException {
     * SuiteProperties.initialize(FILE_NAME); }
     */

}
