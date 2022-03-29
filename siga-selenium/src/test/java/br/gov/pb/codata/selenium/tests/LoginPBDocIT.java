package br.gov.pb.codata.selenium.tests;

import static br.gov.pb.codata.selenium.util.text.Dictionary.oneHundredChars;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.page_objects.SigadocLoginPage;
import br.gov.pb.codata.selenium.util.validator.Validator;

public class LoginPBDocIT extends DriverBase {

	public Validator validator = new Validator();

	@Test
	public void hyperlinks() throws Exception {
		WebDriver driver = getDriver();
		driver.manage().window().maximize();
		driver.get(System.getenv("PBDOC_URL"));
		SigadocLoginPage loginPage = new SigadocLoginPage();
		WebDriverWait wait = new WebDriverWait(driver, 30, 100);
		wait.until(validator.pageTitleStartsWith("PBdoc - Página de Login"));
		loginPage.clickPbdocLink(driver);
		loginPage.clickCodataLink(driver);
		loginPage.clickGovernoEstadoLink(driver);
		loginPage.clickSobrePbdocLink(driver);
		validator.closeAllTabsExcepCurrent();
	}

	// @Test1
	public void loginComSucesso() throws Exception {
		WebDriver driver = getDriver();
		driver.manage().window().setSize(new Dimension(1920, 1080));

		driver.get(System.getenv("PBDOC_URL"));

		SigadocLoginPage loginPage = new SigadocLoginPage();

		loginPage.logar().enviarAutenticacao();

		WebDriverWait wait = new WebDriverWait(driver, 15, 100);
		wait.until(validator.pageTitleStartsWith("PBDoc - Mesa Virtual"));
	}

	// @Test
	public void loginInvalido() throws Exception {
		WebDriver driver = getDriver();
		driver.manage().window().setSize(new Dimension(1920, 1080));
		driver.get(System.getenv("PBDOC_URL"));
		SigadocLoginPage loginPage = new SigadocLoginPage();
		loginPage.digitarCredenciais("usuarioNaoCadastrado", "Senha123").enviarAutenticacao();
		WebDriverWait wait = new WebDriverWait(driver, 15, 100);
		wait.until(validator.paginaDeveMostrarMensagem(
				"Ocorreu um erro tentando localizar a identidade do usuario 'usuarioNaoCadastrado'."));
	}

	// @Test
	public void validacaoDeInputs() throws Exception {
		WebDriver driver = getDriver();
		driver.manage().window().setSize(new Dimension(1920, 1080));

		driver.get(System.getenv("PBDOC_URL"));

		SigadocLoginPage loginPage = new SigadocLoginPage();

		loginPage.digitarCredenciais(oneHundredChars, oneHundredChars).enviarAutenticacao();
	}

}