package tests

import api.AcceptApi
import api.TenderApi
import connection_factories.RestAssuredUtils
import db.DbConnectionFactory
import org.testng.annotations.Test
import jsonTemplate.BaseAccept
import jsonTemplate.BaseTender

class TestAcceptApi{

    BaseAccept baseAccept
    AcceptApi acceptApi
    DbConnectionFactory db
    def acceptJson
    def tenderJson
    RestAssuredUtils rest
    BaseTender baseTender
    TenderApi tenderApi

    TestAcceptApi()
    {
        baseAccept = new BaseAccept()
        acceptApi = new AcceptApi()
        db = new DbConnectionFactory()
        baseTender = new BaseTender()
        tenderApi = new TenderApi()
    }

    @Test(description = "Tender and Accept the given shipment")
    public void acceptTenderShipment()
    {
        def shipmentid='FusionAuto88912'
        baseTender.setShipmentid(shipmentid)
        tenderJson = baseTender.buildjson()
        tenderApi.send(tenderJson)
        tenderApi.assert_for_Tender_Status("NOT_TENDERED",shipmentid)
        baseAccept.setShipmentid(shipmentid)
        acceptJson = baseAccept.buildjson()
        acceptApi.send(acceptJson)
        tenderApi.assert_for_Tender_Status("ACCEPTED",shipmentid)
    }



}


