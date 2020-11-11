var stompClient = null;

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        $("#connection-status").text("成功")
        console.log('Connected: ' + frame);
        updateOnlineCount()
        stompClient.subscribe('/broadcast/waymark', function (data) {
            var message = JSON.parse(data.body)
            showLog(message);
            sendMessageToPP(message.content)
        });
        stompClient.subscribe('/broadcast/onlineCount', function (message) {
            setOnlineCount(JSON.parse(message.body));
        });
    });
}

function showLog(message) {
    $("#message").append("<tr><td>" + new Date().toLocaleString() + "</td><td>" + message.from + "</td><td>" + message.content + "</td></tr>");
}

function setOnlineCount(message) {
    $("#online-count").text(message)
}

function sendMessageToPP(data) {
    var port = $("#ppport").val()
    fetch("http://localhost:" + port + "/place", {
        method: "POST",
        body: data,
        headers: new Headers({'Content-Type': 'application/json'})
    }).catch(err => { console.error('Error:', error)  })
}

function updateOnlineCount() {
    $.get("/onlineCount", function (count) {
        $("#online-count").text(count)
    })
}

$(function () {
    connect();
});

