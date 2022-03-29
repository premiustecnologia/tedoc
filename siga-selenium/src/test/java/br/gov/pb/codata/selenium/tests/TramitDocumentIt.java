package br.gov.pb.codata.selenium.tests;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.PBDocTramitDocumentPage;
import br.gov.pb.codata.selenium.util.text.Dictionary;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
*
* @author Thomas Ribeiro
* Classe para controle de fluxo de anexar um arquivo ao documento criado.
*/
public class TramitDocumentIt extends DriverBase {

	private String usuario2 = System.getenv("USUARIO2");

	public void tramitDocument() throws Exception {
		PBDocTramitDocumentPage tramitDocumentPage = new PBDocTramitDocumentPage();
		tramitDocumentPage.selectAccess(Dictionary.CPF_USUARIO);
		tramitDocumentPage.informReceiver(usuario2);
		tramitDocumentPage.submitDocument();
		PBDocSeleniumController.checkNoError("HistoryDocumentIt.auditDocument");
	}
}
