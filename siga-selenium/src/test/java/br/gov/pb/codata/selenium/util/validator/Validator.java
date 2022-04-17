package br.gov.pb.codata.selenium.util.validator;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.gov.pb.codata.selenium.DriverBase;

public class Validator extends DriverBase {

	public ExpectedCondition<Boolean> pageTitleStartsWith(final String searchString) {
		return driver -> driver.getTitle().toLowerCase().startsWith(searchString.toLowerCase());
	}

	public ExpectedCondition<Boolean> findMessageByXPath(final String msg, String xpath) {
		return driver -> driver.findElement(By.xpath(xpath)).getText().equalsIgnoreCase(msg);
	}

	public void switchTabs(WebDriver driver, int expectedWindowsCount, int SwitchtoWindow) throws Exception {
		(new WebDriverWait(driver, 30)).until(ExpectedConditions.numberOfWindowsToBe(expectedWindowsCount));
		ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs2.get(SwitchtoWindow));
	}
	
	public void closeAllTabsExcepCurrent() throws Exception{
		WebDriver driver = getDriver();
		String originalHandle = driver.getWindowHandle();
	    for(String handle : driver.getWindowHandles()) {
	        if (!handle.equals(originalHandle)) {
	            driver.switchTo().window(handle);
	            driver.close();
	        }
	    }
	    driver.switchTo().window(originalHandle);
	}
	
	public ExpectedCondition<Boolean> pageURLContains(String url){
		return driver -> driver.getCurrentUrl().contains(url);
	}
	
	public void acceptAlert() throws Exception {
		getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Alert alert = getDriver().switchTo().alert();
		String alertText = alert.getText();
		System.out.println("Alert data: " + alertText);
		alert.accept();
	}

}