package org.overlord.sramp.test.ui;

import org.apache.commons.configuration.ConfigurationException;
import org.overlord.commons.test.ui.AbstractOverlordTestSuite;
import org.overlord.commons.test.ui.SuiteProperties;

public class SrampTestSuite extends AbstractOverlordTestSuite {
    public static final String FILE_NAME = "sramp.properties";

    public SrampTestSuite() {
        try {
            SuiteProperties.initialize(FILE_NAME);
        } catch (ConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
