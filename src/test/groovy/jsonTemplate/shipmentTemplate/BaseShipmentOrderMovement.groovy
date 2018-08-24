package jsonTemplate.shipmentTemplate

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
        orderlegid = 0
        orderpickupseq = 1
        orderdeliveryseq = 2
    }

    def buildjson(parent) {

        parent."OrderMovement" {
            OrgId this.orgid
            OrderId this.orderid
            ShipmentId this.shipmentid
            OrderLegId this.orderlegid
            OrderPickupSeq this.orderpickupseq
            OrderDeliverySeq this.orderdeliveryseq

        }
    }
}