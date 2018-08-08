package connection_factories

import com.jayway.restassured.RestAssured
import com.jayway.restassured.response.Response

class RestAssuredUtils {

    Response response

    public def postRequest(String endURL, Object jsonObj, String contentType) throws UnknownHostException {
        try{
        return RestAssured.given().body(jsonObj).when().contentType(contentType).post(endURL)}
        catch (UnknownHostException e)
        {
            assert false :"Please check the URL"
        }
    }

    public static Response getRequest(String endURL,String contentType){
          return RestAssured.given().when().contentType(contentType).get(endURL);
    }
}
