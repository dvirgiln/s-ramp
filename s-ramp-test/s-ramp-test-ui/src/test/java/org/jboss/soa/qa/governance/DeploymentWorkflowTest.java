package org.jboss.soa.qa.governance;

import org.jboss.arquillian.junit.InSequence;

import org.junit.Before;
import org.junit.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;

/**
 * Test which creates new (SwitchYard) deployment and accomplishes Test Dev task.
 *
 * @author sbunciak
 *
 */
public class DeploymentWorkflowTest extends ConsoleTestBase {

	/*
	 * =========================================================================
	 * Just a top-level navigation for DTGov part. Governance workflows are
	 * being tested in separate DTGov (quickstart) tests.
	 * =========================================================================
	 */

	@Before
	public void setUp() {
		super.setUp();

		// navigate to DTGov
		driver.get(DT_GOV_SERVER_UI);
	}

	@Test
	@InSequence(1)
	public void shouleCreateNewDeploymentTest() {
		// navigate to list of deployments
		driver.findElement(By.partialLinkText("Deployments")).click();

		// create new deployment
		submitDeploymentForm(createDeploymentFile());

		// deployment created?
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.xpath(".//span[contains(text(), 'deployment has completed successfully')]")));

		// click to refresh results
		driver.findElement(By.cssSelector("button[data-field='btn-refresh']")).click();

		// wait to see if it was added to deployments table
		wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("switchyard-test-app")));
	}

	@Test
	@InSequence(2)
	public void shouldQueryForSingleDeployment() {
		// navigate to task inbox
		driver.findElement(By.partialLinkText("Deployments")).click();

		// query for the deployment by name
		driver.findElement(By.cssSelector("input[data-field='deployment-search-box']")).sendKeys(
				"switchyard-test-app.jar");

		// click to refresh results
		driver.findElement(By.cssSelector("button[data-field='btn-refresh']")).click();

		// wait to see if it was added to deployments table
		wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("switchyard-test-app")));
	}

	@Test
	@InSequence(3)
	public void shouldApproveDeploymentTaskTest() {
		// open task inbox
		driver.findElement(By.partialLinkText("Task Inbox")).click();

		// wait for Test Dev task and open the details
		wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Test Dev"))).click();

		// claim
		wait.until(ExpectedConditions.elementToBeClickable(By
				.xpath(".//button[contains(text(), 'Claim')]"))).click();

		// start
		wait.until(ExpectedConditions.elementToBeClickable(By
				.xpath(".//button[contains(text(), 'Start')]"))).click();

		// pass
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[value='pass'][type='radio']")))
				.click();

		// submit form
		wait.until(ExpectedConditions.elementToBeClickable(By
				.xpath(".//button[contains(text(), 'Complete')]"))).click();

		// task completed?
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.xpath(".//span[contains(text(), 'You have successfully completed task')]")));
	}

	/*
	 * Submits new deployment form.
	 */
	private void submitDeploymentForm(File file) {
		// import xml file
		wait.until(ExpectedConditions.elementToBeClickable(By
				.xpath(".//button[contains(text(), 'Add Deployment')]"))).click();

		// wait for the modal dialog to fade-in
		dummyWait(2);

		// === hack for file input and submitting form ===
		final JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("$('span.btn-file > input:file').appendTo( $( 'input:file' ).parents('div.fileupload') );");
		js.executeScript("$('div.fileupload span').remove();");

		// fill in the file input
		driver.findElement(By.cssSelector("input[type='file']")).sendKeys(file.getAbsolutePath());
		js.executeScript("$('input:file').attr('name', 'deployment-file');");

		// submit dialog
		driver.findElement(By.cssSelector("button[data-field=add-deployment-dialog-submit-button]")).click();
		// ===============================================
	}
}
