package br.gov.pb.codata.selenium.tests;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.PBDocHistoryDocumentPage;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
*
* @author Thomas Ribeiro
* Classe para controle de fluxo de anexar um arquivo ao documento criado.
*/
public class HistoryDocumentIt extends DriverBase {

	public void auditDocument() throws Exception {
		PBDocHistoryDocumentPage historyDocumentPage = new PBDocHistoryDocumentPage();
		historyDocumentPage.audit();
		PBDocSeleniumController.checkNoError("HistoryDocumentIt.auditDocument");
	}
}
