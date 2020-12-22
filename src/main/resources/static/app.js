var stompClient = null;

function setOnlineCount(message) {
    $("#online-count").text(message)
}

function updateOnlineCount() {
    $.get("/onlineCount", function (count) {
        $("#online-count").text(count)
    })
}

function tts(text) {
    window.speechSynthesis.speak(new SpeechSynthesisUtterance(text));
}

function ttsTest() {
    tts('吃瓜');
}

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        $("#connection-status").text("成功")
        console.log('Connected: ' + frame);
        updateOnlineCount()
        stompClient.subscribe('/broadcast/waymark', function (data) {
            var message = JSON.parse(data.body)

            var row = $("<tr><td>" + new Date().toLocaleString() + "</td><td>" + message.from + "</td><td>" + message.content + "</td></tr>")
            $("#message").append(row);

            var port = $("#ppport").val()
            fetch("http://localhost:" + port + "/place", {
                method: "POST",
                body: message.content,
                headers: new Headers({'Content-Type': 'application/json'})
            }).catch(err => {
                row.css('color', 'red');
                console.error('Error:', error);
            })
        });
        stompClient.subscribe('/broadcast/onlineCount', function (message) {
            setOnlineCount(JSON.parse(message.body));
        });
        stompClient.subscribe('/broadcast/tts', function (message) {
            tts(message.body);
        });
    });
}

$(function () {
    connect();
});

