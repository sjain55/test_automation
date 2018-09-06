package jsonTemplate.tenderTemplate


import groovy.json.JsonBuilder

class BaseTenderConfig {

    def tenderautoaccept


    BaseTenderConfig()
    {
        tenderautoaccept = 'false'

    }

    def buildjson()
    {
        def json = new JsonBuilder()
        def root = json {
            TenderAutoAccept this.tenderautoaccept}

        return json.toPrettyString()
    }

}

