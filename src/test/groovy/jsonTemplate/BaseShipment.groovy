package jsonTemplate

import groovy.json.*

class BaseShipment {

    def orgid
    def shipmentid
    def assignedcarrier
    def transportationstatus
    def totalcost
    def totalcostcurrencyuom
    def shipmentstops = []
    def shipmentordermovements = []

    BaseShipment() {

        orgid = 'dummyOrg'
        shipmentid = '1234'
        assignedcarrier = 'CARR1'
        transportationstatus = 'Planned'
        totalcost = 50.0
        totalcostcurrencyuom = 'USD'
        shipmentstops = (1..2).collect{new BaseShipmentStop(orgid,shipmentid)}
        shipmentordermovements = 1.collect {new BaseShipmentOrderMovement(orgid,shipmentid)}
    }

    def buildjson()
    {
       def json = new JsonBuilder()

        def root = json {
            Orgid this.orgid
            AssignedCarrier this.assignedcarrier
            ShipmentId this.shipmentid
            TransportationStatus this.transportationstatus
            TotalCost this.totalcost
            TotalCostCurrencyUOM this.totalcostcurrencyuom
            Stop (this.shipmentstops).collect{it.buildjson()}
            OrderMovement (this.shipmentordermovements).collect {it.buildjson()}
        }

        return json.toPrettyString()
    }
}