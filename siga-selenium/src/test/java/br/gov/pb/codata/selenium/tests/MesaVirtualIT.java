package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocMesaVirtualPage;
import br.gov.pb.codata.selenium.util.validator.Validator;

public class MesaVirtualIT extends DriverBase {

	public Validator validator = new Validator();
	
	@Test
	public void menuPaginaInicial() throws Exception {
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.abrirPaginaInicial();
		WebDriverWait wait = new WebDriverWait(getDriver(), 1, 100);
		wait.until(validator.pageTitleStartsWith("PBDoc - Mesa Virtual"));
	}
	
	@Test
	public void menuManualPbdoc() throws Exception {
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.abrirManualPbdoc();
		validator.switchTabs(getDriver(), 2, 1);
		WebDriverWait wait = new WebDriverWait(getDriver(), 1, 100);
		wait.until(validator.pageTitleStartsWith("Manual on-line PBdoc — PBDoc - Sistema de Protocolo da Paraíba"));
		validator.switchTabs(getDriver(), 2, 0);
		validator.closeAllTabsExcepCurrent();
	}
	
	@Test
	public void menuVersoesAtualizacoes() throws Exception {
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.abrirVersoesAtualizacoes();
		validator.switchTabs(getDriver(), 2, 1);
		WebDriverWait wait = new WebDriverWait(getDriver(), 1, 100);
		wait.until(validator.pageTitleStartsWith("Mural de avisos — PBDoc - Sistema de Protocolo da Paraíba"));
		validator.switchTabs(getDriver(), 2, 0);
		validator.closeAllTabsExcepCurrent();
	}
	
	@Test
	public void menuLogoff() throws Exception {
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.logoff();
		WebDriverWait wait = new WebDriverWait(getDriver(), 1, 100);
		wait.until(validator.pageTitleStartsWith("PBdoc - Página de Login"));
	}
	
	//@Test
	public void configuracoes() throws Exception {
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clickConfigButton();
	}
	
	//@Test
	public void exibirViasCanceladas() throws Exception {
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clickConfigButton();
		mesaVirtualPage.checkViasCanceladas();
	}
	
	//@Test
	public void exibirOrdemCrescente() throws Exception {
		PBDocSeleniumController.start();
		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clickConfigButton();
		mesaVirtualPage.checkOrdemCrescente();
	}
	
}
