package common_libs

import com.jayway.restassured.response.Response
import org.testng.Reporter
import org.yaml.snakeyaml.Yaml

import java.sql.Timestamp
import java.text.SimpleDateFormat


class CommonUtils {

    def mysql_url= [:]
    public static envParams = null
    def env_tag = 'tlmdevint'

    def generateAuthorizationToken(){
        def userName = 'supplychainadmin@1';
        def password = 'password';
    }


    def read_properties()
    {
        envParams = new Yaml().load(new FileReader(System.getProperty("user.dir") + '/src/test/groovy/config/tlm_env.yml'))[env_tag]
        if (envParams==null)
        {
            throw new Exception("No ENV defined for the env_tag="+ env_tag  + "under config/tlm_env.yml, please check and retry")
        }
        return envParams
    }

    def raise_unknown_db_exception(db_config)
    {
        throw new Exception("Database name not found in the Yaml file, please check and retry")
    }

     

    def add_mysql_url(db_config)
    {
        if(db_config==null)
        {
            return raise_unknown_db_exception(db_config)
        }
        else
        {
            mysql_url['url'] = "jdbc:mysql://${db_config['host']}:${db_config['port']}/${db_config["service"]}"
            return (db_config + mysql_url)
        }
    }

    def update_order_timestamp(order,minimum_days_from_now)
    {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        def dateToStart = 0;

        order.setPickupstartdatetime(timeStampformatter(timestamp.plus(dateToStart+minimum_days_from_now)))
        order.setPickupenddatetime(timeStampformatter(timestamp.plus(dateToStart+1+minimum_days_from_now)))
        order.setDeliverydtartdatetime(timeStampformatter(timestamp.plus(dateToStart+2+minimum_days_from_now)))
        order.setDeliveryenddatetime(timeStampformatter(timestamp.plus(dateToStart+3+minimum_days_from_now)))

    }

    def update_shipment_stop_timestamp(stop,index,minimum_days_from_now)
    {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        def dateToStart = index * 4;
        stop.setPlannedarrivalstart(timeStampformatter(timestamp.plus(dateToStart+minimum_days_from_now)))
        stop.setPlannedarrivalend(timeStampformatter(timestamp.plus(dateToStart+1+minimum_days_from_now)))
        stop.setPlannerdeparturestart(timeStampformatter(timestamp.plus(dateToStart+2+minimum_days_from_now)))
        stop.setPlanneddepartureend(timeStampformatter(timestamp.plus(dateToStart+3+minimum_days_from_now)))

    }

    def timeStampformatter(Timestamp timestamp)
    {
        String  simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss",Locale.US).format(timestamp)
        return simpleDateFormat

    }

    /**
     * This function generates unique string for a given string
    * @author : Samjain
     */
    static def getUniqueIdFor(String objId=""){
        return "${objId ? objId+'_' : ''}${System.currentTimeMillis()}"
    }

    /**
     * Get four digit random number
     */
    static def getFourDigitRandomNumber(){
        def random =  Math.abs(new Random().nextInt() % 1000 + 1)
        return random
    }

    /**
     * DataFormatter
     */
    def static formatDate(Date date){
        return new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss",Locale.US).format(date)
    }

    /**
     * validate if response contains exception
     */
     static def getExceptionFromResponse(Response response){
        def list = response.getBody().jsonPath().getList("exceptions.stackTrace")
        boolean isException
        println "/n stacktrace is : ${list.get(0)}"
        if(list.size()>0){
            isException = true
        }
        return isException
    }
}