var websocket = null;
// 判断当前浏览器是否支持WebSocket
if ('WebSocket' in window) {
	// 创建WebSocket对象,连接服务器端点
	websocket = new WebSocket("ws://localhost:8080/ws");
} else {
	alert('Not support websocket')
}

// 连接发生错误的回调方法
websocket.onerror = function() {
	appendMessage("error");

};

// 连接成功建立的回调方法
websocket.onopen = function(event) {
	/*$.get("/websocket/getOnlineCount",function(data,status){
		$("#count").html(JSON.parse(data));
	});*/
	sendSysMessage("内部通知:一个连接加入了");
	appendMessage("open");
}

// 接收到消息的回调方法
websocket.onmessage = function(event) {
	if (event.data.indexOf("内部") !== -1) {
		$.get("/websocket/getOnlineCount",function(data,status){
			$("#count").html(JSON.parse(data));
		});
	} else
	if (event.data.indexOf("系统") !== -1) {
		$.get("/websocket/getOnlineCount",function(data,status){
			$("#count").html(JSON.parse(data));
		});
		appendMessage(event.data);
	}else{
		appendMessage(event.data);
	}
}

// 连接关闭的回调方法
websocket.onclose = function() {
	/*$.get("/websocket/getOnlineCount",function(data,status){
		$("#count").html(JSON.parse(data));
	});*/
	/*sendSysMessage("内部通知:一个连接关闭了");*/
	appendMessage("close");
}

// 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，
// 防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function() {
	websocket.close();
}

// 将消息显示在网页上
function appendMessage(message) {
	var context = $("#context").html() +"<br/>" + message;
	$("#context").html(context);
}

// 关闭连接
function closeWebSocket() {
	sendSysMessage("内部通知:一个连接关闭了");
	$.get("/websocket/getOnlineCount",function(data,status){
		$("#count").html(JSON.parse(data));
	});
	websocket.close();
}

// 发送消息
function sendMessage() {
	var message = $("#message").val();
	websocket.send(message);
}

// 发送消息
function sendSysMessage(message) {
	websocket.send(message);
}

function getOnlineCount() {

}