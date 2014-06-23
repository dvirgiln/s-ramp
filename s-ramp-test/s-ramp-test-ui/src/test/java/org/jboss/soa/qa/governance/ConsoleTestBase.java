package org.jboss.soa.qa.governance;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.oasis_open.docs.s_ramp.ns.s_ramp_v1.BaseArtifactType;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.overlord.sramp.client.SrampAtomApiClient;
import org.overlord.sramp.common.ArtifactType;

/**
 * Base class for S-RAMP console tests.
 *
 * <p>
 * SOA-GOV-2
 * </p>
 * =============================================================================
 * <p>
 * Web Browser
 * </p>
 *
 * <p>
 * Write a set of UI tests (Selenium) to verify
 * </p>
 *
 * Artifact import <li>XMSL, WSDL and JAR files will be deployed</li> <li>
 * Derived artifacts are created</li> <li>a BPMN2 workflow is started</li> <li>
 * Expected properties and relationships are created</li>
 *
 * Querying <li>Create a query that lists a single artifact</li> <li>Create a
 * query that lists multiple artifacts</li> <li>Create a query using a property
 * value</li> <li>Create a query using classification function</li> <li>Create a
 * query using regex function</li>
 *
 * <p>
 * See S-RAMP Specification Foundation for details
 * </p>
 *
 *
 * @author sbunciak
 *
 */
@RunWith(Arquillian.class)
public class ConsoleTestBase {

    protected static final String GOV_USER = System.getProperty("test_username", "admin");
    protected static final String GOV_PWD = System.getProperty("test_password", "123admin!");

	protected static final String SERVER_URL = System.getProperty("test_serverUrl", "http://localhost:8080");

	protected static final String S_RAMP_SERVER_UI = SERVER_URL + "/s-ramp-ui/";
	protected static final String DT_GOV_SERVER_UI = SERVER_URL + "/dtgov-ui/";

	@Drone
	protected WebDriver driver;
	protected WebDriverWait wait;
	private SrampAtomApiClient srampClient;

	protected final Logger log = Logger.getLogger(ConsoleTestBase.class);

	@Before
	public void setUp() {
		try {
			srampClient = new SrampAtomApiClient(SERVER_URL + "/s-ramp-server", GOV_USER, GOV_PWD, true);
		} catch (Exception e) {
			log.error("Failed to connect to S-RAMP endpoint: ", e);
		}

		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, 60);

		this.login();
	}

	@After
	public void tearDown() {
		this.logout();
	}

	private void login() {
		// log in
		driver.get(S_RAMP_SERVER_UI);
		driver.findElement(By.cssSelector("input[name='j_username']")).sendKeys(GOV_USER);
		driver.findElement(By.cssSelector("input[name='j_password']")).sendKeys(GOV_PWD);
		driver.findElement(By.cssSelector("input[type='submit']")).click();

		// are we at the desired location?
		assertTrue(driver.getTitle().contains("S-RAMP"));
	}

	private void logout() {
		driver.findElement(By.cssSelector("div.overlord-nav-user a")).click();
		driver.findElement(By.partialLinkText("Logout")).click();

		dummyWait(2);

		assertTrue(driver.getTitle().contains("Login"));
	}

	/*
	 * Webdriver hack for wait.
	 */
	protected void dummyWait(int sec) {
		try {
			new WebDriverWait(driver, sec).until(ExpectedConditions.visibilityOfElementLocated(By
					.className("dummy-wait")));
		} catch (TimeoutException ex) {
			log.debug("=== Dummy wait ===");
		}
	}

	/*
	 * Uploads artifact using S-RAMP Atom API client.
	 */
	protected BaseArtifactType uploadArtifact(ArtifactType type, File artifact) {
		try {
			return srampClient.uploadArtifact(type, new FileInputStream(artifact), artifact.getName());
		} catch (FileNotFoundException e) {
			log.error("Artifact file not found: ", e);
        } catch (Exception e) {
			log.error("Failed to upload file: ", e);
		}

		return null;
	}

	/*
	 * Creates new SwitchYard application jar in /tmp dir.
	 */
	protected File createDeploymentFile() {
		// create shrinkwrap archive
		final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "switchyard-test-app.jar")
				.addAsManifestResource("switchyard.xml", "switchyard.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

		// export it to /tmp
		final File switchyardApp = new File(System.getProperty("java.io.tmpdir") + "/" + archive.getName());
		archive.as(ZipExporter.class).exportTo(switchyardApp, true);

		return switchyardApp;
	}
}
