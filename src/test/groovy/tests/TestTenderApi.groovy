package tests


import api.ShipmentApiUtil
import api.TenderApiUtil
import common_libs.CommonUtils
import connection_factories.RestAssuredUtils
import jsonTemplate.shipmentTemplate.BaseShipment
import jsonTemplate.tenderTemplate.BaseAccept
import jsonTemplate.tenderTemplate.BaseReset
import jsonTemplate.tenderTemplate.BaseTender
import jsonTemplate.tenderTemplate.BaseTenderConfig
import jsonTemplate.tenderTemplate.BaseTenderRecall
import jsonTemplate.tenderTemplate.BaseTenderReject
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class TestTenderApi {

    BaseShipment shipment
    def shipmentUtil
    def commonUtil
    def minimum_days_from_now
    def orgId
    BaseTender baseTender
    TenderApiUtil tenderApi
    BaseAccept baseAccept
    BaseTenderReject baseReject
    BaseTenderRecall baseRecall
    BaseReset baseReset
    BaseTenderConfig baseTenderConfig
    def tenderJson,tenderAcceptJson,shipmentJson,tenderRejectJson,tenderRecallJson,tenderResetJson,tenderConfigJson
    def stop_facilities=  ['FAC1', 'FAC2']
    def stop_actions = ['PU', 'DL']
    def orders=['HAR_ORDER_41']
    def tenderautoaccept='false'
    RestAssuredUtils restAssuredUtils;

    TestTenderApi()
    {
        shipment = new BaseShipment()
        shipmentUtil = new ShipmentApiUtil()
        tenderApi = new TenderApiUtil()
        baseTender = new BaseTender()
        commonUtil = new CommonUtils()
        minimum_days_from_now  = 2
        baseAccept = new BaseAccept()
        baseReject = new BaseTenderReject()
        baseRecall = new BaseTenderRecall()
        baseReset = new BaseReset()
        baseTenderConfig = new BaseTenderConfig()
        orgId='1'
    }
     @BeforeClass()
     public void beforeClass(){
        //Set the TenderAutoAccept=fale
        baseTenderConfig.setTenderautoaccept(tenderautoaccept)
        tenderConfigJson=baseTenderConfig.buildjson()
        println("Tender Config Json =" + tenderConfigJson)
        tenderApi.tenderMsg(tenderConfigJson,"configendpoint")

    }

    @Test(description = "Create a Shipment, Tender:Accept:Reject the Shipment")
     public void tenderAcceptRejectShipment()
    {
        def shipmentId='STARJ1_SHIPMENT_15'
        def carrierId='STARJ_CARRIER_01'

        def reasonCode ='REASON_CODE1'

        //Create Shipment Json
        shipment.setOrgid(orgId)
        shipment.setShipmentid(shipmentId)
        shipment.setAssignedcarrier(carrierId)
        shipment.shipmentstops = stop_facilities.collect{shipmentUtil.update_facilities_and_stops_on_tlm_shipment(stop_facilities.indexOf(it) ,shipment,minimum_days_from_now,stop_facilities, stop_actions)}
        shipment.shipmentordermovements=orders.collect{shipmentUtil.update_order_movement(orders.indexOf(it),shipment,orders)}
        shipmentJson=shipment.buildShipmentjsonOrderMovement()
        println("Shipment Json =" + shipmentJson)
        shipmentUtil.createShipment(shipmentJson)
        shipmentUtil.assert_for_Shipment_Stop(shipmentId,carrierId,stop_facilities,stop_actions)

        //Create Tender Json
        baseTender.setShipmentid(shipmentId)
        tenderJson = baseTender.buildjson()
        println("Tender Json =" + tenderJson)
        tenderApi.tenderMsg(tenderJson,"endpoint")
        tenderApi.assert_for_Tender_Status("TENDERED",shipmentId)

        //Create Tender Accept Json
        baseAccept.setShipmentid(shipmentId)
        baseAccept.setCarrierid(carrierId)
        tenderAcceptJson = baseAccept.buildjson()
        println("Accept Json =" + tenderAcceptJson)
        tenderApi.tenderMsg(tenderAcceptJson,"acceptendpoint")
        tenderApi.assert_for_Tender_Status("ACCEPTED",shipmentId)

        //Create Tender Reject Json
        baseReject.setShipmentid(shipmentId)
        baseReject.setCarrierid(carrierId)
        baseReject.setReasoncode(reasonCode)
        tenderRejectJson = baseReject.buildjson()
        println("Reject Json =" + tenderRejectJson)
        tenderApi.tenderMsg(tenderRejectJson,"rejectendpoint")
        tenderApi.assert_for_Tender_Status("REJECTED",shipmentId)

    }

    @Test(description = "Create a Shipment, Tender:Accept:Recall the Shipment")
    public void tenderAcceptRecallShipment(){

        def shipmentId='STARL_SHIPMENT_13'
        def carrierId='STARL_CARRIER_01'
        def reasonCode ='REASON_CODE2'

        //Create Shipment Json
        shipment.setOrgid(orgId)
        shipment.setShipmentid(shipmentId)
        shipment.setAssignedcarrier(carrierId)
        shipment.shipmentstops = stop_facilities.collect{shipmentUtil.update_facilities_and_stops_on_tlm_shipment(stop_facilities.indexOf(it) ,shipment,minimum_days_from_now,stop_facilities, stop_actions)}
        shipment.shipmentordermovements=orders.collect{shipmentUtil.update_order_movement(orders.indexOf(it),shipment,orders)}
        shipmentJson=shipment.buildShipmentjsonOrderMovement()
        println("Shipment Json =" + shipmentJson)
        shipmentUtil.createShipment(shipmentJson)
        shipmentUtil.assert_for_Shipment_Stop(shipmentId,carrierId,stop_facilities,stop_actions)

        //Create Tender Json
        baseTender.setShipmentid(shipmentId)
        tenderJson = baseTender.buildjson()
        println("Tender Json =" + tenderJson)
        tenderApi.tenderMsg(tenderJson,"endpoint")
        tenderApi.assert_for_Tender_Status("TENDERED",shipmentId)

        //Create Tender Accept Json
        baseAccept.setShipmentid(shipmentId)
        baseAccept.setCarrierid(carrierId)
        tenderAcceptJson = baseAccept.buildjson()
        println("Accept Json =" + tenderAcceptJson)
        tenderApi.tenderMsg(tenderAcceptJson,"acceptendpoint")
        tenderApi.assert_for_Tender_Status("ACCEPTED",shipmentId)

        //Create Tender Recall Json
        baseRecall.setShipmentid(shipmentId)
        baseRecall.setCarrierid(carrierId)
        baseRecall.setReasoncode(reasonCode)
        tenderRecallJson = baseRecall.buildjson()
        println("Recall Json =" + tenderRecallJson)
        tenderApi.tenderMsg(tenderRecallJson,"recallendpoint")
        tenderApi.assert_for_Tender_Status("RECALLED",shipmentId)
    }

    @Test(description = "Create a Shipment, Tender:Accept:Reset the Shipment")
    public void tenderAcceptResetShipment(){

        def shipmentId='START_SHIPMENT_13'
        def carrierId='START_CARRIER_01'
        def reasonCode ='REASON_CODE3'

        //Create Shipment Json
        shipment.setOrgid(orgId)
        shipment.setShipmentid(shipmentId)
        shipment.setAssignedcarrier(carrierId)
        shipment.shipmentstops = stop_facilities.collect{shipmentUtil.update_facilities_and_stops_on_tlm_shipment(stop_facilities.indexOf(it) ,shipment,minimum_days_from_now,stop_facilities, stop_actions)}
        shipment.shipmentordermovements=orders.collect{shipmentUtil.update_order_movement(orders.indexOf(it),shipment,orders)}
        shipmentJson=shipment.buildShipmentjsonOrderMovement()
        println("Shipment Json =" + shipmentJson)
        shipmentUtil.createShipment(shipmentJson)
        shipmentUtil.assert_for_Shipment_Stop(shipmentId,carrierId,stop_facilities,stop_actions)

        //Create Tender Json
        baseTender.setShipmentid(shipmentId)
        tenderJson = baseTender.buildjson()
        println("Tender Json =" + tenderJson)
        tenderApi.tenderMsg(tenderJson,"endpoint")
        tenderApi.assert_for_Tender_Status("TENDERED",shipmentId)

        //Create Tender Accept Json
        baseAccept.setShipmentid(shipmentId)
        baseAccept.setCarrierid(carrierId)
        tenderAcceptJson = baseAccept.buildjson()
        println("Accept Json =" + tenderAcceptJson)
        tenderApi.tenderMsg(tenderAcceptJson,"acceptendpoint")
        tenderApi.assert_for_Tender_Status("ACCEPTED",shipmentId)

        //Create Tender Reset Json
        baseReset.setShipmentid(shipmentId)
        baseReset.setReasoncode(reasonCode)
        tenderResetJson = baseReset.buildjson()
        println("Reset Json =" + tenderResetJson)
        tenderApi.tenderMsg(tenderResetJson,"resetendpoint")
        tenderApi.assert_for_Tender_Status("RESET",shipmentId)
    }

    @Test(description = "Create a Shipment, Tender:Reset:Tender:Recall the Shipment")
    public void tenderResetTenderRecall(){
        def shipmentId='STRTTRL_SHIPMENT_13'
        def carrierId='STRTTRL_CARRIER_01'
        def reasonCode ='REASON_CODE4'

        //Create Shipment Json
        shipment.setOrgid(orgId)
        shipment.setShipmentid(shipmentId)
        shipment.setAssignedcarrier(carrierId)
        shipment.shipmentstops = stop_facilities.collect{shipmentUtil.update_facilities_and_stops_on_tlm_shipment(stop_facilities.indexOf(it) ,shipment,minimum_days_from_now,stop_facilities, stop_actions)}
        shipment.shipmentordermovements=orders.collect{shipmentUtil.update_order_movement(orders.indexOf(it),shipment,orders)}
        shipmentJson=shipment.buildShipmentjsonOrderMovement()
        println("Shipment Json =" + shipmentJson)
        shipmentUtil.createShipment(shipmentJson)
        shipmentUtil.assert_for_Shipment_Stop(shipmentId,carrierId,stop_facilities,stop_actions)

        //Create Tender Json
        baseTender.setShipmentid(shipmentId)
        tenderJson = baseTender.buildjson()
        println("Tender Json =" + tenderJson)
        tenderApi.tenderMsg(tenderJson,"endpoint")
        tenderApi.assert_for_Tender_Status("TENDERED",shipmentId)

        //Create Tender Reset Json
        baseReset.setShipmentid(shipmentId)
        baseReset.setReasoncode()
        tenderResetJson = baseReset.buildjson()
        println("Reset Json =" + tenderResetJson)
        tenderApi.tenderMsg(tenderResetJson,"resetendpoint")
        tenderApi.assert_for_Tender_Status("RESET",shipmentId)

        //Create Tender Json
        baseTender.setShipmentid(shipmentId)
        tenderJson = baseTender.buildjson()
        println("Tender Json =" + tenderJson)
        tenderApi.tenderMsg(tenderJson,"endpoint")
        tenderApi.assert_for_Tender_Status("TENDERED",shipmentId)

        //Create Tender Recall Json
        baseRecall.setShipmentid(shipmentId)
        baseRecall.setCarrierid(carrierId)
        baseRecall.setReasoncode(reasonCode)
        tenderRecallJson = baseRecall.buildjson()
        println("Recall Json =" + tenderRecallJson)
        tenderApi.tenderMsg(tenderRecallJson,"recallendpoint")
        tenderApi.assert_for_Tender_Status("RECALLED",shipmentId)

    }

    @Test(description = "Create a Shipment, Tender:Reject the Shipment")
    public void tenderReject(){
        def shipmentId='STRJ_SHIPMENT_13'
        def carrierId='STRJ_CARRIER_01'
        def reasonCode ='REASON_CODE5'

        //Create Shipment Json
        shipment.setOrgid(orgId)
        shipment.setShipmentid(shipmentId)
        shipment.setAssignedcarrier(carrierId)
        shipment.shipmentstops = stop_facilities.collect{shipmentUtil.update_facilities_and_stops_on_tlm_shipment(stop_facilities.indexOf(it) ,shipment,minimum_days_from_now,stop_facilities, stop_actions)}
        shipment.shipmentordermovements=orders.collect{shipmentUtil.update_order_movement(orders.indexOf(it),shipment,orders)}
        shipmentJson=shipment.buildShipmentjsonOrderMovement()
        println("Shipment Json =" + shipmentJson)
        shipmentUtil.createShipment(shipmentJson)
        shipmentUtil.assert_for_Shipment_Stop(shipmentId,carrierId,stop_facilities,stop_actions)

        //Create Tender Json
        baseTender.setShipmentid(shipmentId)
        tenderJson = baseTender.buildjson()
        println("Tender Json =" + tenderJson)
        tenderApi.tenderMsg(tenderJson,"endpoint")
        tenderApi.assert_for_Tender_Status("TENDERED",shipmentId)

        //Create Tender Reject Json
        baseReject.setShipmentid(shipmentId)
        baseReject.setCarrierid(carrierId)
        baseReject.setReasoncode(reasonCode)
        tenderRejectJson = baseReject.buildjson()
        println("Reject Json =" + tenderRejectJson)
        tenderApi.tenderMsg(tenderRejectJson,"rejectendpoint")
        tenderApi.assert_for_Tender_Status("REJECTED",shipmentId)
    }


}
