import io.github.jmgarridopaz.bluezone.hexagon.ports.driven.forstoringtickets.ForStoringTickets;

module bluezone.adapter.forstoringtickets.filesystem {

	// DEPENDS ON
	requires bluezone.hexagon;
	requires io.github.jmgarridopaz.lib.portsadapters;
	requires com.fasterxml.jackson.datatype.jsr310;
	requires com.fasterxml.jackson.databind;

	// PUBLISHES
	exports io.github.jmgarridopaz.bluezone.adapter.forstoringtickets.filesystem;

	// SERVICES
	provides ForStoringTickets
	with io.github.jmgarridopaz.bluezone.adapter.forstoringtickets.filesystem.FilesystemTicketStoreAdapter;
	
}
