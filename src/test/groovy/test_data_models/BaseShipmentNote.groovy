package test_data_models

import groovy.json.*

class BaseShipmentNote {

    def noteseq
    def notetype
    def notevalue
    def notecode
    def notevisibility
    def shipmentid
    def orgid

    BaseShipmentNote(orgid, shipmentid)
    {
        this.orgid=orgid
        noteseq=''
        notetype=''
        notevalue=''
        notecode=''
        notevisibility=''
        this.shipmentid=shipmentid

    }

    def buildjson(parent) {

        parent."ShipmentNote" {
            NoteSeq this.noteseq
            NoteType this.notetype
            NoteValue this.notevalue
            NoteCode this.notecode
            NoteVisibility this.notevisibility
            ShipmentId this.shipmentid
            OrgId  this.orgid

        }
    }

}
