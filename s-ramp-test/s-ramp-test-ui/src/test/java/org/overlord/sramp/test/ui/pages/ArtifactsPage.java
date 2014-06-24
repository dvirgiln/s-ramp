package org.overlord.sramp.test.ui.pages;

import java.io.File;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

@Location("#artifacts")
public class ArtifactsPage extends SrampPage {

    public ArtifactsPage() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void submitImportForm(File file) {
        // import xml file

        Graphene.waitGui().until(ExpectedConditions.elementToBeClickable(By.id("btn-import"))).click();

        // wait for the modal dialog to fade-in
        Graphene.waitAjax();

        // === hack for file input and submitting form ===
        final JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("$('span.btn-file > input:file').appendTo( $( 'input:file' ).parents('div.fileupload') );");
        js.executeScript("$('div.fileupload span').remove();");

        // fill in the file input
        driver.findElement(By.cssSelector("input[type='file']")).sendKeys(file.getAbsolutePath());
        js.executeScript("$('input:file').attr('name', 'artifact-file');");

        // submit dialog
        WebElement submitButton = Graphene.waitGui().until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-field=import-dialog-submit-button]")));
        submitButton.click();
    }
}
