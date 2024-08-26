package jsf.form.archive.util.test;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.engine.EngineUrl;

/**
 * Test archived jsf page
 */
@IvyWebTest(headless = true)
public class FormArchiveWebTestIT{

  @Test
  public void navigateToInfoPage(){
    open(EngineUrl.base());

    $("img").shouldBe(attribute("alt", "Logo"));
    $(By.tagName("img")).shouldBe(attribute("alt", "Logo"));
  }

  @Test
  public void runProcessWithArchivedForm() {
    // valid links can be copied from the start page of the internal web-browser
    open(EngineUrl.createProcessUrl("jsf-form-archive-util-test/191767AC8BC4F5B8/start.ivp"));

    // fill new customer form
    $(By.id("form:items")).sendKeys("Unit");
    $(By.id("form:amount_input")).sendKeys("123");

    // verify that the submit button is enabled, before clicking it.
    $(By.id("form:send")).shouldBe(enabled).click();

    //verify that screenshot of form1 is present
    $(By.id("InitTask.bmp")).shouldBe(attribute("id", "InitTask.bmp"));
  }

} 