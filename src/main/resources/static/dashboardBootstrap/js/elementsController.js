$('.fav-element-toggle').on('click', function () {
    var btn = $(this);
    if (btn.html() === "Apagar") {
        console.log("Apagando");
        btn.html("<i class='fa fa-spinner fa-spin '></i> Apagando");
        setTimeout(function () {
            console.log("Fin timeout");
            btn.html("Encender");
        }, 3000);
    } else if (btn.html() === "Encender") {
        console.log("Encendiendo");
        btn.html("<i class='fa fa-spinner fa-spin '></i> Encendiendo");
        setTimeout(function () {
            console.log("Fin timeout");
            btn.html("Apagar");
        }, 3000);
    }
});