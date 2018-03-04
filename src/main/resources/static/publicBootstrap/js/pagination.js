$(document).ready(function(){
    // initialize number of items
	var nUsers = 0;
    var nOrders = 0;

    // javascript method for add more users
	$("#moreUsers").on("click",function(){
		nUsers++;
		$.get("/adminDashboard/moreUsers",{
			page:nUsers
		})
		.done(function(data){
		    console.log(data);
			if(!$.trim(data)){
				$("#moreUsers").attr("disabled","disabled");
				window.alert("¡No hay más usuarios!");
			}else{
				$("#users").append(data);
			}
		});
	});

    // javascript method for add more orders
    $("#moreOrders").on("click", function () {
        nOrders++;
        console.log("hola1");
        $.get("/adminDashboard/moreOrders", {
            page: nOrders
        }).done(function (data) {
            console.log(data);
            if (!$.trim(data)) {
                $("#moreOrders").attr("disabled", "disabled");
                window.alert("¡No hay más pedidos pendientes de validar!");
            } else {
                $("#orders").append(data);
            }
        });
    });
});