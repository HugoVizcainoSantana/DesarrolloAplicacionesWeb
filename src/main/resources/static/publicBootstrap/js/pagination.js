$(document).ready(function(){
    // initialize number of items
	var nUsers = 0;

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
});