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
public class PBDocHistoryDocumentPage {

	private final Query auditLink = new Query().defaultLocator(By.xpath("//*[@id='auditar']"));

	public PBDocHistoryDocumentPage() throws Exception {
		initQueryObjects(this, DriverBase.getDriver());
	}

	public PBDocHistoryDocumentPage audit() {
		auditLink.findWebElement().click();
		return this;
	}

}
