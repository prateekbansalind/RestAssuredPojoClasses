import files.Response;
import io.restassured.parsing.Parser;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import pojo.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static files.credentials.*;
import static io.restassured.RestAssured.*;

public class OAuthTest {
    public static void main(String[] args) throws InterruptedException {

        // expected courses present in the web automation category
        String[] expectedWebAutomationCoursesArray = {"Selenium Webdriver Java", "Cypress", "Protractor"};

        System.setProperty("webdriver.chrome.driver", "C://Users//prate//IdeaProjects//RestAssuredPojoClasses//chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://accounts.google.com/o/oauth2/v2/auth/identifier?scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&auth_url=https%3A%2F%2Faccounts.google.com%2Fo%2Foauth2%2Fv2%2Fauth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https%3A%2F%2Frahulshettyacademy.com%2FgetCourse.php&flowName=GeneralOAuthFlow");
        driver.findElement(By.cssSelector("input[type='email']")).sendKeys(getUserName());
        driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER);
        Thread.sleep(3000);
        driver.findElement(By.cssSelector("input[type='password']")).sendKeys(getPassword());
        driver.findElement(By.cssSelector("input[type='password']")).sendKeys(Keys.ENTER);
        Thread.sleep(4000);
        String redirectURL = driver.getCurrentUrl();
        String redirectURLSplit = redirectURL.split("code=")[1];
        String code = redirectURLSplit.split("&")[0];
        System.out.println(code);

        // Now we have a code, let get an access token from the Google resources.
        String hitGoogleResourceResponse = given()
                .urlEncodingEnabled(false)
                .queryParams("code",code)
                .queryParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .queryParams("client_secret","erZOWM9g3UtwNRj340YYaK_W")
                .queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
                .queryParams("grant_type","authorization_code")
                .when().log().all().post("https://www.googleapis.com/oauth2/v4/token")
                .asString();

        System.out.println(hitGoogleResourceResponse);

        var hitGoogleResourceResponseJSON = Response.stringToJson(hitGoogleResourceResponse);
        String access_token = hitGoogleResourceResponseJSON.getString("access_token");
        System.out.println(access_token);

        // here we will take reverse approach, what I mean is first we write a code to hit the getCourse API.
        GetCourses ResponseClass = given().queryParam("access_token", access_token)
                .expect().defaultParser(Parser.JSON) // we need to specify what type of data we are expecting to hold in our pojo class.
                // we could have avoid this .expect().defaultParser method if in my response header Content-Type value is JSON.
                .when().get("https://rahulshettyacademy.com/getCourse.php")
                .as(GetCourses.class);
                // here we are storing out response JSON directly into pojo class.
//                .asString(); // asString() can be directly call to see the result fastly but cannot use assertion.
                //.then().extract().response().asString(); - this is used when we are doing any assertion
                // we cannot print any raw response from the api response that's why we always need to convert them.

        System.out.println(ResponseClass.getLinkedIn());
        System.out.println(ResponseClass.getInstructor());

        // this is a static way of doing it but what if something get deleted from the array and the index has been changed.
        // so we need to dynamically target the desired response.
        String apiIndexOneCourseName = ResponseClass.getCourses().getApi().get(1).getCourseTitle();
        System.out.println(apiIndexOneCourseName);

        // Dynamically get the desired response
        var apiCourses = ResponseClass.getCourses().getApi();
        for (var key : apiCourses)
            if (key.getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing"))
                System.out.println(key.getPrice());

        // Dynamically retrieve all the courses from the webAutomation courses.
        ArrayList<String> actualWebAutomationCourses = new ArrayList<String>();
        var webAutomationCourses = ResponseClass.getCourses().getWebAutomation();
        for (var key : webAutomationCourses)
            actualWebAutomationCourses.add(key.getCourseTitle());
        System.out.println(actualWebAutomationCourses); // this line holds a dynamic array list of all the course belongs to webAutomation.

        // it's complicated to compare array with arraylist and vice versa.
        // So, it's better either of them to change into the one so the comparison can become easy.
        // In this project, we will convert the webAutomationCourses (expected) array into array list and then
        // compare it with the actual result which is saved in the arrayList.
        List<String> expectedWebAutomationCourses = Arrays.asList(expectedWebAutomationCoursesArray);
        // TestNG Assert Method
        // Assert.assertTrue()
        Assert.assertTrue(actualWebAutomationCourses.equals(expectedWebAutomationCourses));



    }


}
