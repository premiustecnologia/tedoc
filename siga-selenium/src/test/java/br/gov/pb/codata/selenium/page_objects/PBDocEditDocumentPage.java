package br.gov.pb.codata.selenium.page_objects;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.lazerycode.selenium.util.Query;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.tests.AttachDocumentIt;
import br.gov.pb.codata.selenium.tests.HistoryDocumentIt;
import br.gov.pb.codata.selenium.tests.SignDocumentIT;
import br.gov.pb.codata.selenium.tests.TramitDocumentIt;
import br.gov.pb.codata.selenium.tests.exceptions.PBDocGenericError;
import br.gov.pb.codata.selenium.util.text.Dictionary;

public class PBDocEditDocumentPage extends DriverBase{

	private final Query linkSignDocument = new Query().defaultLocator(By.xpath("//*[@id='assinar']"));
	private final Query linkFinishDocument = new Query().defaultLocator(By.xpath("//*[@id='finalizar']"));
	private final Query linkAttachDocument = new Query().defaultLocator(By.xpath("//*[@id='anexar']"));
	private final Query linkHistoryDocument = new Query().defaultLocator(By.xpath("//*[@id='hist-oacute-rico']"));
	private final Query linkTramitDocument = new Query().defaultLocator(By.xpath("//*[@id='tramitar']"));
	private final Query linkExcludeDocument = new Query().defaultLocator(By.xpath("//*[@id='excluir']"));

	public PBDocEditDocumentPage() throws Exception {
		initQueryObjects(this, DriverBase.getDriver());
	}

	public void doAction(String action) throws PBDocGenericError, Exception {
		switch (action) {
		case Dictionary.ASSINAR:
			linkSignDocument.findWebElement().click();
			PBDocSeleniumController.checkNoError("EditDocumentIt.edit:" + action);
			SignDocumentIT signDocumentIT = new SignDocumentIT();
			signDocumentIT.signDocument();
			break;
		case Dictionary.FINALIZAR:
			linkFinishDocument.findWebElement().click();
			PBDocSeleniumController.checkNoError("EditDocumentIt.edit:" + action);
			break;
		case Dictionary.ANEXAR:
			linkAttachDocument.findWebElement().click();
			PBDocSeleniumController.checkNoError("EditDocumentIt.edit:" + action);
			AttachDocumentIt attachDocumentIt = new AttachDocumentIt();
			attachDocumentIt.attachDocument();
			break;
		case Dictionary.AUDITAR:
			linkHistoryDocument.findWebElement().click();
			PBDocSeleniumController.checkNoError("EditDocumentIt.edit:" + action);
			HistoryDocumentIt historyDocumentIt = new HistoryDocumentIt();
			historyDocumentIt.auditDocument();
			break;
		case Dictionary.TRAMITAR:
			linkTramitDocument.findWebElement().click();
			PBDocSeleniumController.checkNoError("EditDocumentIt.edit:" + action);
			TramitDocumentIt tramitDocumentIt = new TramitDocumentIt();
			tramitDocumentIt.tramitDocument();
			break;
		case Dictionary.EXCLUIR:
			try {
				linkExcludeDocument.findWebElement().click();
				PBDocSeleniumController.checkNoError("EditDocumentIt.edit:" + action);
			} catch (UnhandledAlertException f) {
			    try {
			    	WebDriverWait wait = new WebDriverWait(getDriver(), 5, 100);
			    	wait.wait(100);
			    	Alert alert = getDriver().switchTo().alert();
			        String alertText = alert.getText();
			        System.out.println("Alert data: " + alertText);
			        alert.accept();
			    } catch (NoAlertPresentException e) {
			        e.printStackTrace();
			    }
			}
			break;
		default:
			break;
		}
	}
}
