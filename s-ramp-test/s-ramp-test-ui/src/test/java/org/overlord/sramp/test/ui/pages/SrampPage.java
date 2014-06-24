package org.overlord.sramp.test.ui.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.overlord.commons.test.ui.pages.OverlordPage;

public abstract class SrampPage extends OverlordPage {

    @FindBy(partialLinkText = "Artifacts")
    private WebElement artifactsLink;

    public SrampPage() {
        super();

    }

    public void goToArtifacts() {
        artifactsLink.click();
    }

}
