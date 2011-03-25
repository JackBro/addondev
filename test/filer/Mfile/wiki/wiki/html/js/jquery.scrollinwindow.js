/*

ある要素が画面内に収まるようにスクロールするjQueryプラグイン

$('#hoge').isinwindow(); // hogeが画面内（見える位置）にあればtrue, なければfalseを返します
$('#hoge').inwindow(); // hogeがスクロールしないと見えない位置にある場合、見えるようにスクロールします

http://d.hatena.ne.jp/JJX/20090227/1235768010

*/

jQuery.fn.isinwindow = function(){
    var top    = $(window).scrollTop();
    var bottom = $(window).height() + $(window).scrollTop();
    var left   = $(window).scrollLeft();
    var right  = $(window).width() + $(window).scrollLeft();

    return this.offset().top  >= top   && this.offset().top  + this.height() <= bottom &&
           this.offset().left >= left  && this.offset().left + this.width()  <= right;
};
jQuery.fn.inwindow = function(){
    var top    = $(window).scrollTop();
    var bottom = $(window).height() + $(window).scrollTop();
    var left   = $(window).scrollLeft();
    var right  = $(window).width() + $(window).scrollLeft();

    if(!(this.offset().top  >= top)){
        $(window).scrollTop(this.offset().top);
    }
    if(!(this.offset().top  + this.height() <= bottom)){
        $(window).scrollTop(
            $(window).scrollTop() + this.offset().top  + this.height() - bottom
        );
    }
    if(!(this.offset().left  >= left)){
        $(window).scrollLeft(this.offset().left);
    }
    if(!(this.offset().left  + this.width() <= right)){
        $(window).scrollLeft(
            $(window).scrollLeft() + this.offset().left  + this.width() - right
        );
    }
    return this;
};
