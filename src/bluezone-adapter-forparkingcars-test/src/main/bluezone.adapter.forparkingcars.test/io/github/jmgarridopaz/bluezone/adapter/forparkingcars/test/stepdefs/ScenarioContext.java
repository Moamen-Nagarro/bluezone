package io.github.jmgarridopaz.bluezone.adapter.forparkingcars.test.stepdefs;

import java.time.Clock;
import java.util.Map;

import io.github.jmgarridopaz.bluezone.adapter.forparkingcars.test.sut.SutProvider;
import io.github.jmgarridopaz.bluezone.hexagon.driver.forparkingcars.ForParkingCars;
import io.github.jmgarridopaz.bluezone.hexagon.driver.forparkingcars.RateData;
import io.github.jmgarridopaz.bluezone.hexagon.permit.PermitTicket;

/**
 * Class for sharing state between different steps files in one scenario.
 * 
 * It is instantiated and injected into the stepdefs by PicoContainer (DI framework).
 * 
 * Put this class in stepdefs package to be recreated for every scenario.
 * 
 * So that there won't be state leakage between scenarios.
 */
public class ScenarioContext {

	private ForParkingCars forParkingCars;
	private Map<String,RateData> rates;
	private PermitTicket permitTicket;
	private Clock clock;

	public ScenarioContext() {
	}
	
	
	ForParkingCars forParkingCars() {
		return this.forParkingCars;
	}

	
	void setRates(Map<String,RateData> rates) {
		this.rates = rates;
	}

	Map<String, RateData> rates() {
		return this.rates;
	}


	void setPermitTicket(PermitTicket permitTicket) {
		this.permitTicket = permitTicket;
	}

	PermitTicket permitTicket() {
		return this.permitTicket;
	}

	
	void setClock(Clock clock) {
		this.clock = clock;		
	}

	Clock clock() {
		return this.clock;
	}
	
}
