package br.gov.pb.codata.selenium.page_objects;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.util.text.Dictionary;
import com.lazerycode.selenium.util.Query;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.Iterator;
import java.util.Set;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;

/**
*
* @author Thomas Ribeiro
*/
public class PBDocTramitDocumentPage {
	private final Query dropdownAccess = new Query().defaultLocator(By.name("tipoResponsavel"));
	private final Query inputReceiver = new Query().defaultLocator(By.name("lotaResponsavelSel.sigla"));
	private final Query buttonSubmit = new Query().defaultLocator(By.id("button_ok"));

	public PBDocTramitDocumentPage() throws Exception {
		initQueryObjects(this, DriverBase.getDriver());
	}

	public PBDocTramitDocumentPage selectAccess(String access) {
		Select select = new Select(dropdownAccess.findWebElement());
		select.selectByVisibleText(access);
		return this;
	}

	public PBDocTramitDocumentPage informReceiver(String receiver) {
		inputReceiver.findWebElement().sendKeys(receiver);
		return this;
	}

	public PBDocTramitDocumentPage submitDocument() {
		buttonSubmit.findWebElement().click();
		return this;
	}

}
