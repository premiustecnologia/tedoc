package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.support.ui.WebDriverWait;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.PBDocHistoryDocumentPage;
import br.gov.pb.codata.selenium.tests.exceptions.PBDocGenericError;
import br.gov.pb.codata.selenium.util.validator.Validator;

public class HistoryDocumentIt extends DriverBase {

	private Validator validator = new Validator();
	
	public void auditDocument() throws Exception {
		PBDocHistoryDocumentPage historyDocumentPage = new PBDocHistoryDocumentPage();
		historyDocumentPage.audit();
		PBDocSeleniumController.checkNoError("HistoryDocumentIt.auditDocument");
		WebDriverWait wait = new WebDriverWait(getDriver(), 5, 100);
		if(!wait.until(validator.pageURLContains("exibirCompleto=true"))) {
			throw new PBDocGenericError("Página não coneguiu auditar em 5 segundos");
		};
	}
}
