<%--
  Created by IntelliJ IDEA.
  User: YG
  Date: 2022/12/16
  Time: 16:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>注销</title>
</head>
<body>
<%
    Cookie[] cookies = request.getCookies();
    for(int i=0;i<cookies.length;i++){
        cookies[i].setMaxAge(0);
        response.addCookie(cookies[i]);
    }
    String tips = "你已成功注销";
%>
<script type="text/javascript">
    alert("<%=tips%>");  //弹出警示框
</script>
<%
    response.sendRedirect("login.jsp");
%>
</body>
</html>
