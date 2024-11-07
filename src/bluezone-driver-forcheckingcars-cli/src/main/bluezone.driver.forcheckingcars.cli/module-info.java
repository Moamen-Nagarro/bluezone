module io.github.jmgarridopaz.bluezone.driver.forcheckingcars.cli {

	// DEPENDS ON
	requires bluezone.hexagon; // The module for the hexagonal architecture
	requires io.github.jmgarridopaz.lib.portsadapters; // The module for port adapters
//	requires org.testng; // TestNG for testing framework
//	requires org.hamcrest;
	requires java.logging;

	// PUBLISHES
	exports io.github.jmgarridopaz.bluezone.driver.forcheckingcars.cli; // Exporting the CLI package
}
