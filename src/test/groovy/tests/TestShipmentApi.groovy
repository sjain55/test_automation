package tests

import org.testng.annotations.Test
import test_data_models.BaseShipment
import test_data_models.BaseShipmentOrderMovement
import test_data_models.BaseShipmentStop

class TestShipmentApi {

    BaseShipment shipment

    TestShipmentApi()
    {
        shipment = new BaseShipment()
    }

    @Test
    public void createShipment()
    {
        shipment.setOrgid('123')
        shipment.setShipmentid('Ship123')
        //shipment.buildjson()
        println("Json =" + shipment.buildjson())
    }


}
