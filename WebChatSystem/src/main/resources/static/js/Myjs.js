var key = CryptoJS.enc.Utf8.parse("WebChatSystem202");
function Encrypt(word) {
    var srcs = CryptoJS.enc.Utf8.parse(word);
    var encrypted = CryptoJS.AES.encrypt(srcs, key, {
        mode : CryptoJS.mode.ECB,
        padding : CryptoJS.pad.Pkcs7
    });
    return encrypted.toString();
}

function Decrypt(word) {
    var decrypt = CryptoJS.AES.decrypt(word, key, {
        mode : CryptoJS.mode.ECB,
        padding : CryptoJS.pad.Pkcs7
    });
    return CryptoJS.enc.Utf8.stringify(decrypt).toString();
}

/*
* Websocket连接
* */
var socket;
var count=0;
function openSocket() {
    var Token = localStorage.getItem("Token");
    var email = localStorage.getItem("email");
    if (typeof (WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
    } else {
        console.log("您的浏览器支持WebSocket");
        //实现化WebSocket对象，指定要连接的服务器地址与端口  建立连接
        //var socketUrl="${request.contextPath}/im/"+$("#userId").val();

        var socketUrl = "http://39.106.105.240:80/MyWebsocket/" + Token;
        socketUrl = socketUrl.replace("https", "ws").replace("http", "ws");
        if (socket != null) {
            socket.close();
            socket = null;
        }
        socket = new WebSocket(socketUrl);
        //打开事件
        socket.onopen = function () {
            console.log("websocket已打开");
            //socket.send("这是来自客户端的消息" + location.href + new Date());
        };
        //获得消息事件
        socket.onmessage = function (msg) {
            console.log(msg.data);
            var mess = JSON.parse(msg.data)
            //发现消息进入    开始处理前端触发逻辑
            var fromuseremails = mess.fromuseremail;
            var touserid=mess.touserid;
            var mes = Decrypt(mess.mestext);
            if(touserid==-1) {
                $("#insert").before("<div id='myAlert" + count + "' class=\"alert alert-warning\"><a class=\"close\" data-dismiss=\"alert\">&times;</a><a>收到" + fromuseremails + "的消息</a></div>");
            }
            else
            {
                $("#insert").before("<div id='myAlert"+count+"' class=\"alert alert-warning\"><a class=\"close\" data-dismiss=\"alert\">&times;</a><a>收到"+touserid+"群的消息</a></div>");
            }
            count++;
            var id="myAlert"+(count-4);
            console.log(id);
            if(count>4)document.getElementById(id).style.display="none";
        };
        //关闭事件
        socket.onclose = function () {
            console.log("websocket已关闭");
        };
        //发生了错误事件
        socket.onerror = function () {
            console.log("websocket发生了错误");
        }
    }
}
function goodbye() {
    if(socket!==null&&socket.readyState==1)
        socket.onclose();
}


function set_name() {
    var thename=localStorage.getItem("this_name");
    var theimg=localStorage.getItem("this_img");
    if(theimg!=null&&theimg!="")
    document.getElementById("user_img").src=theimg;
    if(thename!=null&&thename!="")
    document.getElementById("user_name").innerText=thename;
}