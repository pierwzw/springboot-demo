<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>My WebSocket</title>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="./../../static/js/websocket.js"></script>
</head>
<body>
    凌汐聊天器
    <br />
    在线人数：<div id="count">0</div>
    <br>
    <input id="message" type="text" />
    <button onclick="sendMessage()">发送消息</button>
    <button onclick="closeWebSocket()">退出聊天</button>
    <div id="context"></div>
</body>
</html>