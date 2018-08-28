package jsonTemplate.shipmentTemplate

import groovy.json.JsonBuilder

class BaseShipmentPartyQualiffier {
    def partyqualifierid
    def qualifierdescription

    BaseShipmentPartyQualiffier(){
        partyqualifierid = ''
        qualifierdescription = ''
    }

    def buildjson() {
        def json = new JsonBuilder()
        def root = json {
            PartyQualifierId this.partyqualifierid
            QualifierDescription this.qualifierdescription
        }
        return json.toPrettyString()
    }
}
