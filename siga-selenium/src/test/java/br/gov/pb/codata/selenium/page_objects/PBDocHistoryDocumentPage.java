package br.gov.pb.codata.selenium.page_objects;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;

import org.openqa.selenium.By;

import com.lazerycode.selenium.util.Query;

import br.gov.pb.codata.selenium.DriverBase;

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
