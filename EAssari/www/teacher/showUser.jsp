<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<jsp:include page="../menu.jsp"/>


<html>
<head>
<title>Pekan tehtävät</title>
</head>

<body>

<h2>Pekka Pekkala</h2>

<a href="#">Poista käyttäjä</a><br>

<h3>Henkilötiedot</h3>
<ul>
<li>Henkilötunnus: 9813475-1242
<li>Opiskelijanumero: 09385903185
<li>Tähän kaikki opiskelijan tiedot
<li>...
</ul>

<h3>Tehtävät</h3>
<p>
<table border="1" cellpadding="4">
<tr>
<td>&nbsp;</td>
<td><b>Tehtävä</b></td>
<td><b>Tyyppi</b></td>
<td><b>Yrityskerrat</b></td>
<td><b>Viimeisin pvm</b></td>
</tr>
<tr>
<td><img src="positive.gif"></td>
<td>Tehtävä 1</td>
<td>Täydennystehtävä</td>
<td align="center">5</td>
<td>5.5.2006</td>
</tr>
<tr>
<td>&nbsp;</td>
<td>Tehtävä 2</td>
<td>Staattinen tehtävä</td>
<td align="center">0</td>
<td>-</td>
</tr>
<tr>
<td><img src="positive.gif"></td>
<td>Tehtävä 3</td>
<td>Staattinen tehtävä</td>
<td align="center">1</td>
<td>27.4.2006</td>
</tr>
<tr>
<td><img src="negative.gif"></td>
<td>Tehtävä 4</td>
<td>Täydennystehtävä</td>
<td align="center">2</td>
<td>2.5.2006</td>
</tr>
</table>
</p>

<p><b>Tehtyjä tehtäviä yhteensä:</b><br>
2 hyväksyttyä<br>
1 keskeneräistä</p>

</body>
</html>
