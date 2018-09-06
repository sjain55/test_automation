package jsonTemplate.tenderTemplate

import groovy.json.JsonBuilder

class BaseTenderRecall {

    def shipmentid
    def carrierid
    def reasoncode

    BaseTenderRecall()
    {
        shipmentid = ''
        reasoncode = ''
        carrierid = ''
    }

    def buildjson()
    {
        def json = new JsonBuilder()
        def root = json {
            ShipmentId this.shipmentid
            CarrierId this.carrierid
            ReasonCode this.reasoncode
        }

        return json.toPrettyString()
    }
}
