import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;


public class VkTest {
    private static final int DELAY_TIME = 2500;

    private static final String MARRIGE_TEXT = "замужем";
    private static final String POSITIV_MESSAGE = "Изменения корректны";
    private static final String NEGATIV_MESSAGE = "Изменения некоректны";
    private static final String TEXT_POST = "Я учусь в УНЦ «Инфоком» NetCracker. Это круто!!!";

    private static final String PAGE_LINK = "http://www.vk.com";
    private static final String FIELD_EMAIL = "index_email";
    private static final String FIELD_PASS = "index_pass";
    private static final String LOGIN_BUTTON = "index_login_button";
    private static final String MAIN_MENU = "#side_bar_inner li";
    private static final String MY_PAGE = "l_pr";
    private static final String MY_POST = "post_field";
    private static final String SEND_POST = "send_post";
    private static final String PROFILE = "top_profile_link";
    private static final String EDIT = "top_edit_link";
    private static final String FAMILY_STATUS = "#pedit_status_row .selector_dropdown:nth-child(2)";
    private static final String MARRIGE_STATUS = "#pedit_status_row li:nth-child(5)";
    private static final String EDIT_BUTTON = ".pedit_controls .flat_button.button_big_width";
    private static final String INTERESTS = "ui_rmenu_interests";
    private static final String ABOUT = "pedit_interests_about";
    private static final String MORE_PAGE = "#profile_short .profile_more_info";
    private static final String WAIT = "#pedit_result";
    private static final String MARRIGE_STATUS_PROFILE = "#profile_short > div:nth-child(3) .labeled a";
    private static final String ABOUT_PROFILE ="#profile_full > div:nth-child(4) div.profile_info > div:nth-child(2) .labeled";

    private static final String UPLOAD_PATH = "./Screenshots/";
    private static final String WEB_DRIVER = "WebDriver.Chrome.Driver";
    private static final String PATH_TO_WEB_DRIVER = "./chromedriver.exe";


    void doTest(String login, String password) throws IOException, InterruptedException {


        System.setProperty(WEB_DRIVER, PATH_TO_WEB_DRIVER);


        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(PAGE_LINK);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        //Вход в учетную запись
        driver.findElement(By.id(FIELD_EMAIL)).sendKeys(login);
        driver.findElement(By.id(FIELD_PASS)).sendKeys(password);
        driver.findElement(By.id(LOGIN_BUTTON)).click();

        //Открытие меню
        File scrFile;
        List<WebElement> menu = driver.findElements(By.cssSelector(MAIN_MENU));

        File uploadDir = new File(UPLOAD_PATH);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        for (WebElement elementMenu : menu) {
            elementMenu.click();
            Thread.sleep(DELAY_TIME);
            scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File(UPLOAD_PATH + elementMenu.hashCode() + ".png"));
        }

        //Добавление поста
        driver.findElement(By.id(MY_PAGE)).click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.id(MY_POST)));

        driver.findElement(By.id(MY_POST)).clear();
        driver.findElement(By.id(MY_POST)).sendKeys(TEXT_POST);
        driver.findElement(By.id(SEND_POST)).click();

        if (driver.getPageSource().contains(TEXT_POST)) {
            System.out.println(POSITIV_MESSAGE);
        } else {
            System.out.println(NEGATIV_MESSAGE);
        }

        //Изменение семейного положения
        driver.findElement(By.id(PROFILE)).click();
        driver.findElement(By.id(EDIT)).click();
        driver.findElement(By.cssSelector(FAMILY_STATUS)).click();
        driver.findElement(By.cssSelector(MARRIGE_STATUS)).click();
        driver.findElement(By.cssSelector(EDIT_BUTTON)).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(WAIT)));

        //Изменение "Обо мне"
        driver.findElement(By.id(INTERESTS)).click();
        driver.findElement(By.id(ABOUT)).clear();
        driver.findElement(By.id(ABOUT)).sendKeys(TEXT_POST);
        driver.findElement(By.cssSelector(EDIT_BUTTON)).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(WAIT)));

        //Проверка добавления изменений
        driver.findElement(By.id(MY_PAGE)).click();
        driver.findElement(By.cssSelector(MORE_PAGE)).click();

        WebElement marrigeText = driver.findElement(By.cssSelector(MARRIGE_STATUS_PROFILE));
        assertEquals(MARRIGE_TEXT, marrigeText.getText());

        WebElement aboutText = driver.findElement(By.cssSelector(ABOUT_PROFILE));
        assertEquals(TEXT_POST, aboutText.getText());

        if (driver.getPageSource().contains(MARRIGE_TEXT)) {
            System.out.println(POSITIV_MESSAGE);
        } else {
            System.out.println(NEGATIV_MESSAGE);
        }

        if (driver.getPageSource().contains(TEXT_POST)) {
            System.out.println(POSITIV_MESSAGE);
        } else {
            System.out.println(NEGATIV_MESSAGE);
        }

        driver.quit();
    }
}

