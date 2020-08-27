package it.polito.ezgas;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ EntityAndDtoUnitTests.class, EZGasApplicationTests.class, GasStationConverterTests.class,
        GasStationServiceResponseTimeTests.class, GasStationServiceTests.class, UserConverterTests.class,
        UserServiceResponseTimeTests.class, UserServiceTests.class })
public class AllTests {

}
