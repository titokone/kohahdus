<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<jsp:include page="../menu.jsp"/>


<p>
<b>Hae opiskelija</b><br>
<input type="text" value="Pekka"> <input type="button" value="Hae" onclick="location.href = 'showUser.jsp'">
</p>


<p>Löytyi useita. Valitse seuraavista.</p>

<p>
<a href="showUser.jsp">Pekka Molli</a><br>
<a href="showUser.jsp">Pekka Pakkala</a><br>
<a href="showUser.jsp">Pekka Lahtinen</a>
</p>

