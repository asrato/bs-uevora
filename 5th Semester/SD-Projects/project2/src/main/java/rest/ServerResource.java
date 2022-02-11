package rest;

import jakarta.ws.rs.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Path(value = "/api")
public class ServerResource {

    private QueryManager queryManager;

    public ServerResource() {
        this.queryManager = new QueryManager();
    }

    @Path("/events")
    @GET
    @Produces({"application/json"})
    public synchronized List<Event> getEvents(@QueryParam("date") String date, @QueryParam("filter") String filter) {
        if (filter == null || filter.equals("")) {
            System.out.print("Getting all events " +
                    ((date == null || date.equals("")) ? "" : "in " + date) + "\n");
            return this.queryManager.searchEvents(date == null ? "" : date);
        }
        switch (filter) {
            case "future":
            case "past":
            case "now":
            case "past-and-now":
                System.out.println("Getting filtered events...");
                return this.queryManager.getFilteredEvents(filter);
            case "all":
                System.out.println("Getting all events...");
                return this.queryManager.searchEvents("");
            default:
                System.out.println("An error occurred while using the desired filter to get the events");
                return new ArrayList<>();
        }
    }

    @Path("/event")
    @POST
    @Consumes({"application/json"})
    public synchronized boolean postEvent(Event event) {
        System.out.println("Posting a new event");

        if (event.getName() != null && event.getType() != null && event.getDate() != null) {
            return this.queryManager.createEvent(event);
        } else {
            return false;
        }
    }

    @Path("/participants")
    @GET
    @Produces({"application/json"})
    public synchronized List<Participant> getParticipants(@QueryParam("name") String name) {
        System.out.println("Getting participants from event " + name);

        return this.queryManager.getParticipants(name);
    }

    @Path("/classification")
    @GET
    @Produces({"application/json"})
    public synchronized List<HashMap<String, String>> getClassification(@QueryParam("event") String event, @QueryParam("start") String start, @QueryParam("end") String end) {
        System.out.println("Getting classification of event " + event);
        System.out.println("Starting point -> " + start + "\nEnding point -> " + end);

        return this.queryManager.getClassification(event, start, end);
    }

    @Path("/at_point")
    @GET
    @Produces({"application/json"})
    public synchronized int getNumberOfParticipants(@QueryParam("event") String event, @QueryParam("location") String location) {
        System.out.println("Getting number of participants for event " + event + " at location " + location);

        return this.queryManager.getNumberOfParticipantsAtPoint(event, location);
    }

    @Path("/register")
    @POST
    @Produces({"application/json"})
    public synchronized int registerParticipant(Participant participant) {
        System.out.println("Registering " + participant.getName() + " in event " + participant.getEventName());

        int dorsal = this.queryManager.getLastDorsal(participant.getEventName()) + 1;
        int chipID = this.queryManager.getLastChipID() + 1;

        participant.setDorsal(dorsal);
        participant.setChipID(chipID);

        if (this.queryManager.registerParticipant(participant)) {
            return dorsal;
        }

        return -1;
    }

    @Path("/sensor/register_time")
    @POST
    @Produces({"application/json"})
    public synchronized boolean registerTime(Chip chip) {
        HashMap<String, String> pairs = new HashMap<>();
        pairs.put("p1", "start");
        pairs.put("p2", "p1");
        pairs.put("p3", "p2");
        pairs.put("finish", "p3");

        String[] event = this.queryManager.getEventByChipId(chip.getChipID());

        if (!this.queryManager.getChipIdByCurrentDate().contains(chip.getChipID())) {
            System.out.println("There is no participant with chip '" + chip.getChipID() + "' registered in today's events\n");
            return false;
        }

        List<String> locations = this.queryManager.getLocationsByChipId(chip.getChipID());

        if (chip.getSection().equals("start") || locations.contains(pairs.get(chip.getSection()))) {
            System.out.println("Registering a new time for chip " + chip.getChipID() + " at section '" + chip.getSection() + "' of the event " + event[0] + "\n");

            return this.queryManager.registerTime(chip, event[0]);
        } else {
            System.out.println("Participant needs to pass location '" + pairs.get(chip.getSection()) + "' before passing location '" + chip.getSection() + "'");
            return false;
        }
    }
}
