package spring.boot.security.jdbc.auth.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class SpringSecurityController {

    @GetMapping("/")
    public String defaultPage(Model model, HttpServletRequest request) throws Exception {
        if (request.getUserPrincipal() == null) {
            model.addAttribute("session", "<a href=\"/login\">Sign in</a> | <a href=\"/newuser\">Register</a>");
            model.addAttribute("operations", "<a href=\"/previous_events?page=1\">Previous events</a> <br>"
                    + "<a href=\"/live_events?page=1\">Live events</a> <br>"
                    + "<a href=\"/future_events?page=1\">Future events</a> <br>"
                    + "<a href=\"/search_events\">Search events</a> <br>");
        } else {
            String username = request.getRemoteUser();
            String name = new RegisterManager(username).getNameByUsername(username);
            String role = new RegisterManager(username).getRole();
            model.addAttribute("session", name + ", <a href=\"/logout\">Logout</a>");

            if (role.equals("ROLE_STAFF")) {
                model.addAttribute("operations", "<a href=\"/newevent\">Register an event</a> <br>" +
                        "<a href=\"/newtime\">Register a time for participant</a> <br>" +
                        "<a href=\"/previous_events?page=1\">Previous events</a> <br>"
                        + "<a href=\"/live_events?page=1\">Live events</a> <br>"
                        + "<a href=\"/future_events?page=1\">Future events</a> <br>"
                        + "<a href=\"/search_events\">Search events</a> <br>");
            } else {
                model.addAttribute("operations", "<a href=\"/newinscricao\">Join an event</a> <br>"
                        + "<a href=\"/inscricoes_atleta\">Joined events</a> <br>" +
                        "<a href=\"/previous_events?page=1\">Previous events</a> <br>"
                        + "<a href=\"/live_events?page=1\">Live events</a> <br>"
                        + "<a href=\"/future_events?page=1\">Future events</a> <br>"
                        + "<a href=\"/search_events\">Search events</a> <br>");
            }
        }
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(Model model, @RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout) {
        if (error != null) {
            model.addAttribute("error", "Invalid Credentials");
        }

        return "login";
    }

    @GetMapping("/logout")
    public String logoutPage(Model model, HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";
    }

    @GetMapping("/newuser")
    public String newuser(Model model) {
        return "newuser";
    }

    @GetMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String real_name,
                               @RequestParam String genero,
                               @RequestParam String escalao,
                               Model model) throws Exception {

        model.addAttribute("title", "registration page");

        RegisterManager a = new RegisterManager(username, password, real_name, genero, escalao);
        int res = a.RegisterName();
        if (res == 1) {
            model.addAttribute("op", "User Registration");
            model.addAttribute("success", "<p class=\"success\">success</p>");
        } else {
            model.addAttribute("op", "User Registration");
            model.addAttribute("success", "<p class=\"error\">failed</p>");
            model.addAttribute("reason", "<p>Username is taken</p>");
        }

        return "confirmations";
    }

    @GetMapping("/newinscricao")
    public String newInscricao(Model model, HttpServletRequest request) throws Exception {
        String username = request.getRemoteUser();
        RegisterManager registerManager = new RegisterManager(username);

        StringBuilder builder = new StringBuilder();

        JSONArray events = registerManager.getEventsToJoin();

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);

        if (events.length() == 0) {
            builder.append("<p>There are no events to join</p>");
        } else {
            builder.append("<div class=\"events_list\">");

            for (int i = 0; i < events.length(); i++) {
                JSONObject event = events.getJSONObject(i);
                builder.append("<div class=\"event\"> <div class=\"event-info\">");

                builder.append("<p class=\"event-name\">").append(event.getString("event")).append("</p>");
                builder.append("<p class=\"event-price\">").append(df.format(Float.parseFloat(event.getString("price")))).append("&euro;</p>");
                builder.append("<p class=\"event-date\">").append(event.getString("date")).append("</p>");

                builder.append("</div><form class=\"join-button\" action=\"/registerChallenge\" method=\"GET\">" +
                                "<input type=\"hidden\" name=\"nome_evento\" value=\"").append(event.getString("event"))
                        .append("\"><button class=\"join\">JOIN</button></form></div>");
            }

            builder.append("</div>");
        }

        model.addAttribute("events", builder.toString());

        return "newinscricao";
    }

    @GetMapping("/registerChallenge")
    public String registerChallenge(@RequestParam String nome_evento,
                                    Model model, HttpServletRequest request) throws Exception {

        String nome = request.getRemoteUser();

        model.addAttribute("title", "registration page");

        RegisterManager a = new RegisterManager(nome, nome_evento);
        JSONObject res = a.RegisterInscricao();

        if (res != null) {
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            df.setMinimumFractionDigits(2);
            model.addAttribute("op", "Checkout");
            model.addAttribute("success", "<p class=\"success\">success</p><br><p>Dorsal #" + res.getInt("dorsal") + "</p>");
            model.addAttribute("payment", "<div class=\"payment-details\"><h3>Payment details</h3>"
                    + "<div class=\"payment-content\">"
                    + "<p class=\"payment-param\">Entity: " + res.getString("entity") + "</p>"
                    + "<p class=\"payment-param\">Reference: " + res.getString("reference") + "</p>"
                    + "<p class=\"payment-param\">Amount: " + df.format(Float.parseFloat(res.getString("value"))) + "&euro;</p></div>/div>");
        } else {
            model.addAttribute("op", "Checkout");
            model.addAttribute("success", "<p class=\"error\">failed</p>");
            model.addAttribute("reason", "<p>An error occurred while join the event</p>");
        }
        return "confirmations";
    }

    @GetMapping("/newevent")
    public String newEvent(Model model) {
        model.addAttribute("message", "fill details");
        return "newevent";
    }

    @GetMapping("/registerevent")
    public String registerEvent(@RequestParam String eventname, @RequestParam String eventdate,
                                @RequestParam String description,
                                @RequestParam String value,
                                Model model) throws Exception {
        RegisterManager a = new RegisterManager(eventname, description, value);
        int res = a.RegisterEvent(eventdate);
        //${op} {sucess} ${reason}
        if (res == 1) {
            model.addAttribute("op", "Event Registration");
            model.addAttribute("success", "<p class=\"success\">success</p>");
        } else {
            model.addAttribute("op", "Event Registration");
            model.addAttribute("success", "<p class=\"error\">failed</p>");
            model.addAttribute("reason", "<p>There is already an event with the same name</p>");
        }
        return "confirmations";
    }

    @GetMapping("/inscricoes_atleta")
    public String showInscricoes(Model model, HttpServletRequest request) throws Exception {
        String username = request.getRemoteUser();
        RegisterManager registerManager = new RegisterManager(username);

        model.addAttribute("nome_proprio", registerManager.getNameByUsername(username));

        StringBuilder builder = new StringBuilder();

        JSONArray paidArray = registerManager.getPaid(username);
        JSONArray unpaidArray = registerManager.getUnpaid(username);

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);

        if (paidArray.length() == 0 && unpaidArray.length() == 0) {
            builder.append("<p>no events</p>");
        } else {
            builder.append("<div class=\"events_list\">");

            for (int i = 0; i < unpaidArray.length(); i++) {
                JSONObject event = unpaidArray.getJSONObject(i);
                builder.append("<div class=\"event\"> <div class=\"event-info\">");

                builder.append("<p class=\"event-name\">").append(event.getString("event")).append("</p>");
                builder.append("<p class=\"event-price\">").append(df.format(Float.parseFloat(event.getString("price")))).append("&euro;</p>");
                builder.append("<p class=\"event-date\">").append(event.getString("date")).append("</p>");

                builder.append("</div><div class=\"payment-info\"><form class=\"payment-button\" action=\"/payment\" method=\"GET\"><input type=\"hidden\" name=\"event\" value=\"").append(event.getString("event")).append("\"><button class=\"payment-status-red\">WAITING PAYMENT</button></form></div></div></div>");
            }

            for (int i = 0; i < paidArray.length(); i++) {
                JSONObject event = paidArray.getJSONObject(i);
                builder.append("<div class=\"event\"> <div class=\"event-info\">");

                builder.append("<p class=\"event-name\">").append(event.getString("event")).append("</p>");
                builder.append("<p class=\"event-price\">").append(df.format(Float.parseFloat(event.getString("price")))).append("&euro;</p>");
                builder.append("<p class=\"event-date\">").append(event.getString("date")).append("</p>");

                builder.append("</div><div class=\"payment-info\"><div class=\"payment-status-green\"></div></div></div>");
            }
            builder.append("</div>");
        }

        model.addAttribute("events", builder.toString());

        return "inscricoes_atleta";
    }

    @GetMapping("/newtime")
    public String newTime(Model model) {
        model.addAttribute("message", "fill details");
        return "newtime";
    }

    @GetMapping("/registertime") // eventname dorsal timestamp section
    public String registerTime(@RequestParam String eventname, @RequestParam String dorsal,
                               @RequestParam String timestamp,
                               @RequestParam String section,
                               Model model) throws Exception {

        RegisterManager a = new RegisterManager(dorsal, eventname, timestamp, section);
        int res = a.RegisterTime();

        if (res == 1) {
            model.addAttribute("op", "Time Registration");
            model.addAttribute("success", "<p class=\"success\">success</p>");
        } else if (res == -1) {
            model.addAttribute("op", "Time Registration");
            model.addAttribute("success", "<p class=\"error\">failed</p>");
            model.addAttribute("reason", "<p>Dorsal didn't participate in the event</p>");
        } else if (res == -2) {
            model.addAttribute("op", "Time Registration");
            model.addAttribute("success", "<p class=\"error\">failed</p>");
            model.addAttribute("reason", "<p>This section already has a defined time</p>");
        } else if (res == -3) {
            model.addAttribute("op", "Time Registration");
            model.addAttribute("success", "<p class=\"error\">failed</p>");
            model.addAttribute("reason", "<p>The previous section doesn't have a defined time</p>");
        } else {
            model.addAttribute("op", "Registration in event");
            model.addAttribute("success", "<p class=\"error\">failed</p>");
            model.addAttribute("reason", "<p>The user didn't pay to join the event</p>");
        }
        return "confirmations";
    }

    @GetMapping("/error")
    public String treatError(Model model) {
        return "error";
    }

    @GetMapping("/payment")
    public String payment(@RequestParam String event, HttpServletRequest request, Model model) throws Exception {
        String username = request.getRemoteUser();
        RegisterManager registerManager = new RegisterManager(username, event);

        JSONObject paymentDetails = registerManager.getPaymentDetails();

        String payment = "<div class=\"payment-details\"><div class=\"full-width\">"
                + "<div class=\"payment-content\">"
                + "<p class=\"payment-param\">Entity: " + paymentDetails.getString("entity") + "</p>"
                + "<p class=\"payment-param\">Reference: " + paymentDetails.getString("reference") + "</p>"
                + "<p class=\"payment-param\">Amount: " + paymentDetails.getString("value") + "&euro;</p></div></div>";

        model.addAttribute("payment", payment);
        model.addAttribute("event", event);

        return "payment";
    }

    @GetMapping("/confirmPayment")
    public String confirmPayment(@RequestParam String event, HttpServletRequest request, Model model) throws Exception {
        String username = request.getRemoteUser();
        RegisterManager registerManager = new RegisterManager(username, event);

        registerManager.pay();

        model.addAttribute("op", "Checkout");
        model.addAttribute("success", "<p class=\"success\">success</p>");

        return "confirmations";
    }

    @GetMapping("/previous_events")
    public String previousEvents(@RequestParam String page, Model model) throws Exception {
        int pageNumber = Integer.parseInt(page);

        RegisterManager registerManager = new RegisterManager();

        JSONArray events = registerManager.getEvents("previous");

        int numberOfPages = 0;

        if (events.length() % 4 == 0) {
            numberOfPages = (int) Math.floor(events.length() / 4);
        } else {
            numberOfPages = (int) Math.floor(events.length() / 4) + 1;
        }

        StringBuilder builder = new StringBuilder();

        builder.append("<div class=\"pag-events-list\">");

        if (events.length() == 0) {
            if (pageNumber < 1) {
                throw new Exception();
            }
            builder.append("<h4>There are no previous events to display!</h4>");

            model.addAttribute("events", builder.toString());
        } else {
            if (pageNumber < 1 || pageNumber > numberOfPages) {
                throw new Exception();
            }

            builder.append("<div class=\"cards-container\">");

            for (int i = (pageNumber - 1) * 4; i < pageNumber * 4 && i < events.length(); i++) {

                JSONObject event = events.getJSONObject(i);

                builder.append("<div class=\"event-card\">");

                builder.append("<p>").append(event.getString("event")).append("</p>");
                builder.append("<p>").append(event.getString("date")).append("</p>");

                builder.append("<form action=\"/event_info\" method=\"GET\"><input type=\"hidden\" name=\"event\" value=\"").append(event.getString("event")).append("\"><button type=\"submit\">Details</button></form>");

                builder.append("</div>");
            }

            builder.append("</div></div>");

            model.addAttribute("events", builder.toString());

            builder = new StringBuilder();

            if (pageNumber == 1 && numberOfPages == 1) {
                builder.append("");
            } else if (pageNumber == 1) {
                builder.append("<div class=\"pagination\"><form action=\"/previous_events\" method=\"GET\"><input type=\"hidden\" name=\"page\" value=\"").append((pageNumber + 1)).append("\"><button type=\"submit\">Next</button></form></div>");
            } else if (pageNumber == numberOfPages) {
                builder.append("<div class=\"pagination\"><form action=\"/previous_events\" method=\"GET\"><input type=\"hidden\" name=\"page\" value=\"").append((pageNumber - 1)).append("\"><button type=\"submit\">Previous</button></form></div>");
            } else {
                builder.append("<div class=\"pagination\"><form action=\"/previous_events\" method=\"GET\"><input type=\"hidden\" name=\"page\" value=\"").append((pageNumber - 1)).append("\"><button style=\"margin-right:10px\" type=\"submit\">Previous</button></form>");
                builder.append("<form action=\"/previous_events\" method=\"GET\"><input type=\"hidden\" name=\"page\" value=\"").append((pageNumber + 1)).append("\"><button style=\"margin-left:10px\" type=\"submit\">Next</button></form></div>");
            }

            model.addAttribute("buttons", builder.toString());
        }

        return "previous_events";
    }

    @GetMapping("/future_events")
    public String futureEvents(@RequestParam String page, Model model) throws Exception {
        int pageNumber = Integer.parseInt(page);

        RegisterManager registerManager = new RegisterManager();

        JSONArray events = registerManager.getEvents("future");

        int numberOfPages = 0;

        if (events.length() % 4 == 0) {
            numberOfPages = (int) Math.floor(events.length() / 4);
        } else {
            numberOfPages = (int) Math.floor(events.length() / 4) + 1;
        }

        StringBuilder builder = new StringBuilder();

        builder.append("<div class=\"pag-events-list\">");

        if (events.length() == 0) {
            if (pageNumber < 1) {
                throw new Exception();
            }
            builder.append("<h4>There are no future events to display!</h4>");

            model.addAttribute("events", builder.toString());
        } else {
            if (pageNumber < 1 || pageNumber > numberOfPages) {
                throw new Exception();
            }

            builder.append("<div class=\"cards-container\">");

            for (int i = (pageNumber - 1) * 4; i < pageNumber * 4 && i < events.length(); i++) {

                JSONObject event = events.getJSONObject(i);

                builder.append("<div class=\"event-card\">");

                builder.append("<p>").append(event.getString("event")).append("</p>");
                builder.append("<p>").append(event.getString("date")).append("</p>");

                builder.append("<form action=\"/event_info\" method=\"GET\"><input type=\"hidden\" name=\"event\" value=\"").append(event.getString("event")).append("\"><button type=\"submit\">Details</button></form>");

                builder.append("</div>");
            }

            builder.append("</div></div>");

            model.addAttribute("events", builder.toString());

            builder = new StringBuilder();

            if (pageNumber == 1 && numberOfPages == 1) {
                builder.append("");
            } else if (pageNumber == 1) {
                builder.append("<div class=\"pagination\"><form action=\"/previous_events\" method=\"GET\"><input type=\"hidden\" name=\"page\" value=\"").append((pageNumber + 1)).append("\"><button type=\"submit\">Next</button></form></div>");
            } else if (pageNumber == numberOfPages) {
                builder.append("<div class=\"pagination\"><form action=\"/previous_events\" method=\"GET\"><input type=\"hidden\" name=\"page\" value=\"").append((pageNumber - 1)).append("\"><button type=\"submit\">Previous</button></form></div>");
            } else {
                builder.append("<div class=\"pagination\"><form action=\"/previous_events\" method=\"GET\"><input type=\"hidden\" name=\"page\" value=\"").append((pageNumber - 1)).append("\"><button style=\"margin-right:10px\" type=\"submit\">Previous</button></form>");
                builder.append("<form action=\"/previous_events\" method=\"GET\"><input type=\"hidden\" name=\"page\" value=\"").append((pageNumber + 1)).append("\"><button style=\"margin-left:10px\" type=\"submit\">Next</button></form></div>");
            }

            model.addAttribute("buttons", builder.toString());
        }

        return "future_events";
    }

    @GetMapping("/live_events")
    public String liveEvents(@RequestParam String page, Model model) throws Exception {
        int pageNumber = Integer.parseInt(page);

        RegisterManager registerManager = new RegisterManager();

        JSONArray events = registerManager.getEvents("");

        int numberOfPages = 0;

        if (events.length() % 4 == 0) {
            numberOfPages = (int) Math.floor(events.length() / 4);
        } else {
            numberOfPages = (int) Math.floor(events.length() / 4) + 1;
        }

        StringBuilder builder = new StringBuilder();

        builder.append("<div class=\"pag-events-list\">");

        if (events.length() == 0) {
            if (pageNumber < 1) {
                throw new Exception();
            }
            builder.append("<h4>There are no live events to display!</h4>");

            model.addAttribute("events", builder.toString());
        } else {
            if (pageNumber < 1 || pageNumber > numberOfPages) {
                throw new Exception();
            }

            builder.append("<div class=\"cards-container\">");

            for (int i = (pageNumber - 1) * 4; i < pageNumber * 4 && i < events.length(); i++) {

                JSONObject event = events.getJSONObject(i);

                builder.append("<div class=\"event-card\">");

                builder.append("<p>").append(event.getString("event")).append("</p>");
                builder.append("<p>").append(event.getString("date")).append("</p>");

                builder.append("<form action=\"/event_info\" method=\"GET\"><input type=\"hidden\" name=\"event\" value=\"").append(event.getString("event")).append("\"><button type=\"submit\">Details</button></form>");
                builder.append("<form action=\"/track_participants\" method=\"GET\"><input type=\"hidden\" name=\"event\" value=\"").append(event.getString("event")).append("\"><button type=\"submit\">Track Participants</button></form>");

                builder.append("</div>");
            }

            builder.append("</div></div>");

            model.addAttribute("events", builder.toString());

            builder = new StringBuilder();

            if (pageNumber == 1 && numberOfPages == 1) {
                builder.append("");
            } else if (pageNumber == 1) {
                builder.append("<div class=\"pagination\"><form action=\"/previous_events\" method=\"GET\"><input type=\"hidden\" name=\"page\" value=\"").append((pageNumber + 1)).append("\"><button type=\"submit\">Next</button></form></div>");
            } else if (pageNumber == numberOfPages) {
                builder.append("<div class=\"pagination\"><form action=\"/previous_events\" method=\"GET\"><input type=\"hidden\" name=\"page\" value=\"").append((pageNumber - 1)).append("\"><button type=\"submit\">Previous</button></form></div>");
            } else {
                builder.append("<div class=\"pagination\"><form action=\"/previous_events\" method=\"GET\"><input type=\"hidden\" name=\"page\" value=\"").append((pageNumber - 1)).append("\"><button style=\"margin-right:10px\" type=\"submit\">Previous</button></form>");
                builder.append("<form action=\"/previous_events\" method=\"GET\"><input type=\"hidden\" name=\"page\" value=\"").append((pageNumber + 1)).append("\"><button style=\"margin-left:10px\" type=\"submit\">Next</button></form></div>");
            }

            model.addAttribute("buttons", builder.toString());
        }

        return "live_events";
    }

    @GetMapping("/event_info")
    public String eventInfo(@RequestParam String event, Model model) throws Exception {
        model.addAttribute("event", StringUtils.capitalize(event));

        RegisterManager registerManager = new RegisterManager(event);
        JSONObject eventInfo = registerManager.getEventInfo();

        String info = "<p>Description: " + eventInfo.getString("description") + "</p><br>" +
                "<p>Date: " + eventInfo.getString("date") + "</p><br>" +
                "<p>Price to join: " + eventInfo.getString("price") + "&euro;</p><br>";

        model.addAttribute("info", info);

        JSONArray participants = registerManager.getParticipants();

        if (participants.length() > 0) {

            StringBuilder builder = new StringBuilder();

            builder.append("<div class=\"participants\">");

            for (int i = 0; i < participants.length(); i++) {
                JSONObject participant = participants.getJSONObject(i);

                builder.append("<div class=\"participant\">");

                builder.append("<div style=\"width: 10%\"><p style=\"text-align: right\">").append(participant.getInt("dorsal")).append("</p></div><div style=\"width: 50%\"><p>")
                        .append(participant.getString("name")).append("</p></div><div style=\"width: 30%\"><p style=\"text-align\": left\">")
                        .append(participant.getString("tier").toUpperCase()).append("</div></p>");

                if (participant.getBoolean("paid")) {
                    builder.append("<div class=\"payment-status-green\"/>");
                } else {
                    builder.append("<div class=\"payment-status-fail\"/>");
                }

                builder.append("</div></div>");
            }

            builder.append("</div>");

            model.addAttribute("participants", builder.toString());

            int integerDate = Integer.parseInt(eventInfo.getString("date").replaceAll("-", ""));

            Date today = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            int todayDate = Integer.parseInt(df.format(today).replaceAll("-", ""));

            if (integerDate <= todayDate) {
                model.addAttribute("classification", "<form action=\"/classification\" method=\"GET\"><input type=\"hidden\" name=\"event\" value=\"" + event + "\"><button class=\"classification-button\" type=\"submit\">Classification</button></form>");
            } else {
                model.addAttribute("classification", "");
            }
        } else {
            model.addAttribute("participants", "<p>This event has no classifications!</p>");
            model.addAttribute("classification", "");
        }

        return "event_info";
    }

    @GetMapping("/classification")
    public String getClassification(Model model, @RequestParam String event) throws Exception {
        model.addAttribute("event", event);

        RegisterManager registerManager = new RegisterManager(event);

        JSONObject classification = registerManager.getClassification("start", "finish");
        JSONArray general = classification.getJSONArray("general");
        JSONArray female = classification.getJSONArray("female");
        JSONArray male = classification.getJSONArray("male");

        if (general.length() == 0) {
            model.addAttribute("general", "<p style=\"margin-top: 10px\">These event has no classification yet</p>");
            model.addAttribute("female", "");
            model.addAttribute("male", "");
        } else {
            StringBuilder builder = new StringBuilder();

            // general

            builder.append("<div id=\"abc\" class=\"classification-g\">");

            for (int i = 0; i < general.length(); i++) {
                JSONObject participant = general.getJSONObject(i);

                builder.append("<div class=\"participant\">");

                builder.append("<div style=\"width: 10%\"><p>").append(participant.getInt("dorsal")).append("</p></div>");
                builder.append("<div style=\"width: 40%\"><p>").append(participant.getString("name")).append("</p></div>");
                builder.append("<div style=\"width: 15%\"><p>").append(participant.getString("time")).append("</p></div>");
                builder.append("<div style=\"width: 15%; text-align: right\"><p>").append(participant.getString("gender")).append("</p></div>");
                builder.append("<div style=\"width: 20%; text-align: right\"><p>").append(participant.getString("tier")).append("</p></div>");

                builder.append("</div>");

            }

            builder.append("</div>");

            model.addAttribute("general", builder.toString());

            // female

            builder = new StringBuilder();

            builder.append("<div class=\"classification-f\">");

            for (int i = 0; i < female.length(); i++) {
                JSONObject participant = female.getJSONObject(i);

                builder.append("<div class=\"participant\">");

                builder.append("<div style=\"width: 10%\"><p>").append(participant.getInt("dorsal")).append("</p></div>");
                builder.append("<div style=\"width: 40%\"><p>").append(participant.getString("name")).append("</p></div>");
                builder.append("<div style=\"width: 15%\"><p>").append(participant.getString("time")).append("</p></div>");
                builder.append("<div style=\"width: 15%; text-align: right\"><p>").append(participant.getString("gender")).append("</p></div>");
                builder.append("<div style=\"width: 20%; text-align: right\"><p>").append(participant.getString("tier")).append("</p></div>");

                builder.append("</div>");
            }

            builder.append("</div>");

            model.addAttribute("female", builder.toString());

            // male

            builder = new StringBuilder();

            builder.append("<div class=\"classification-m\">");

            for (int i = 0; i < male.length(); i++) {
                JSONObject participant = male.getJSONObject(i);

                builder.append("<div class=\"participant\">");

                builder.append("<div style=\"width: 10%\"><p>").append(participant.getInt("dorsal")).append("</p></div>");
                builder.append("<div style=\"width: 40%\"><p>").append(participant.getString("name")).append("</p></div>");
                builder.append("<div style=\"width: 15%\"><p>").append(participant.getString("time")).append("</p></div>");
                builder.append("<div style=\"width: 15%; text-align: right\"><p>").append(participant.getString("gender")).append("</p></div>");
                builder.append("<div style=\"width: 20%; text-align: right\"><p>").append(participant.getString("tier")).append("</p></div>");

                builder.append("</div>");
            }

            builder.append("</div>");

            model.addAttribute("male", builder.toString());
        }

        return "classification";
    }

    @GetMapping("/about_us")
    public String getAboutUs(Model model) {
        return "about_us";
    }

    @GetMapping("/search_events")
    public String searchEvents(Model model) throws Exception {
        RegisterManager registerManager = new RegisterManager();

        JSONArray events = registerManager.getEvents("all");

        StringBuilder builder = new StringBuilder();

        if (events.length() == 0) {
            builder.append("<p>There are no registered events</p>");
        } else {
            builder.append("<div id=\"events-to-display\">");

            for (int i = 0; i < events.length(); i++) {
                JSONObject event = events.getJSONObject(i);

                builder.append("<div id=\"").append(event.getString("event")).append("|").append(event.getString("date")).append("\" class=\"participant\">");

                builder.append("<div style=\"width: 50%\"><p>").append(event.getString("event")).append("</p></div>");
                builder.append("<div style=\"width: 30%; text-align: right\"><p>").append(event.getString("date")).append("</p></div>");

                builder.append("<form action=\"/event_info\" method=\"GET\"><input type=\"hidden\" name=\"event\" value=\"").append(event.getString("event")).append("\"><button type=\"submit\">Details</button></form>");

                builder.append("</div>");
            }
            builder.append("<p id=\"empty\" style=\"display: none\">No events to display</p>");
            builder.append("</div");

            StringBuilder search = new StringBuilder();
            search.append("<><div class=\"search\"><button onClick=\"onClickSearchName()\">Search by Name</button><input name=\"event-name\"><input type=\"date\" name=\"event-date\"><button onClick=\"onClickSearchDate()\">Search by Date</button></div></>");
            model.addAttribute("search", search.toString());
        }

        model.addAttribute("events", builder.toString());

        return "search_events";
    }

    @GetMapping("/track_participants")
    public String trackParticipants(@RequestParam String event, Model model) throws Exception {
        model.addAttribute("event", event);

        RegisterManager registerManager = new RegisterManager(event);

        JSONArray participantsStart = registerManager.getParticipantsForSection("start");
        JSONArray participantsP1 = registerManager.getParticipantsForSection("p1");
        JSONArray participantsP2 = registerManager.getParticipantsForSection("p2");
        JSONArray participantsP3 = registerManager.getParticipantsForSection("p3");
        JSONArray participantsFinish = registerManager.getParticipantsForSection("finish");

        StringBuilder builder = new StringBuilder();

        if (participantsStart.length() > 0) {
            builder.append("<div id=\"participantsStart\">");

            for (int i = 0; i < participantsStart.length(); i++) {
                JSONObject participant = participantsStart.getJSONObject(i);

                builder.append("<div class=\"participant\">");

                builder.append("<div style=\"width: 10%\"><p style=\"text-align: right\">").append(participant.getInt("dorsal")).append("</p></div><div style=\"width: 50%\"><p>")
                        .append(participant.getString("name")).append("</p></div><div style=\"width: 30%; display: flex; flex-direction: row; justify-content: end\"><p style=\"text-align\": left\">")
                        .append(participant.getString("time").toUpperCase()).append("</div></p>");

                builder.append("</div>");
            }
            builder.append("</div>");

            model.addAttribute("start", builder.toString());

            if (participantsP1.length() > 0) {
                builder = new StringBuilder();

                builder.append("<div id=\"participantsP1\">");

                for (int i = 0; i < participantsP1.length(); i++) {
                    JSONObject participant = participantsP1.getJSONObject(i);

                    builder.append("<div class=\"participant\">");

                    builder.append("<div style=\"width: 10%\"><p style=\"text-align: right\">").append(participant.getInt("dorsal")).append("</p></div><div style=\"width: 50%\"><p>")
                            .append(participant.getString("name")).append("</p></div><div style=\"width: 30%; display: flex; flex-direction: row; justify-content: end\"><p style=\"text-align\": left\">")
                            .append(participant.getString("time").toUpperCase()).append("</div></p>");

                    builder.append("</div>");
                }
                builder.append("</div>");

                model.addAttribute("p1", builder.toString());

                if (participantsP2.length() > 0) {
                    builder = new StringBuilder();

                    builder.append("<div id=\"participantsP2\">");

                    for (int i = 0; i < participantsP2.length(); i++) {
                        JSONObject participant = participantsP2.getJSONObject(i);

                        builder.append("<div class=\"participant\">");

                        builder.append("<div style=\"width: 10%\"><p style=\"text-align: right\">").append(participant.getInt("dorsal")).append("</p></div><div style=\"width: 50%\"><p>")
                                .append(participant.getString("name")).append("</p></div><div style=\"width: 30%; display: flex; flex-direction: row; justify-content: end\"><p style=\"text-align\": left\">")
                                .append(participant.getString("time").toUpperCase()).append("</div></p>");

                        builder.append("</div>");
                    }
                    builder.append("</div>");

                    model.addAttribute("p2", builder.toString());

                    if (participantsP3.length() > 0) {
                        builder = new StringBuilder();

                        builder.append("<div id=\"participantsP3\">");

                        for (int i = 0; i < participantsP3.length(); i++) {
                            JSONObject participant = participantsP3.getJSONObject(i);

                            builder.append("<div class=\"participant\">");

                            builder.append("<div style=\"width: 10%\"><p style=\"text-align: right\">").append(participant.getInt("dorsal")).append("</p></div><div style=\"width: 50%\"><p>")
                                    .append(participant.getString("name")).append("</p></div><div style=\"width: 30%; display: flex; flex-direction: row; justify-content: end\"><p style=\"text-align\": left\">")
                                    .append(participant.getString("time").toUpperCase()).append("</div></p>");

                            builder.append("</div>");
                        }
                        builder.append("</div>");

                        model.addAttribute("p3", builder.toString());

                        if (participantsFinish.length() > 0) {
                            builder = new StringBuilder();

                            builder.append("<div id=\"participantsFinish\">");

                            for (int i = 0; i < participantsFinish.length(); i++) {
                                JSONObject participant = participantsFinish.getJSONObject(i);

                                builder.append("<div class=\"participant\">");

                                builder.append("<div style=\"width: 10%\"><p style=\"text-align: right\">").append(participant.getInt("dorsal")).append("</p></div><div style=\"width: 50%\"><p>")
                                        .append(participant.getString("name")).append("</p></div><div style=\"width: 30%; display: flex; flex-direction: row; justify-content: end\"><p style=\"text-align\": left\">")
                                        .append(participant.getString("time").toUpperCase()).append("</div></p>");

                                builder.append("</div>");
                            }
                            builder.append("</div>");

                            model.addAttribute("finish", builder.toString());
                        } else {
                            model.addAttribute("finish", "<p id=\"finish\">There is no participants for Finish section");
                        }
                    } else {
                        model.addAttribute("p3", "<p id=\"p3\">There is no participants for P3 section");
                        model.addAttribute("finish", "<p id=\"finish\">There is no participants for Finish section");
                    }
                } else {
                    model.addAttribute("p2", "<p id=\"p2\">There is no participants for P2 section");
                    model.addAttribute("p3", "<p id=\"p3\">There is no participants for P3 section");
                    model.addAttribute("finish", "<p id=\"finish\">There is no participants for Finish section");
                }
            } else {
                model.addAttribute("p1", "<p id=\"p1\">There is no participants for P1 section");
                model.addAttribute("p2", "<p id=\"p2\">There is no participants for P2 section");
                model.addAttribute("p3", "<p id=\"p3\">There is no participants for P3 section");
                model.addAttribute("finish", "<p id=\"finish\">There is no participants for Finish section");
            }
        } else {
            model.addAttribute("start", "<p id=\"start\">There is no participants for the event");
            model.addAttribute("p1", "<p id=\"p1\">There is no participants for P1 section");
            model.addAttribute("p2", "<p id=\"p2\">There is no participants for P2 section");
            model.addAttribute("p3", "<p id=\"p3\">There is no participants for P3 section");
            model.addAttribute("finish", "<p id=\"finish\">There is no participants for Finish section");
        }


        return "track_participants";
    }
}
