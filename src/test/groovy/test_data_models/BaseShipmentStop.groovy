package test_data_models

import groovy.json.*

class BaseShipmentStop {

    def orgid
    def stopseq
    def shipmentid
    def stopfacilityid
    def stopfacilityname
    def stopaddressline1
    def stopaddressline2
    def stopaddressline3
    def stopcity
    def stopcounty
    def stopcountry
    def stopstateorprovince
    def stoppostalcode
    def stopaction
    def plannedarrivalstart
    def plannedarrivalend
    def plannerdeparturestart
    def planneddepartureend
    def actualarrivaltime
    def actualdeparturetime
    def estimatedarrivaltime
    def estimateddeparturetime


    BaseShipmentStop(orgid,shipmentid)
    {
        this.orgid=orgid
        stopseq=1
        this.shipmentid=shipmentid
        stopfacilityid='DC1'
        stopfacilityname=''
        stopaddressline1=''
        stopaddressline2=''
        stopaddressline3=''
        stopcity=''
        stopcounty=''
        stopcountry=''
        stopstateorprovince=''
        stoppostalcode=''
        stopaction='PU'
        plannedarrivalstart=''
        plannedarrivalend=''
        plannerdeparturestart=''
        planneddepartureend=''
        actualarrivaltime=''
        actualdeparturetime=''
        estimatedarrivaltime=''
        estimateddeparturetime=''
    }

    def buildjson() {
        def json = new JsonBuilder()
        def root = json {

            Orgid this.orgid
            StopSeq this.stopseq
            ShipmentId this.shipmentid
            StopFacilityId this.stopfacilityid
            StopFacilityName this.stopfacilityname
            StopAddressLine1 this.stopaddressline1
            StopAddressLine2 this.stopaddressline2
            StopAddressLine3 this.stopaddressline3
            StopCity this.stopcity
            StopCounty this.stopcounty
            StopCountry this.stopcountry
            StopStateOrProvince this.stopstateorprovince
            StopPostalCode this.stoppostalcode
            StopAction this.stopaction
            PlannedArrivalStart this.plannedarrivalstart
            PlannedArrivalEnd this.planneddepartureend
            PlannedDepartureStart this.plannerdeparturestart
            PlannedDepartureEnd this.planneddepartureend
            ActualArrivalTime this.actualarrivaltime
            ActualDepartureTime this.actualdeparturetime
            EstimatedArrivalTime this.estimatedarrivaltime
            EstimatedDepartureTime this.estimateddeparturetime
        }
        return json.toPrettyString()

    }



}
