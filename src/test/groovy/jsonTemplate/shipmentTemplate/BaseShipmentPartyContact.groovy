package jsonTemplate.shipmentTemplate

class BaseShipmentPartyContact {
    def firstname
    def lastname
    def email
    def phone
    def address1
    def address2
    def address3
    def city
    def state
    def postalcode
    def county
    def country

    BaseShipmentPartyContact(){
        firstname = ''
        lastname = ''
        email = ''
        phone = ''
        address1 = ''
        address2 = ''
        address3 = ''
        city = ''
        state = ''
        postalcode = ''
        county = ''
        country = ''
    }
    def buildjson(parent)
    {
        parent."PartyContact"{
            FirstName this.firstname
            LastName this.lastname
            Email this.email
            Phone this.phone
            Address1 this.address1
            Address2 this.address2
            Address3 this.address3
            City this.city
            State this.state
            PostalCode this.postalcode
            County this.county
            Country this.country
        }
    }

}
