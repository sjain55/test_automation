package jsonTemplate

import groovy.json.*

class BaseShipmentOrderMovement {


    def orgid
    def orderid
    def shipmentid
    def orderlegid
    def orderpickupseq
    def orderdeliveryseq


    BaseShipmentOrderMovement(orgid,shipmentid) {
        this.orgid = orgid
        orderid = ''
        this.shipmentid = shipmentid
        orderlegid = ''
        orderpickupseq = 1
        orderdeliveryseq = 2
    }

    def buildjson() {
        def json = new JsonBuilder()
        def root = json {

            OrgId this.orgid
            OrderId this.orderid
            ShipmentId this.shipmentid
            OrderLegId this.orderlegid
            OrderPickupSeq this.orderpickupseq
            OrderDeliverySeq this.orderdeliveryseq

        }
        return json.toPrettyString()
    }
}