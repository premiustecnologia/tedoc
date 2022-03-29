package br.gov.pb.codata.selenium.page_objects;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;

import org.openqa.selenium.By;

import com.lazerycode.selenium.util.Query;

import br.gov.pb.codata.selenium.DriverBase;

public class SigadocMesaVirtualPage extends DriverBase {

	private final Query criarNovo = new Query().defaultLocator(By.linkText("Criar Novo"));
	private final Query menuPrincipal = new Query().defaultLocator(By.linkText("MENU"));
	private final Query paginaInicial = new Query().defaultLocator(By.linkText("Página Inicial"));
	private final Query manualPbdoc = new Query().defaultLocator(By.linkText("Manual PBdoc"));
	private final Query versoesAtualizacoes = new Query().defaultLocator(By.linkText("Versões e Atualizações"));
	private final Query logoff = new Query().defaultLocator(By.linkText("Logoff"));
	private final Query quadrosQuantitativos = new Query().defaultLocator(By.linkText("Quadros Quantitativos"));
	private final Query gestaoDeIdentidade = new Query().defaultLocator(By.linkText("Gestão de Identidade"));
	private final Query cadastroDePessoa = new Query().defaultLocator(By.linkText("Cadastro de Pessoa"));
	private final Query configButton = new Query().defaultLocator(By.xpath("//*[@id=\"configMenu\"]/button"));
	private final Query viasCanceladasCheck = new Query().defaultLocator(By.id("trazerCancelados"));
	private final Query ordemCrescenteCheck = new Query().defaultLocator(By.id("ordemCrescenteData"));

	public SigadocMesaVirtualPage() throws Exception {
		initQueryObjects(this, DriverBase.getDriver());
	}

	public SigadocMesaVirtualPage clicarCriarNovo() {
		criarNovo.findWebElement().click();
		return this;
	}

	public SigadocMesaVirtualPage logoff() {
		menuPrincipal.findWebElement().click();
		logoff.findWebElement().click();
		return this;
	}

	public SigadocMesaVirtualPage abrirVersoesAtualizacoes() {
		menuPrincipal.findWebElement().click();
		versoesAtualizacoes.findWebElement().click();
		return this;
	}

	public SigadocMesaVirtualPage abrirManualPbdoc() {
		menuPrincipal.findWebElement().click();
		manualPbdoc.findWebElement().click();
		return this;
	}

	public SigadocMesaVirtualPage abrirPaginaInicial() {
		menuPrincipal.findWebElement().click();
		paginaInicial.findWebElement().click();
		return this;
	}

	public SigadocMesaVirtualPage abrirQuadrosQuantitativos() {
		menuPrincipal.findWebElement().click();
		quadrosQuantitativos.findWebElement().click();

		return this;
	}

	public SigadocMesaVirtualPage abrirCadastroDePessoa() {
		menuPrincipal.findWebElement().click();
		gestaoDeIdentidade.findWebElement().click();
		cadastroDePessoa.findWebElement().click();

		return this;
	}

	public SigadocMesaVirtualPage clickConfigButton() {
		configButton.findWebElement().click();
		return this;
	}

	public SigadocMesaVirtualPage checkViasCanceladas() throws Exception {
		viasCanceladasCheck.findWebElement().click();
		return this;
	}

	public SigadocMesaVirtualPage checkOrdemCrescente() throws Exception {
		ordemCrescenteCheck.findWebElement().click();
		return this;
	}

}