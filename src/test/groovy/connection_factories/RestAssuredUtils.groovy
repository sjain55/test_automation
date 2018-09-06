package connection_factories

import com.jayway.restassured.RestAssured
import com.jayway.restassured.path.json.JsonPath
import com.jayway.restassured.response.Response
import groovy.json.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser
import sun.security.krb5.Credentials;


class RestAssuredUtils {

    Response response

   public def postRequest(String endURL, Object jsonObj, String contentType) throws UnknownHostException {
        try{

                 return RestAssured.given()
                         .header("Authorization", "Bearer " +tokenAuthentication())
                         .header("Organization","1")
                         .header("Location","01")
                         .body(jsonObj)
                         .when()
                         .contentType(contentType)
                         .post(endURL)
        }
        catch (UnknownHostException e){
            assert false :"Please check the URL"
        }
    }

    public def tokenAuthentication()
    {
        def token
        HttpUriRequest request = RequestBuilder.post()
                .setUri("https://authserver.sc2020devint.manhdev.com/"+"oauth/token")
                .setHeader("Authorization", "Basic b21uaWNvbXBvbmVudC4xLjAuMDpiNHM4cmdUeWc1NVhZTnVu")
                .addParameter("username", "supplychainadmin@1")
                .addParameter("password", "password")
                .addParameter("grant_type", "password")
                .build();
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();

        JsonElement root = new JsonParser().parse(EntityUtils.toString(entity));
        token = root.getAsJsonObject().get("access_token").toString().split("\"")[1];
        println("Auth token http is : "+token)
        return token;
    }

    public static Response getRequest(String endURL,String contentType){
          return RestAssured.given().when().contentType(contentType).get(endURL);
    }



}
