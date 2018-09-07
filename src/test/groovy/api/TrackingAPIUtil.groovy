package api

import com.jayway.restassured.response.Response
import common_libs.CommonUtils
import connection_factories.RestAssuredUtils
import db.DbConnectionFactory
import jsonTemplate.tenderTemplate.BaseTender
import jsonTemplate.trackingTemplate.BaseTracking

class TrackingAPIUtil {
    BaseTracking baseTracking
    RestAssuredUtils rest
    ShipmentApiUtil shipmentUtil
    BaseTender tender
    String URL
    def config  = new CommonUtils()
    def tracking_app_config
    def tracking_config
    DbConnectionFactory db
    def orgId="dummyOrg"
    def carrierId="Auto_CARR_01"
    def shipmentId


    TrackingAPIUtil(){
        rest = new RestAssuredUtils()
        tender = new BaseTender()
        db = new DbConnectionFactory()
        shipmentUtil = new ShipmentApiUtil()
        tracking_config = config.read_properties()
        tracking_app_config = tracking_config['app_config']['tracking']
    }


    def createTrackingMessage(message){
       // shipment_config = config.read_properties()
       // tracking_app_config = shipment_config['app_config']['tracking']
        URL = tracking_app_config['url'] + tracking_app_config['endpoint']
        Response res = rest.postRequest(URL,message,"application/json")
        println res.getStatusCode()==200?"Tracking message is created":"Tracking message is not created"
        return res
    }

    def validateTransitStatusOfAShipment(String shipmentId,String expStatus){
        def sql = db.shipmentDbProperties()
        def resultset = sql.rows("select TRANSIT_STATUS from SCS_SHIPMENT where SHIPMENT_ID=${shipmentId}")
        assert (resultset.TRANSIT_STATUS.get(0))==expStatus:"Transit status is not found to be as expected ${resultset.TRANSIT_STATUS.get(0)}"
    }


    def createTrackingMessage(def messageType,def stopSeq,def shipmentId,def address,def carrierId){
        baseTracking = new BaseTracking()
        baseTracking.setMessageType(messageType)
        baseTracking.setStopSeq(stopSeq)
        baseTracking.setShipmentid(shipmentId)
        baseTracking.setAddress(address)
        baseTracking.setCarrierId(carrierId)
        URL = tracking_app_config['url'] + tracking_app_config['endpoint']
        println "tracking josn is: ${baseTracking.buildjson()}"
        Response res = rest.postRequest(URL,baseTracking.buildjson(),"application/json")
        println "Exception exists : ${CommonUtils.getExceptionFromResponse(res)}"
        println res.getStatusCode()==200?"Tracking message is created":"Tracking message is not created"
        return res
    }


    def validateMessageTypeInDB(String shipmentId,String messagetype){
        def sql = db.trackingDbProperties()
        def resultset = sql.rows("select MESSAGE_TYPE from TK_TRACKING where SHIPMENT_ID=${shipmentId}")
        assert (resultset.MESSAGE_TYPE.get(0))==messagetype:"Transit status is not found to be as expected ${resultset.MESSAGE_TYPE.get(0)}"
    }





}
