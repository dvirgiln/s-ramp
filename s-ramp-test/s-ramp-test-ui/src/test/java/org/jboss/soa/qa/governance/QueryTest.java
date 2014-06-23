package org.jboss.soa.qa.governance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.oasis_open.docs.s_ramp.ns.s_ramp_v1.BaseArtifactType;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.overlord.sramp.common.ArtifactType;

import java.io.File;

/**
 * Test for various types of queries.
 *
 * @author sbunciak
 *
 */
public class QueryTest extends ConsoleTestBase {

	private BaseArtifactType wsdlDocument;

	@Before
	public void setUp() {
		super.setUp();
		// Upload test data
		wsdlDocument = uploadArtifact(ArtifactType.WsdlDocument(), new File("src/test/resources/sampleService.wsdl"));

		// move to artifacts
		driver.get(S_RAMP_SERVER_UI);
		driver.findElement(By.partialLinkText("Artifacts")).click();
	}

	@Test
	public void shouldQueryNoArtifact() {
		// set query
		driver.findElement(By.id("sramp-search-box")).clear();
		driver.findElement(By.id("sramp-search-box")).sendKeys(
				"/s-ramp/core/XmlDocument[@uuid='111111111111111-fakeuuid']");
		// click to refresh results
		driver.findElement(By.cssSelector("button[data-field='btn-refresh']")).click();

		dummyWait(3);

		// check number of artifacts
		assertEquals(0, driver.findElements(By.xpath("//table[@data-field='sramp-artifacts-table']/tbody/tr")).size());
	}

	@Test
	public void shouldQuerySingleArtifact() {
		// set query
		driver.findElement(By.id("sramp-search-box")).clear();
		driver.findElement(By.id("sramp-search-box")).sendKeys(
				"/s-ramp/wsdl/WsdlDocument[@uuid='" + wsdlDocument.getUuid() + "']");
		// click to refresh results
		driver.findElement(By.cssSelector("button[data-field='btn-refresh']")).click();

		// Artifact exists?
		wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("sampleService.wsdl")));
		// check number of artifacts
		assertEquals(1, driver.findElements(By.xpath("//table[@data-field='sramp-artifacts-table']/tbody/tr")).size());
	}

	@Test
	public void shouldQueryMultipleArtifacts() {
		// set query
		driver.findElement(By.id("sramp-search-box")).clear();
		driver.findElement(By.id("sramp-search-box")).sendKeys(
				"/s-ramp/xsd[relatedDocument[@uuid='" + wsdlDocument.getUuid() + "']]");
		// click to refresh results
		driver.findElement(By.cssSelector("button[data-field='btn-refresh']")).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("work")));

		// check number of artifacts
		assertEquals(8, driver.findElements(By.xpath("//table[@data-field='sramp-artifacts-table']/tbody/tr")).size());
	}

	@Test
	public void shouldQueryUsingRegexFunction() {
		// set query
		driver.findElement(By.id("sramp-search-box")).clear();
		driver.findElement(By.id("sramp-search-box")).sendKeys(
				"/s-ramp/wsdl/WsdlDocument[xp2:matches(@name, 'sampleService.*')]");
		// click to refresh results
		driver.findElement(By.cssSelector("button[data-field='btn-refresh']")).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("sampleService")));

		// check number of artifacts
		assertTrue(driver.findElements(By.xpath("//table[@data-field='sramp-artifacts-table']/tbody/tr")).size() > 0);
	}
}
