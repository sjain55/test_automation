package jsonTemplate.tenderTemplate


import groovy.json.JsonBuilder

class BaseReset {

    def shipmentid
    def reasoncode

    BaseReset()
    {
        shipmentid = ''
        reasoncode = ''
    }

    def buildjson()
    {
        def json = new JsonBuilder()
        def root = json {
            ShipmentId this.shipmentid
            ReasonCode this.reasoncode }

        return json.toPrettyString()
    }

}

