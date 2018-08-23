package jsonTemplate


import groovy.json.JsonBuilder

class BaseOrderLine {

    def pipelinestatus
    def unitweight
    def protectionlevelid
    def unitvolume
    def productclassid
    def orderid
    def orderlineid
    def originalorderedquantity
    def orderedquantity
    def itemid
    def weight
    def weightuom
    def volume
    def volumeuom
    def orgid

    BaseOrderLine(orgid,orderid) {
        pipelinestatus
        unitweight='1'
        protectionlevelid
        unitvolume='1'
        productclassid=''
        this.orderid=orderid
        orderlineid='1'
        originalorderedquantity='100'
        orderedquantity='100'
        itemid='HAR_ITEM1'
        weight='100'
        weightuom='lb'
        volume='100'
        volumeuom='cuft'
        this.orgid=orgid
    }
    def buildjson(parent) {

        parent."OrderLine" {
            PipelineStatus this.pipelinestatus
            UnitWeight this.unitweight
            ProtectionLevelId this.protectionlevelid
            UnitVolume this.unitvolume
            ProductClassId this.productclassid
            OrderId this.orderid
            OrderLineId this.orderlineid
            OriginalOrderedQuantity this.originalorderedquantity
            OrderedQuantity this.orderedquantity
            ItemId this.itemid
            Weight this.weight
            WeightUOM this.weightuom
            Volume this.volume
            VolumeUOM this.volumeuom
        }
    }

}