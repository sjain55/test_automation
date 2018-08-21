package jsonTemplate

class BaseShipmentActionReset {

    def stop
    def ordermovement

    BaseShipmentActionReset()
    {
        stop='RESET'
        ordermovement='RESET'
    }

    def buildjson(parent) {

        parent."Actions" {

            Stop this.stop
            OrderMovement this.ordermovement

        }
    }



}
