import io.github.jmgarridopaz.bluezone.hexagon.ports.driven.forobtainingrates.ForObtainingRates;
import io.github.jmgarridopaz.bluezone.hexagon.ports.driven.forpaying.ForPaying;
import io.github.jmgarridopaz.bluezone.hexagon.ports.driven.forstoringtickets.ForStoringTickets;

module bluezone.startup {

	// DEPENDS ON
	// all the other modules
	requires bluezone.driver.forparkingcars.test;
//	requires bluezone.driver.forcheckingcars.test;
//	requires bluezone.driver.forcheckingcars.cli;
	requires bluezone.adapter.forparkingcars.webui;
	requires bluezone.adapter.forobtainingrates.stub;
	requires bluezone.adapter.forstoringtickets.fake;
	requires bluezone.adapter.forpaying.spy;
    requires io.github.jmgarridopaz.bluezone.driver.forcheckingcars.cli;
    requires io.github.jmgarridopaz.lib.portsadapters;
    requires bluezone.hexagon;
    requires io.github.jmgarridopaz.lib.javalangutils;

    // SERVICES
	uses ForObtainingRates;
	uses ForStoringTickets;
	uses ForPaying;

}
