package test_data_models

import groovy.json.*

class BaseShipment {

    def orgid
    def shipmentid
    def assignedcarrier
    def transportationstatus
    def totalcost
    def totalcostcurrencyuom
    def shipmentactions = []
    def shipmentstops = []
    def shipmentordermovements = []
    def shipmentnotes =[]

    BaseShipment() {

        orgid = 'dummyOrg'
        shipmentid = '1234'
        assignedcarrier = 'CARR1'
        transportationstatus = 'Planned'
        totalcost = 50.0
        totalcostcurrencyuom = 'USD'
        shipmentactions =1.collect {new BaseShipmentActionReset()}
        shipmentstops = (1..2).collect{new BaseShipmentStop(orgid,shipmentid)}
        shipmentordermovements = 1.collect {new BaseShipmentOrderMovement(orgid,shipmentid)}
        shipmentnotes=1.collect {new BaseShipmentNote(orgid,shipmentid)}
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
            Stop(
                    this.shipmentstops.collect{it.buildjson(delegate)}
            )

        }
        return json.toPrettyString()
    }

    def buildShipmentjsonOrderMovement()
    {
        def json = new JsonBuilder()
        def root = json {
            Orgid this.orgid
            AssignedCarrier this.assignedcarrier
            ShipmentId this.shipmentid
            TransportationStatus this.transportationstatus
            TotalCost this.totalcost
            TotalCostCurrencyUOM this.totalcostcurrencyuom
            Stop(
                    this.shipmentstops.collect{it.buildjson(delegate)}
            )
            OrderMovement(
                    this.shipmentordermovements.collect {it.buildjson(delegate)}
            )
        }
        return json.toPrettyString()
    }

    def buildShipmentjsonReset()
    {
        def json = new JsonBuilder()
        def root = json {
            Orgid this.orgid
            Actions(
                    this.shipmentactions.collect {it.buildjson(delegate)}
            )
            AssignedCarrier this.assignedcarrier
            ShipmentId this.shipmentid
            TransportationStatus this.transportationstatus
            TotalCost this.totalcost
            TotalCostCurrencyUOM this.totalcostcurrencyuom
            Stop(
                    this.shipmentstops.collect{it.buildjson(delegate)}
            )
            OrderMovement(
                    this.shipmentordermovements.collect {it.buildjson(delegate)}
            )
        }
        return json.toPrettyString()
    }

    def buildShipmentNotejson()
    {
        def json = new JsonBuilder()
        def root = json {
            Orgid this.orgid
            AssignedCarrier this.assignedcarrier
            ShipmentId this.shipmentid
            TransportationStatus this.transportationstatus
            TotalCost this.totalcost
            TotalCostCurrencyUOM this.totalcostcurrencyuom
            Stop(
                    this.shipmentstops.collect{it.buildjson(delegate)}
            )

            ShipmentNote(
                    this.shipmentnotes.collect {it.buildjson(delegate)}
            )
        }
        return json.toPrettyString()
    }
}