package org.overlord.sramp.test.ui;

import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Before;
import org.junit.Test;
import org.overlord.sramp.test.ui.pages.ArtifactsPage;

/**
 * Test to verify deployments of xml,xsd,wsdl,jar files to S-RAMP.
 *
 * @author sbunciak
 *
 */
public class ArtifactsTestSuite extends SrampTestSuite {
    @Page
    private ArtifactsPage artifactsPage;

    @Before
    public void initialize() throws Exception {
        super.login();
        /*
         * if (artifactsPage == null) { artifactsPage =
         * PageFactory.initElements(driver, ArtifactsPage.class); }
         */
    }

	@Test
    @InSequence(1)
    public void shoudlImportXmlArtifact() throws Exception {

        System.out.println("");
		// open & submit import dialog
        // submitImportForm(new File("src/test/resources/sampleDocument.xml"));

		// query
        // wait.until(ExpectedConditions.visibilityOfElementLocated(By
        // .xpath(".//span[contains(text(), 'import has completed successfully')]")));
	}



}
