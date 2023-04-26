var stompClient = null;

var player;


$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect1" ).click(function() { connectPlayer(1); });
    $( "#connect2" ).click(function() { connectPlayer(2); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendMessage(); });
    $( "#sendToAll" ).click(function() { sendPublicName(); });
});

function connectPlayer(playernum) {
    disconnectFromLobby();

    var socket = new SockJS('/connections');
    player = playernum;
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        // setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/player/msg', function (message) {
            console.log(message.body);
            showMessage(JSON.parse(message.body));
        });
        stompClient.send("/app/player", {}, JSON.stringify({'xyz': -203, 'player': player}));
    });



    var requestOptions = {
        method: 'POST',
        redirect: 'follow'
    };

    fetch("usingPlayer/"+player, requestOptions)
        .then(response => response.text())
        .then(result => console.log(result))
        .catch(error => console.log('error', error));


}

function sendMessage() {
    stompClient.send("/app/player", {}, JSON.stringify({'xyz': $("#message").val(), 'player': player}));
}

function showMessage(message) {

    if (message.x < 0){
        if (message.z == 3){
            if (message.player == 1){
                setConnected(false, null);
            }
            else {
                setConnected(null, false);
            }
        }
        else if(message.z == 2) {
            if (message.player == 1){
                setConnected(true, null);
            }
            else {
                setConnected(null, true);
            }
        }

        if (message.x == -2 && message.y == 0 && message.z == 0){
            if (player != message.player){
                dummyLoose()
            }
            else{
                dummyWin()
            }
        }
    }
    else{
        $("#messages").append("<tr><td>" + message.x + message.y + message.z + " player: "+ message.player +"</td></tr>");
    }
}

function dummyWin(){
    $("#messages").append("<tr><td>YOU SURVIVED!</td></tr>");
}

function dummyLoose(){
    $("#messages").append("<tr><td>YOU ARE DEAD!</td></tr>");
}

function disconnect() {
    stompClient.send("/app/player", {}, JSON.stringify({'xyz': -202, 'player': player}));
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    if (player == 1){
        setConnected(true, null);
    }
    else {
        setConnected(null, true);
    }
    console.log("Disconnected");

    $("#disconnect").prop("disabled", false);

    var requestOptions = {
        method: 'POST',
        redirect: 'follow'
    };

    fetch("/releasePlayer/"+player, requestOptions)
        .then(response => response.text())
        .then(result => console.log(result))
        .catch(error => console.log('error', error));
}

function setConnected(player1, player2) {
    $("#disconnect").prop("disabled", false);
    $("#messages").html("");
    if (player1 != null){
        $("#connect1").prop("disabled", !player1);
    }
    if (player2 != null){
        $("#connect2").prop("disabled", !player2);
    }
}

function setLobbyConnected(player1, player2) {
    $("#disconnect").prop("disabled", true);
    $("#messages").html("");
    if (player1 != null){
        $("#connect1").prop("disabled", !player1);
    }
    if (player2 != null){
        $("#connect2").prop("disabled", !player2);
    }
}
function disconnectFromLobby() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
}
