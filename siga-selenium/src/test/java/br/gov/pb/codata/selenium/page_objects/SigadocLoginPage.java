package br.gov.pb.codata.selenium.page_objects;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.lazerycode.selenium.util.Query;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.util.validator.Validator;

public class SigadocLoginPage {

	public Validator validator = new Validator();
	public static final String USERNAME = System.getenv("USUARIO");
	public static final String SENHA = System.getenv("SENHA");
	private final Query inputUsuario = new Query().defaultLocator(By.id("username"));
	private final Query inputSenha = new Query().defaultLocator(By.id("password"));
	private final Query buttonPrimeiroAcesso = new Query().defaultLocator(By.linkText("Primeiro acesso"));
	private final Query pbdocLink = new Query()
			.defaultLocator(By.xpath("/html/body/footer/div/div/div/div[1]/div[1]/a"));
	private final Query codataLink = new Query().defaultLocator(By.xpath("/html/body/footer/div/div/div/div[2]/a"));
	private final Query governoEstadoLink = new Query()
			.defaultLocator(By.xpath("/html/body/footer/div/div/div/div[3]/a"));
	private final Query sobrePbdocLink = new Query().defaultLocator(By.linkText("Sobre o PBdoc"));

	public SigadocLoginPage() throws Exception {
		initQueryObjects(this, DriverBase.getDriver());
	}

	public SigadocLoginPage digitarCredenciais(String username, String password) {
		inputUsuario.findWebElement().clear();
		inputUsuario.findWebElement().sendKeys(username);

		inputSenha.findWebElement().clear();
		inputSenha.findWebElement().sendKeys(password);

		return this;
	}

	public SigadocLoginPage logar() {
		inputUsuario.findWebElement().clear();
		inputUsuario.findWebElement().sendKeys(USERNAME);

		inputSenha.findWebElement().clear();
		inputSenha.findWebElement().sendKeys(SENHA);

		return this;
	}

	public SigadocLoginPage enviarAutenticacao() {
		inputSenha.findWebElement().submit();

		return this;
	}

	public SigadocLoginPage primeiroAcesso() {
		buttonPrimeiroAcesso.findWebElement().click();

		return this;
	}

	public SigadocLoginPage clickPbdocLink(WebDriver driver) throws Exception {
		pbdocLink.findWebElement().click();
		WebDriverWait wait = new WebDriverWait(driver, 5, 100);
		validator.switchTabs(driver, 2, 1);
		wait.until(validator.pageTitleStartsWith("Página Inicial — PBDoc - Sistema de Protocolo da Paraíba"));
		validator.switchTabs(driver, 2, 0);
		return this;
	}

	public SigadocLoginPage clickCodataLink(WebDriver driver) throws Exception {
		codataLink.findWebElement().click();
		WebDriverWait wait = new WebDriverWait(driver, 5, 100);
		validator.switchTabs(driver, 3, 2);
		wait.until(validator
				.pageTitleStartsWith("Página Inicial — CODATA - Companhia de Processamento de Dados da Paraíba"));
		validator.switchTabs(driver, 3, 0);
		return this;
	}

	public SigadocLoginPage clickGovernoEstadoLink(WebDriver driver) throws Exception {
		governoEstadoLink.findWebElement().click();
		WebDriverWait wait = new WebDriverWait(driver, 5, 100);
		validator.switchTabs(driver, 4, 3);
		wait.until(validator.pageTitleStartsWith("Página Inicial — Governo da Paraíba"));
		validator.switchTabs(driver, 4, 0);
		return this;
	}

	public SigadocLoginPage clickSobrePbdocLink(WebDriver driver) throws Exception {
		sobrePbdocLink.findWebElement().click();
		WebDriverWait wait = new WebDriverWait(driver, 5, 100);
		validator.switchTabs(driver, 5, 4);
		wait.until(validator.pageTitleStartsWith("Página Inicial — PBDoc - Sistema de Protocolo da Paraíba"));
		validator.switchTabs(driver, 5, 0);
		return this;
	}

}