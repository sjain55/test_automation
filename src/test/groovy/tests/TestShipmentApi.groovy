package tests

import api.OrderAPIUtil
import api.ShipmentAPIUtil
import common_libs.CommonUtils
import org.testng.annotations.Test
import jsonTemplate.BaseOrder
import jsonTemplate.BaseShipment

class TestShipmentApi {

    BaseShipment shipment
    BaseOrder order
    def shipmentUtil = new ShipmentAPIUtil();
    def orderUtil = new OrderAPIUtil();
    def commonUtil = new CommonUtils()
    def minimum_days_from_now  = 2
    def orgId='dummyOrg'


    def shipmentJson,shipmentNoteJson,orderJson

    TestShipmentApi()
    {
        shipment = new BaseShipment()
        order = new BaseOrder()
    }

    @Test(description = "Create a 4 Stop shipment, ensure check in Stop Table")
    public void createShipment()
    {
        def shipmentId='HAR_SHIPMENT_01'
        def carrierId='HAR_CARRIER_01'
        def stop_facilities=  ['FAC1', 'FAC2', 'FAC3', 'FAC4']
        def stop_actions = ['PU', 'DL', 'DL', 'DL']

        //Create Shipment Json
        shipment.setOrgid(orgId)
        shipment.setShipmentid(shipmentId)
        shipment.setAssignedcarrier(carrierId)
        shipment.shipmentstops = stop_facilities.collect{shipmentUtil.update_facilities_and_stops_on_tlm_shipment(stop_facilities.indexOf(it) ,shipment,minimum_days_from_now,stop_facilities, stop_actions)}
        shipmentJson=shipment.buildjson()
        println("Shipment Json with 4 Stops =" + shipmentJson)

        //Hit POST /api/scshipment/shipment/Save API, and validate the Shipment creation in db
        shipmentUtil.createShipment(shipmentJson)
        shipmentUtil.assert_for_Shipment_Stop(shipmentId,carrierId,stop_facilities,stop_actions)
    }

       @Test(description= "To create Shipment with Order Note")
       public void createShipmentWithNotes() {
           def shipmentId='HAR_SHIPMENT_02'
           def carrierId='HAR_CARRIER_02'
           def stop_facilities=  ['FAC1', 'FAC2', 'FAC3', 'FAC4']
           def stop_actions = ['PU', 'DL', 'DL', 'DL']
           def noteType =['BOL','Shipping Label','PRO']
           def noteValue= ['BOL ID: BOL123','CNTR NUMBERS: LPN1,LPN2','PRO ID: PRO ID']
           def noteCode= ['BOL1','LABEL','PRO1']
           def noteVisibility=['All','Internal Only','Carrier']

           //Create Shipment Json with Shipment Level Notes
           shipment.setOrgid(orgId)
           shipment.setShipmentid(shipmentId)
           shipment.setAssignedcarrier(carrierId)
           shipment.shipmentstops = stop_facilities.collect{shipmentUtil.update_facilities_and_stops_on_tlm_shipment(stop_facilities.indexOf(it) ,shipment,minimum_days_from_now,stop_facilities, stop_actions)}
           shipment.shipmentnotes=noteVisibility.collect{shipmentUtil.update_shipment_note(noteVisibility.indexOf(it),noteType,noteValue,noteCode,noteVisibility,shipment)}
           shipmentNoteJson=shipment.buildShipmentNotejson()
           println("Shipment Json with Shipment Level Note is =" + shipmentNoteJson)

           //Create Note against a shipment
           shipmentUtil.createShipment(shipmentNoteJson)
           shipmentUtil.assert_for_Shipment_Level_Note(shipmentId,noteType,noteValue,noteCode,noteVisibility)
       }

        @Test(description= "Create an Order")
        public void createDistributionOrder()
        {
            def orderId='HAR_ORDER_01'
            def order_origin_facility='FAC1'
            def order_destination_facility='FAC4'
            def order_line_item=['HAR_ITEM01','HAR_ITEM02']

            //Create Order json
            order.setOrderid(orderId)
            order.setOriginfacilityid(order_origin_facility)
            order.setOriginfacilityname(order_origin_facility)
            order.setDestinationfacilityid(order_destination_facility)
            order.setDestinationfacilityname(order_destination_facility)
            commonUtil.update_order_timestamp(order,minimum_days_from_now)
            order.orderlines=order_line_item.collect{orderUtil.update_order_Lines(order_line_item.indexOf(it),order_line_item,order)}
            orderJson=order.buildjson()
            println("Distribution Order json =" + orderJson)

            //Create Order
            //orderUtil.createDistributionOrder(orderJson)
         }

        @Test(description= "RESET functionality of shipment json. Reset both stop and Order Movement")
        public void createShipmentWithResetLogic()
        {
            def shipmentId='HAR_SHIPMENT_04'
            def carrierId='HAR_CARRIER_04'
            def orders=['HAR_ORDER_41','HAR_ORDER_42']
            def stop_facilities=  ['FAC1', 'FAC2', 'FAC3', 'FAC4']
            def stop_actions = ['PU', 'DL', 'DL', 'DL']

            //Create Shipment Json with Reset Logic
            shipment.setOrgid(orgId)
            shipment.setShipmentid(shipmentId)
            shipment.setAssignedcarrier(carrierId)
            shipment.shipmentstops = stop_facilities.collect{shipmentUtil.update_facilities_and_stops_on_tlm_shipment(stop_facilities.indexOf(it) ,shipment,minimum_days_from_now,stop_facilities, stop_actions)}
            shipment.shipmentordermovements=orders.collect{shipmentUtil.update_order_movement(orders.indexOf(it),shipment,orders)}
            shipmentJson=shipment.buildShipmentjsonOrderMovement()
            println("Shipment Json with 4 Stops and 2 Order Movement=" + shipmentJson)

            //Hit POST /api/scshipment/shipment/Save API, and validate the Shipment creation in db
            shipmentUtil.createShipment(shipmentJson)
            shipmentUtil.assert_for_Shipment_Stop_OrdMov(shipmentId,carrierId,stop_facilities,stop_actions,orders)

            //shipment RESET LOGIC FOR STOP & ORDER MOVEMENT
            def resetOrders=['HAR_ORDER_41']
            def reset_stop_facilities=  ['FAC1', 'FAC2']
            def reset_stop_actions = ['PU', 'DL']
            shipment.shipmentstops = reset_stop_facilities.collect{shipmentUtil.update_facilities_and_stops_on_tlm_shipment(reset_stop_facilities.indexOf(it) ,shipment,minimum_days_from_now,reset_stop_facilities, reset_stop_actions)}
            shipment.shipmentordermovements=resetOrders.collect{shipmentUtil.update_order_movement(resetOrders.indexOf(it),shipment,orders)}
            shipmentJson=shipment.buildShipmentjsonReset()
            println("Shipment Reset Json with 2 Stops and 1 order movement =" + shipmentJson)

            //Hit POST /api/scshipment/shipment/Save API, and validate the Shipment creation in db
            shipmentUtil.createShipment(shipmentJson)
            shipmentUtil.assert_for_Shipment_Stop_OrdMov(shipmentId,carrierId,reset_stop_facilities,reset_stop_actions,resetOrders)

        }
 }
