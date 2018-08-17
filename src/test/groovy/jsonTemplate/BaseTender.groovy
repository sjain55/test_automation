package jsonTemplate

import groovy.json.*


class BaseTender {


    def shipmentid

    BaseTender()
    {
        shipmentid = '1234'
    }

    def buildjson()
    {
        def json = new JsonBuilder()
        def root = json {
            ShipmentId this.shipmentid }

        return json.toPrettyString()
    }

}
