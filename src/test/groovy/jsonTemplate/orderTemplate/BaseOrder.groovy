package jsonTemplate.orderTemplate

import groovy.json.JsonBuilder

class BaseOrder {

    def orderid
    def orgid
    def fulfillmentstatus
    def transportationstatus
    def designatedmodeid
    def designatedcarrierid
    def designatedservicelevelid
    def designatedequipmentid
    def designatedtractorid
    def designateddrivertype
    def isbackordered
    def destinationcity
    def destinationcounty
    def destinationcountry
    def destinationstateprovince
    def destinationpostalcode
    def destinationfacilityid
    def destinationfacilityname
    def originfacilityid
    def originfacilityname
    def originaddressline1
    def originaddressline2
    def originaddressline3
    def origincity
    def origincounty
    def origincountry
    def originstateorprovince
    def originpostalcode
    def productclassid
    def protectionlevelid
    def deliveryenddatetime
    def deliverydtartdatetime
    def pickupenddatetime
    def pickupstartdatetime
    def pipelineid
    def designatedrateid
    def orderlines=[]

    BaseOrder() {

        orderid='O1234'
        orgid='1'
        fulfillmentstatus
        transportationstatus
        designatedmodeid
        designatedcarrierid== 'CARR1'
        designatedservicelevelid
        designatedequipmentid
        designatedtractorid
        designateddrivertype
        isbackordered
        destinationcity
        destinationcounty
        destinationcountry
        destinationstateprovince
        destinationpostalcode
        destinationfacilityid
        destinationfacilityname
        originfacilityid
        originfacilityname
        originaddressline1
        originaddressline2
        originaddressline3
        origincity
        origincounty
        origincountry
        originstateorprovince
        originpostalcode
        productclassid
        protectionlevelid
        deliveryenddatetime
        deliverydtartdatetime
        pickupenddatetime
        pickupstartdatetime
        pipelineid
        designatedrateid
        orderlines=1.collect {new BaseOrderLine(orgid,orderid)}
    }

    def buildjson()
    {
        def json = new JsonBuilder()
        def root = json {
            OrderId this.orderid
            OrgId this.orgid
            FulfillmentStatus this.fulfillmentstatus
            TransportationStatus this.transportationstatus
            DesignatedModeId this.designatedmodeid
            DesignatedCarrierId this.designatedcarrierid
            DesignatedServiceLevelId this.designatedservicelevelid
            DesignatedEquipmentId this.designatedequipmentid
            DesignatedTractorId this.designatedtractorid
            DesignatedDriverType this.designateddrivertype
            IsBackOrdered this.isbackordered
            DestinationCity this.destinationcity
            DestinationCounty this.destinationcounty
            DestinationCountry this.destinationcountry
            DestinationStateOrProvince this.destinationstateprovince
            DestinationPostalCode this.destinationpostalcode
            DestinationFacilityId this.destinationfacilityid
            DestinationFacilityName this.destinationfacilityname
            OriginFacilityId this.originfacilityid
            OriginFacilityName this.originfacilityname
            OriginAddressLine1 this.originaddressline1
            OriginAddressLine2 this.originaddressline2
            OriginAddressLine3 this.originaddressline3
            OriginCity this.origincity
            OriginCounty this.origincounty
            OriginCountry this.origincountry
            OriginStateOrProvince this.originstateorprovince
            OriginPostalCode this.originpostalcode
            ProductClassId this.productclassid
            ProtectionLevelId this.protectionlevelid
            DeliveryEndDateTime this.deliveryenddatetime
            DeliveryStartDateTime this.deliverydtartdatetime
            PickupEndDateTime this.pickupenddatetime
            PickupStartDateTime this.pickupstartdatetime
            PipelineId this.pipelineid
            DesignatedRateId this.designatedrateid

            OrderLine(
                    this.orderlines.collect{it.buildjson(delegate)}
            )

        }
        return json.toPrettyString()
    }
}