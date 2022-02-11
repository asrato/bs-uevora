<%@ page language="java" session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Register - Galaxy Runners</title>
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
                <h2>Register</h2>
                <form class="form" id="form1" method="POST" action="/register">
                    <p class="input-label">username</p>
                    <input type="text" name="username">
                    <p class="input-label">password</p>
                    <input type="password" name="password">
                    <p class="input-label">name</p>
                    <input type="text" name="real_name">
                    <p class="input-label">gender</p>
                    <div class="radio-group">
                        <div class="radio">
                            <input type="radio" checked name="genero" value="m">
                            <p class="radio-label">Masculine</p>
                        </div>
                        <div class="radio">
                            <input type="radio" name="genero" value="f">
                            <p class="radio-label">Feminine</p>
                        </div>
                    </div>
                    <p class="input-label">tier</p>
                    <div class="radio-group">
                        <div class="radio-subgroup">
                            <div class="radio">
                                <input type="radio" checked name="escalao" value="jun">
                                <p class="radio-label">Junior</p>
                            </div>
                            <div class="radio">
                                <input type="radio" name="escalao" value="sen">
                                <p class="radio-label">Senior</p>
                            </div>
                            <div class="radio">
                                <input type="radio" name="escalao" value="vet35">
                                <p class="radio-label">Vet35</p>
                            </div>
                        </div>
                        <div class="radio-subgroup">
                            <div class="radio">
                                <input type="radio" name="escalao" value="vet40">
                                <p class="radio-label">Vet40</p>
                            </div>
                            <div class="radio">
                                <input type="radio" name="escalao" value="vet45">
                                <p class="radio-label">Vet45</p>
                            </div>
                            <div class="radio">
                                <input type="radio" name="escalao" value="vet50">
                                <p class="radio-label">Vet50</p>
                            </div>
                        </div>
                        <div class="radio-subgroup">
                            <div class="radio">
                                <input type="radio" name="escalao" value="vet55">
                                <p class="radio-label">Vet55</p>
                            </div>
                            <div class="radio">
                                <input type="radio" name="escalao" value="vet60">
                                <p class="radio-label">Vet60</p>
                            </div>
                            <div class="radio">
                                <input type="radio" name="escalao" value="vet65">
                                <p class="radio-label">Vet65</p>
                            </div>
                        </div>
                    </div>
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