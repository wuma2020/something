<%--
  User: 伍马
  Date: 2018/7/28 0028
  --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%  String path = application.getContextPath();%>
<head>
    <title>登录页面</title>
</head>

<body>
        <form action="<%=path%>/subLogin" method="post" >
            姓名 ：<input type="text" name="username" value="登录名" />
            密码 ：<input type="password" name="password" value="密码" />
            <input type="checkbox"  name="rememberMe"/> 记住我 <br/>
            <input type="submit" value="登录" />
        </form>
</body>
</html>