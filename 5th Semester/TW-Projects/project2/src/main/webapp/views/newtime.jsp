<%@ page language="java" session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Time Registration - Galaxy Runners</title>
    <link rel="stylesheet" href="../static/css/reseter.css">
    <link rel="stylesheet" href="../static/css/index.css">
    <link rel="icon" href="../static/img/galaxy.png">
    <link href="https://fonts.googleapis.com/css2?family=Lobster&family=Rowdies&display=swap" rel="stylesheet">
</head>
<body>
<div id="wrapper">
    <div id="content">
        <a class="back-button" href="/">< Back to menu</a>
        <br>
        <div class="page-content">
            <div class="form-container">
                <h2>New Time</h2>

                <form class="form" id="form1" method="GET" action="/registertime">
                    <p class="input-label">Event name</p>
                    <input type="text" name="eventname">
                    <p class="input-label">Dorsal</p>
                    <input type="number" min="0" name="dorsal">
                    <p class="input-label">Section</p>
                    <div class="radio-group">
                        <div class="radio-subgroup">
                            <div class="radio">
                                <input type="radio" checked name="section" value="start">
                                <p class="radio-label">Start</p>
                            </div>
                            <div class="radio">
                                <input type="radio" name="section" value="p1">
                                <p class="radio-label">P1</p>
                            </div>
                            <div class="radio">
                                <input type="radio" name="section" value="p2">
                                <p class="radio-label">P2</p>
                            </div>
                            <div class="radio">
                                <input type="radio" name="section" value="p3">
                                <p class="radio-label">P3</p>
                            </div>
                            <div class="radio">
                                <input type="radio" name="section" value="finish">
                                <p class="radio-label">Finish</p>
                            </div>
                        </div>
                    </div>
                    <p class="input-label">Time</p>
                    <input type="time" name="timestamp" value="11:59:59 PM" step="1">
                    <button type="submit">Register</button>
                </form>
            </div>
        </div>
    </div>
    <div id="cover"></div>
</div>
<div id="footer">
    <div id="footer-content">
        <div id="footer-left">
            <div class="footer-subtext">
                <p>Official</p>
                <p>Sponsors</p>
            </div>
            <div class="horizontal-divider"></div>
            <div class="footer-image">
                <img src="../static/img/uevora.png" alt="uévora" height="100%">
            </div>
            <div class="footer-image">
                <img src="../static/img/nasa.png" alt="nasa" height="100%">
            </div>
            <div class="footer-image">
                <img src="../static/img/prozis.png" alt="prozis" height="100%">
            </div>
            <div class="footer-image">
                <img src="../static/img/spacex.png" alt="spacex" height="80%">
            </div>
        </div>
        <div id="footer-center">
            <a href="/about_us">
                <button>ABOUT US</button>
            </a>
        </div>
        <div id="footer-right">
            <div class="foot">
                <p class="footer-rights">João Rouxinol #44451</p>
                <p class="footer-rights">André Rato #45517</p>
            </div>
            <img class="logo-mnlda" src="../static/img/mnlda-logo.png" alt="mnlda logo" height="40px">
            <div class="horizontal-divider"></div>
            <div class="footer-subtext">
                <p>All rights</p>
                <p>Reserverd</p>
            </div>
        </div>
    </div>
</div>
</body>
</html>