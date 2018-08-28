package jsonTemplate.shipmentTemplate

class BaseShipmentInvolvedParties {
    def orgid
    def shipmentid
    def involvedpartyid
    def partyqualifierid
    def partycontactcorp
    def partycontactlanguage
    def partycontact = []

    BaseShipmentInvolvedParties(orgid, shipmentid, partyqualifierid)
    {
        this.orgid = orgid
        this.shipmentid = shipmentid
        involvedpartyid = "1234"
        this.partyqualifierid = partyqualifierid
        partycontactcorp = ''
        partycontactlanguage = ''
        partycontact = new BaseShipmentPartyContact()
    }
    def buildjson(parent)
    {
        parent."InvolvedParties"{
            OrgId this.orgid
            ShipmentId this.shipmentid
            InvolvedPartyId this.involvedpartyid
            PartyContact this.partycontact.each{it.buildjson(delegate)}
            PartyQualifierId this.partyqualifierid
            PartyContactCorp this.partycontactcorp
            PartyContactLanguage this.partycontactlanguage
        }
    }
}
