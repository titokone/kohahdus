<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<jsp:include page="../menu.jsp"/>


<html>
<head>
<title>Pekan teht�v�t</title>
</head>

<body>

<h2>Pekka Pekkala</h2>

<a href="#">Poista k�ytt�j�</a><br>

<h3>Henkil�tiedot</h3>
<ul>
<li>Henkil�tunnus: 9813475-1242
<li>Opiskelijanumero: 09385903185
<li>T�h�n kaikki opiskelijan tiedot
<li>...
</ul>

<h3>Teht�v�t</h3>
<p>
<table border="1" cellpadding="4">
<tr>
<td>&nbsp;</td>
<td><b>Teht�v�</b></td>
<td><b>Tyyppi</b></td>
<td><b>Yrityskerrat</b></td>
<td><b>Viimeisin pvm</b></td>
</tr>
<tr>
<td><img src="positive.gif"></td>
<td>Teht�v� 1</td>
<td>T�ydennysteht�v�</td>
<td align="center">5</td>
<td>5.5.2006</td>
</tr>
<tr>
<td>&nbsp;</td>
<td>Teht�v� 2</td>
<td>Staattinen teht�v�</td>
<td align="center">0</td>
<td>-</td>
</tr>
<tr>
<td><img src="positive.gif"></td>
<td>Teht�v� 3</td>
<td>Staattinen teht�v�</td>
<td align="center">1</td>
<td>27.4.2006</td>
</tr>
<tr>
<td><img src="negative.gif"></td>
<td>Teht�v� 4</td>
<td>T�ydennysteht�v�</td>
<td align="center">2</td>
<td>2.5.2006</td>
</tr>
</table>
</p>

<p><b>Tehtyj� teht�vi� yhteens�:</b><br>
2 hyv�ksytty�<br>
1 keskener�ist�</p>

</body>
</html>
