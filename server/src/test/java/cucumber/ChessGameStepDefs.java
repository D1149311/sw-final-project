package cucumber;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class ChessGameStepDefs {
    private final int clientCount = 2;
    private WebDriver[] clients = new WebDriver[clientCount];
    private Integer[] points = new Integer[clientCount];
    private Integer[] roomIds = new Integer[clientCount];
    private final String testedURL = "http://127.0.0.1:8080/";

    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");
        options.addArguments("--remote-allow-origins=*");

        for (int i = 0; i < clientCount; i++) {
            WebDriver client = new ChromeDriver(options);
            client.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
            clients[i] = client;
            points[i] = 0;
            roomIds[i] = 0;
        }
    }

    @After
    public void tearDown() {
        for (int i = 0; i < clientCount; i++) {
            clients[i].close();
            points[i] = 0;
            roomIds[i] = 0;
        }
    }

    @Given("Client{int} entered the website")
    public void i_entered_the_website(int client) {
        clients[client].get(testedURL);
    }

    @When("Client{int} enter {string} in the username field")
    public void i_enter_in_the_username_field(int client, String username) {
        WebElement usernameInput = clients[client].findElement(By.id("username"));
        usernameInput.clear();
        usernameInput.sendKeys(username);
    }

    @When("Client{int} enter {string} in the password field")
    public void i_enter_in_the_password_field(int client, String password) {
        WebElement passwordInput = clients[client].findElement(By.id("password"));
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    @When("Client{int} click the {string} button")
    public void i_click_the_button(int client, String button) throws InterruptedException {
        WebElement submitButton = clients[client].findElement(By.id(button));
        submitButton.click();
        Thread.sleep(1000);
    }

    @When("Client{int} enter the room number with client{int}")
    public void i_enter_the_prompt(int client, int room) {
        clients[client].switchTo().alert().sendKeys(String.valueOf(roomIds[room]));
    }

    @When("Client{int} click the board at {int} {int}")
    public void i_click_the_board(int client, int x, int y) throws InterruptedException {
        WebElement grid = clients[client].findElement(By.id(y + "-" + x));
        grid.click();
        Thread.sleep(500);
    }

    @When("Client{int} choose {string} to promote")
    public void i_choose_to_promote(int client, String choice) throws InterruptedException {
        WebElement grid = clients[client].findElement(By.xpath("//*[@class='choice']//img[@alt='" + choice + "']"));
        grid.click();
        Thread.sleep(1000);
    }

    @Then("Client{int} should be in the {string} page")
    public void i_should_be_in_the_page(int client, String page) throws InterruptedException {
        Thread.sleep(1000);
        String url = clients[client].getCurrentUrl();
        assertEquals(testedURL + page, url);
    }

    @Then("Client{int} should see the error message")
    public void i_should_see_the_error_message(int client) {
        WebElement greeting = clients[client].findElement(By.className("error"));
        assertEquals("wrong username or password", greeting.getText());
    }

    @Then("Client{int} should see the greeting {string}")
    public void i_should_see_the_greeting(int client, String expectedGreeting) {
        WebElement greeting = clients[client].findElement(By.id("name"));
        assertEquals(expectedGreeting, greeting.getText());
    }

    @Then("Client{int} should see the waiting text")
    public void i_should_see_the_waiting_text(int client) {
        WebElement waiting = clients[client].findElement(By.className("waiting"));
        assertEquals("等待中...", waiting.getText().substring(0, 6));
    }

    @Then("Client{int} save the room number")
    public void i_save_the_room_number(int client) {
        WebElement waiting = clients[client].findElement(By.className("waiting"));;
        roomIds[client] = Integer.parseInt(waiting.getText().substring(13));
    }

    @Then("Client{int} should see the {string} on the grid {int} {int}")
    public void i_should_see_piece_on_the_grid(int client, String piece, int x, int y) {
        if ("".equals(piece)) {
            assertThrows(NoSuchElementException.class, () -> clients[client].findElement(By.xpath("//*[@id='" + y + "-" + x + "']//img")));
        } else {
            WebElement grid = clients[client].findElement(By.xpath("//*[@id='" + y + "-" + x + "']//img"));
            assertEquals(piece, grid.getAttribute("alt"));
        }
    }

    @Then("Client{int} should see the threatened on the grid {int} {int}")
    public void i_should_see_threatened_on_the_grid(int client, int x, int y) {
        WebElement grid = clients[client].findElement(By.id(y + "-" + x));
        String[] names = grid.getAttribute("class").split(" ");
        boolean eatable = false;
        for (String name : names) {
            if ("eatable".equals(name)) {
                eatable = true;
                break;
            }
        }
        assertTrue(eatable);
    }

    @Then("Client{int} should see the {string} on the alert")
    public void i_should_see_the_text_on_the_alert(int client, String text) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(clients[client], Duration.ofSeconds(5));
        wait.until(ExpectedConditions.alertIsPresent());
        assertEquals(text, clients[client].switchTo().alert().getText());
    }

    @Then("Client{int} accept the alert")
    public void i_accept_the_alert(int client) {
        clients[client].switchTo().alert().accept();
    }
}
