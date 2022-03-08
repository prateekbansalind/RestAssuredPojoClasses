package files;

import io.restassured.path.json.JsonPath;

public class Response {
    public static String responseInString(){
        return "{\n" +
                "  \"instructor\": \"RahulShetty\",\n" +
                "  \"url\": \"rahulshettycademy.com\",\n" +
                "  \"services\": \"projectSupport\",\n" +
                "  \"expertise\": \"Automation\",\n" +
                "  \"courses\": {\n" +
                "    \"webAutomation\": [\n" +
                "      {\n" +
                "        \"courseTitle\": \"Selenium Webdriver Java\",\n" +
                "        \"price\": \"50\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"courseTitle\": \"Cypress\",\n" +
                "        \"price\": \"40\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"courseTitle\": \"Protractor\",\n" +
                "        \"price\": \"40\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"api\": [\n" +
                "      {\n" +
                "        \"courseTitle\": \"Rest Assured Automation using Java\",\n" +
                "        \"price\": \"50\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"courseTitle\": \"SoapUI Webservices testing\",\n" +
                "        \"price\": \"40\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"mobile\": [\n" +
                "      {\n" +
                "        \"courseTitle\": \"Appium-Mobile Automation using Java\",\n" +
                "        \"price\": \"50\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"linkedIn\": \"https://www.linkedin.com/in/rahul-shetty-trainer/\"\n" +
                "}";
    }

    public static JsonPath stringToJson(String responseInString){
        JsonPath responseInJson = new JsonPath(responseInString);
        return responseInJson;
    }
}
