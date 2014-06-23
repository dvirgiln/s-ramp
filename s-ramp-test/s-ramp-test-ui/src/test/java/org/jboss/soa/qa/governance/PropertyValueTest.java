package org.jboss.soa.qa.governance;

import org.jboss.arquillian.junit.InSequence;

import org.junit.Before;
import org.junit.Test;

import org.oasis_open.docs.s_ramp.ns.s_ramp_v1.BaseArtifactType;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.overlord.sramp.common.ArtifactType;

import java.io.File;

/**
 * Test which assignes a custom property to xml artifact, subsequently queries for this property value.
 *
 * @author sbunciak
 *
 */
public class PropertyValueTest extends ConsoleTestBase {

	@Before
	public void setUp() {
		super.setUp();
		// navigate to s-ramp-ui
		driver.get(S_RAMP_SERVER_UI);
		// jump to artifacts
		driver.findElement(By.partialLinkText("Artifacts")).click();
	}

	@Test
	@InSequence(1)
	public void shouldSetCustomProperty() {

		final BaseArtifactType uploadedXml = uploadArtifact(ArtifactType.XmlDocument(), new File(
				"src/test/resources/sampleDocument.xml"));

		// == search for uploaded xml
		driver.findElement(By.id("sramp-search-box")).clear();
		driver.findElement(By.id("sramp-search-box")).sendKeys(
				"/s-ramp/core/XmlDocument[@uuid='" + uploadedXml.getUuid() + "']");
		// click to refresh results
		driver.findElement(By.cssSelector("button[data-field='btn-refresh']")).click();
		// search for uploaded xml
		wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("sampleDocument.xml"))).click();

		// wait for artifact details
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-field='add-property-button']")));
		driver.findElement(By.cssSelector("button[data-field='add-property-button']")).click();

		driver.findElement(By.cssSelector("input[data-field='name']")).sendKeys("from-soa-tests");

		driver.findElement(By.cssSelector("textarea[data-field='value']")).sendKeys("browser-test");

		driver.findElement(By.cssSelector("button[data-field=add-property-submit-button]")).click();
		// query
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.xpath(".//span[contains(text(), 'You have successfully updated artifact')]")));
		// Importing Artifact(s) [Complete]
	}

	@Test
	@InSequence(2)
	public void shouldQueryForPropertyValueUsingSearchbox() {
		// set query
		driver.findElement(By.id("sramp-search-box")).clear();
		driver.findElement(By.id("sramp-search-box")).sendKeys(
				"/s-ramp/core/XmlDocument[@from-soa-tests='browser-test']");
		// click to refresh results
		driver.findElement(By.cssSelector("button[data-field='btn-refresh']")).click();

		// artifact found?
		wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("sampleDocument.xml")));
	}

	@Test
	@InSequence(3)
	public void shouldQueryForPropertyValueUsingForm() {
		// open the properties filter
		driver.findElement(By.partialLinkText("Custom Properties")).click();
		// open popup for property name
		driver.findElement(By.cssSelector("button[data-field='addCustomPropertyFilter']")).click();
		// set property name
		driver.findElement(By.cssSelector("input[data-field='name']")).sendKeys("from-soa-tests");
		// confirm
		driver.findElement(By.cssSelector("button[data-field='submit-button']")).click();
		// set property value [until now the filter worked for any value of
		// "from-soa-tests" property ]
		driver.findElement(By.cssSelector("input[data-field='filter-custom-property-input']")).sendKeys("browser-test");

		// click to refresh results
		driver.findElement(By.cssSelector("button[data-field='btn-refresh']")).click();

		// Artifact found?
		wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("sampleDocument.xml")));
	}
}
