package br.gov.pb.codata.selenium.page_objects;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;

import org.openqa.selenium.By;
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
import br.gov.pb.codata.selenium.util.validator.Validator;

public class PBDocEditDocumentPage extends DriverBase {

	private Validator validator = new Validator();
	private final Query linkSignDocument = new Query().defaultLocator(By.xpath("//*[@id='assinar']"));
	private final Query linkFinishDocument = new Query().defaultLocator(By.xpath("//*[@id='finalizar']"));
	private final Query linkAttachDocument = new Query().defaultLocator(By.xpath("//*[@id='anexar']"));
	private final Query linkHistoryDocument = new Query().defaultLocator(By.xpath("//*[@id='hist-oacute-rico']"));
	private final Query linkTramitDocument = new Query().defaultLocator(By.xpath("//*[@id='tramitar']"));
	private final Query linkExcludeDocument = new Query().defaultLocator(By.xpath("//*[@id='excluir']"));
	private final Query linkDuplicateDocument = new Query().defaultLocator(By.xpath("//*[@id='duplicar']"));
	private final Query docTitleH2 = new Query().defaultLocator(By.xpath("//*[@id=\"page\"]/div[1]/div/h2"));

	public PBDocEditDocumentPage() throws Exception {
		initQueryObjects(this, DriverBase.getDriver());
	}

	public void doAction(String action) throws PBDocGenericError, Exception {
		WebDriverWait wait = new WebDriverWait(getDriver(), 5, 100);
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
			wait.until(validator.pageTitleStartsWith("PBdoc - Documento"));
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
			linkExcludeDocument.findWebElement().click();
			try {
				validator.acceptAlert();
			} catch (Exception e) {
				e.printStackTrace();
				throw new PBDocGenericError("Falha ao tentar excluir documento");
			}
			PBDocSeleniumController.checkNoError("EditDocumentIt.edit:" + action);
			break;
		case Dictionary.DUPLICAR:
			duplicar();
			break;
		default:
			break;
		}
	}
	
	private String getDocTitle() {
		return docTitleH2.findWebElement().getText();
	}
	
	private void duplicar() throws Exception {
		String docOrigial = getDocTitle();
		System.out.println("ORiginal: " + docOrigial);
		String docDuplicado = "TMP-" + (Integer.parseInt(docOrigial.substring(4)) + 1);
		System.out.println("ORiginal: " + docDuplicado);
		linkDuplicateDocument.findWebElement().click();
		try {
			validator.acceptAlert();
		} catch (Exception e) {
			e.printStackTrace();
			throw new PBDocGenericError("Falha ao tentar duplicar documento");
		}
		PBDocSeleniumController.checkNoError("EditDocumentIt.edit: Duplicar");
		if(!docDuplicado.equals(getDocTitle())) {
			throw new PBDocGenericError("Falha ao tentar duplicar documento. Titulos dos documentos fora do padrão");
		}
	}
}
