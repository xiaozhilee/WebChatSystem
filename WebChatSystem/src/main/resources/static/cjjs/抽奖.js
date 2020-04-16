$(function(){
    $("#pointer").click(function () {
        var m = randm();
        switch (m) {
            case 0:
                rate(60,"我吃兔兔！");
                break;
            case 1:
                rate(120,"你陪我睡哟！");
                break;
            case 2:
                rate(180,"对不起嘛！");
                break;
            case 3:
                rate(240,"你养兔兔哟！");
                break;
            case 4:
                rate(300,"来进被窝窝！");
                break;
            case 5:
                rate(360,"给老子道歉！");
                break;
        }
    })
    function randm() {
        return Math.floor(Math.random()*5)
    }

    function rate(x,y) {
        $("#background").stopRotate();
        $("#background").rotate({
            angle:0,
            animateTo:x+1800,
            duration:3000,
            callback:function(){    //回调函数
                alert(y);
            }
        })
    }
    
})