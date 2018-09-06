package jsonTemplate.tenderTemplate

import groovy.json.JsonBuilder

class BaseTenderReject {

    def shipmentid
    def carrierid
    def reasoncode

    BaseTenderReject()
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
