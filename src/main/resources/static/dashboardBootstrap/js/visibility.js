function showHomes(id) {
    var div = document.getElementById(id);
    console.log(id);

    if (div.style.display === "block") {
        div.style.display = "none";
    } else {
        div.style.display = "block";
    }
}
