package org.jboss.soa.qa.governance;

import org.junit.Before;
import org.junit.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;

/**
 * Test to verify deployments of xml,xsd,wsdl,jar files to S-RAMP.
 *
 * @author sbunciak
 *
 */
public class ArtifactImportTest extends ConsoleTestBase {

	@Before
	public void setUp() {
		super.setUp();
		// navigate to s-ramp-ui
		driver.get(S_RAMP_SERVER_UI);
		// jump to artifacts
		driver.findElement(By.partialLinkText("Artifacts")).click();
	}

	@Test
	public void shoudlImportXmlArtifact() {
		// open & submit import dialog
		submitImportForm(new File("src/test/resources/sampleDocument.xml"));

		// query
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.xpath(".//span[contains(text(), 'import has completed successfully')]")));
	}

	@Test
	public void shouldImportWsdlArtifact() {
		// open & submit import dialog
		submitImportForm(new File("src/test/resources/sampleService.wsdl"));

		// query
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.xpath(".//span[contains(text(), 'import has completed successfully')]")));
	}

	@Test
	public void shouldImportXsdFile() {
		// open & submit import dialog
		submitImportForm(new File("src/test/resources/sampleSchema.xsd"));
		// query
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.xpath(".//span[contains(text(), 'import has completed successfully')]")));
	}

	@Test
	public void shouldImportJarFile() {
		// open & submit import dialog
		submitImportForm(createDeploymentFile());

		// query
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.xpath(".//span[contains(text(), 'import has completed successfully')]")));
	}

	private void submitImportForm(File file) {
		// import xml file
		wait.until(ExpectedConditions.elementToBeClickable(By.id("btn-import"))).click();

		// wait for the modal dialog to fade-in
		dummyWait(2);

		// === hack for file input and submitting form ===
		final JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("$('span.btn-file > input:file').appendTo( $( 'input:file' ).parents('div.fileupload') );");
		js.executeScript("$('div.fileupload span').remove();");

		// fill in the file input
		driver.findElement(By.cssSelector("input[type='file']")).sendKeys(file.getAbsolutePath());
		js.executeScript("$('input:file').attr('name', 'artifact-file');");

		// submit dialog
		wait.until(
				ExpectedConditions.elementToBeClickable(By
						.cssSelector("button[data-field=import-dialog-submit-button]"))).click();
		// ===============================================
	}
}
