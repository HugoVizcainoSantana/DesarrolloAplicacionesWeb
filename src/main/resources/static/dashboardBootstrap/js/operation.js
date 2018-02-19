/**
 *
 */
function oper() {
    var countPer = document.getElementById("blind").value;
    var countLuc = document.getElementById("light").value;
    var resultPer = countPer * 32;
    var resultLuc = countLuc * 23;
    var total = resultPer + resultLuc

    $('#luces').append(resultLuc);
    $('#persiana').append(resultPer);
    $('#total').append(total);
}