package jsonTemplate.tenderTemplate

import groovy.json.*

class BaseAccept {

    def shipmentid
    def carrierid

    BaseAccept()
    {
        shipmentid = '1234'
        carrierid = 'CARR1'
    }

    def buildjson()
    {
        def json = new JsonBuilder()
        def root = json {
            ShipmentId this.shipmentid
            CarrierId this.carrierid }

        return json.toPrettyString()
    }

}