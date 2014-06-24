package org.overlord.sramp.test.ui;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ArchiveImportException;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.overlord.commons.test.ui.AbstractOverlordTestSuite;
import org.overlord.commons.test.ui.SuiteConstants;
import org.overlord.commons.test.ui.SuiteProperties;
import org.overlord.sramp.test.ui.pages.ArtifactsPage;
import org.overlord.sramp.test.ui.pages.HomePage;

public class ArtifactsTestSuite extends AbstractOverlordTestSuite {
    public static final String FILE_NAME = "sramp.properties";


    @Deployment(name = "server", order = 1)
    public static WebArchive deploySrampServer() throws IllegalArgumentException, ArchiveImportException, ConfigurationException {
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File((String) SuiteProperties.getProperty(SuiteConstants.S_RAMP_SERVER_WAR_PATH)));
    }

    @Deployment(name = "ui", order = 2)
    public static WebArchive deploySrampUi() throws IllegalArgumentException, ArchiveImportException, ConfigurationException {
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File((String) SuiteProperties.getProperty(SuiteConstants.S_RAMP_UI_WAR_PATH)));
    }

    public ArtifactsTestSuite() {
        try {
            SuiteProperties.initialize(FILE_NAME);
        } catch (ConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Test
    @InSequence(2)
    @OperateOnDeployment("ui")
    public void homePage(@InitialPage HomePage homePage) throws Exception {
        homePage.goToArtifacts();
    }

    @Test
    @InSequence(3)
    @OperateOnDeployment("ui")
    public void shoudlImportXmlArtifact(@InitialPage ArtifactsPage artifacts) {
        // open & submit import dialog
        artifacts.submitImportForm(new File("src/test/resources/sampleDocument.xml"));

        // query
        Graphene.waitModel().until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(".//span[contains(text(), 'import has completed successfully')]")));

    }
}
