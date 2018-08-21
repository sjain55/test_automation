package common_libs

import connection_factories.RestAssuredUtils
import db.DbConnectionFactory
import test_data_models.BaseOrder
import test_data_models.BaseOrderLine
import test_data_models.BaseShipmentNote
import test_data_models.BaseShipmentOrderMovement
import test_data_models.BaseShipmentStop
import test_data_models.BaseTender

import java.sql.Timestamp
import java.text.SimpleDateFormat

class OrderUtils {
    RestAssuredUtils rest
    BaseTender tender
    String URL
    def config  = new CommonUtils()
    def order_app_config
    def order_config
    def order_db_config
    DbConnectionFactory db

    OrderUtils()
    {
        rest = new RestAssuredUtils()
        db = new DbConnectionFactory()
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

    def timeStampformatter(Timestamp timestamp)
    {
        String  simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss",Locale.US).format(timestamp);
        return simpleDateFormat

    }

    def update_order_Lines(index, order_line_item,order )
    {
        BaseOrderLine orderline = new BaseOrderLine(order.orgid,order.orderid)
        orderline.setOrderlineid(index+1)
        orderline.setItemid(order_line_item[index])
        return orderline
    }

    def createDistributionOrder(orderJson)
    {
        try {
            order_config = config.read_properties()
            order_app_config = order_config['app_config']['order']
            order_db_config= config.add_mysql_url(order_config['db_config']['order'])
            URL = order_app_config['url']+ order_app_config['create_endpoint']
            println ("URL = " +URL)
            def status = rest.postRequest(URL, orderJson, "application/json","1")

            println("Status =" + status)
            if(status.getStatusCode()!=200)
            {
                throw  new Exception("Unable to post Request to "+ URL)
            }
        }catch(Exception e){
            assert false: "Exception occured ${e.printStackTrace()}"
        }
    }
}