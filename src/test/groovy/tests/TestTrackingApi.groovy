package tests

import api.AcceptApi

import api.ShipmentApiUtil
import api.TenderApi
import api.TrackingAPIUtil
import com.jayway.restassured.response.Response
import db.DbConnectionFactory
import jsonTemplate.trackingTemplate.Address
import jsonTemplate.tenderTemplate.BaseAccept
import jsonTemplate.shipmentTemplate.BaseShipment
import jsonTemplate.tenderTemplate.BaseTender
import jsonTemplate.trackingTemplate.BaseTracking
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class TestTrackingAPT {
    AcceptApi accept
    BaseAccept acceptTender
    BaseShipment shipment
    BaseTender tender
    BaseTracking tracking
    TrackingAPIUtil trackingUtil
    TenderApi tenderUtil
    def shipment_db_config
    DbConnectionFactory db
    def shipmentUtil
    def orgId='dummyOrg'


    TestTrackingAPT()
    {
        shipment = new BaseShipment()
        tender = new BaseTender()
        shipmentUtil = new ShipmentApiUtil()
        db = new DbConnectionFactory()
        trackingUtil = new TrackingAPIUtil()
        tenderUtil = new TenderApi()
        acceptTender = new BaseAccept()
        accept = new AcceptApi()
    }

    @Test
    public void createTrackingMesage(){
        def id
        def stoplist = ['FAC1', 'FAC2', 'FAC3', 'FAC4']
        def stopAction = ['PU', 'DL', 'DL', 'DL']
        def order = ['Order1','Order2','Order3']
        id = shipmentUtil.createShipmentWithStopsAndStopActions(stoplist, stopAction, order, trackingUtil)
        tender.setShipmentid(id)
        tenderUtil.createTender(tender.buildjson())
        acceptTender.setShipmentid(id)
        acceptTender.setCarrierid(trackingUtil.carrierId)
        accept.acceptTender(acceptTender.buildjson())
        Address address = new Address()
        address.setAddress1("Test address1")
        address.setAddress2("Test address2")
        address.setCity("New York")
        tracking = new BaseTracking(id,trackingUtil.carrierId,address)
        Response res = trackingUtil.createTrackingMessage(tracking.buildjson())
        assert res.getStatusCode()==200:"Tracking message is not created as response code is not 200"
        trackingUtil.validateTransitStatusOfAShipment(id,"IN TRANSIT")
    }

    @Test(description = "This test case validates that shipment transit status gets changed based on tacking message type (SCE-2830)")
    public void validateShipmentTransitStatusBasedOnMessageType(){
        def id
        def stoplist = ['FAC1', 'FAC2', 'FAC3', 'FAC4']
        def stopAction = ['PU', 'DL', 'DL', 'DL']
        def order = ['Order1','Order2','Order3']
        id = shipmentUtil.createShipmentWithStopsAndStopActions(stoplist, stopAction, order, trackingUtil)
        tender.setShipmentid(id)
        tenderUtil.createTender(tender.buildjson())
        acceptTender.setShipmentid(id)
        acceptTender.setCarrierid(trackingUtil.carrierId)
        accept.acceptTender(acceptTender.buildjson())
        Address address = new Address()
        address.setAddress1("Test address1")
        address.setAddress2("Test address2")
        address.setCity("New York")
        trackingUtil.validateTransitStatusOfAShipment(id,"NOT STARTED")
        trackingUtil.createTrackingMessage("Arrival","1",id,address,trackingUtil.carrierId)
        trackingUtil.validateTransitStatusOfAShipment(id,"IN TRANSIT")
        trackingUtil.createTrackingMessage("Departure","1",id,address,trackingUtil.carrierId)
        trackingUtil.validateTransitStatusOfAShipment(id,"IN TRANSIT")
        trackingUtil.createTrackingMessage("Arrival","2",id,address,trackingUtil.carrierId)
        trackingUtil.validateTransitStatusOfAShipment(id,"IN TRANSIT")
        trackingUtil.createTrackingMessage("Departure","2",id,address,trackingUtil.carrierId)
        trackingUtil.validateTransitStatusOfAShipment(id,"IN TRANSIT")
        trackingUtil.createTrackingMessage("Arrival","3",id,address,trackingUtil.carrierId)
        trackingUtil.validateTransitStatusOfAShipment(id,"IN TRANSIT")
        trackingUtil.createTrackingMessage("Departure","3",id,address,trackingUtil.carrierId)
        trackingUtil.validateTransitStatusOfAShipment(id,"IN TRANSIT")
        trackingUtil.createTrackingMessage("Arrival","4",id,address,trackingUtil.carrierId)
        trackingUtil.validateTransitStatusOfAShipment(id,"Delivered")
        trackingUtil.createTrackingMessage("Departure","5",id,address,trackingUtil.carrierId)
        trackingUtil.validateTransitStatusOfAShipment(id,"Completed")
    }


    @DataProvider
    public Object[][] getMessageType(){
        Object[][] obj = new Objects[0][3]
        obj[0][0]="Arrival"
        obj[0][1]="ETA"
        obj[0][2]="ETD"
        obj[0][3]="Departure"
        return obj
    }

    @Test(description = "This test case validates if user is able to send tracking message of type ETA and ETD and store in DB",dataProvider = "getMessageType")
    void validateEtaAndEtdMessageType(String messageType){
        def id = shipmentUtil.createShipmentAndAcceptTender()
        Address address = new Address()
        address.setAddress1("Test address1")
        address.setAddress2("Test address2")
        address.setCity("New York")
        trackingUtil.createTrackingMessage(messageType,"1",id,address,shipment.assignedcarrier)
        trackingUtil.validateMessageTypeInDB(id,messageType)
    }
}
