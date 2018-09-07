package api

import common_libs.CommonUtils
import connection_factories.RestAssuredUtils
import jsonTemplate.tenderTemplate.BaseAccept

class AcceptApi {

    RestAssuredUtils rest
    BaseAccept accept
    String URL
    def config  = new CommonUtils()
    def accept_app_config
    def accept_config
    def accept_db_config

    AcceptApi()
    {
        rest = new RestAssuredUtils()
        accept = new BaseAccept()
    }

    def acceptTender(msg)
    {
        try {
            accept_config = config.read_properties()
            accept_app_config = accept_config['app_config']['accept']
            accept_db_config= config.add_mysql_url(accept_config['db_config']['accept'])
            URL = accept_app_config['url']+ accept_app_config['endpoint']
            def status = rest.postRequest(URL, msg, "application/json")
            if(status.getStatusCode()!=200)
            {
                throw  new Exception("Unable to post Request to "+ URL)
            }
        }
        catch (Exception e) {
            assert false:"Exception occured ${e.printStackTrace()}"
        }

    }

}
