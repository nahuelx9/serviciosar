addEventListener('load', inicio, false);


VanillaTilt.init(document.querySelectorAll(".menu-servicios"), {
    max: 15,
    speed: 400,
    transition: true,
    scale: 1.1
});

$(document).ready(function () {
    $('[data-toggle="popover"]').popover();
});


$('#pop').popover().click(function () {
    setTimeout(function () {
        $('#pop').popover('hide');
    }, 5000);
});

/* Menu desplegable lateral */
function openNav() {
    document.getElementById("mySidenav").style.width = "250px";
}

/* Set the width of the side navigation to 0 */
function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
}

$("#fragment-servicios").hide();
$("#fragment-imagenes").hide();
$("#fragment-analisis").hide();
$("#fragment-buzon").hide();
$("#fragment-alertas").hide();


$("#btnUsuarios").click(function () {
    $("#fragment-servicios").hide();
    $("#fragment-imagenes").hide();
    $("#fragment-analisis").hide();
    $("#fragment-buzon").hide();
    $("#fragment-alertas").hide();
    $("#fragment-usuarios").show();
});

$("#btnServicios").click(function () {
    $("#fragment-imagenes").hide();
    $("#fragment-usuarios").hide();
    $("#fragment-analisis").hide();
    $("#fragment-buzon").hide();
    $("#fragment-alertas").hide();
    $("#fragment-servicios").show();
});
$("#btnImagenes").click(function () {
    $("#fragment-usuarios").hide();
    $("#fragment-servicios").hide();
    $("#fragment-analisis").hide();
    $("#fragment-alertas").hide();
    $("#fragment-imagenes").show();
});

$("#btnAnalisis").click(function () {
    $("#fragment-usuarios").hide();
    $("#fragment-servicios").hide();
    $("#fragment-imagenes").hide();
    $("#fragment-buzon").hide();
    $("#fragment-alertas").hide();
    $("#fragment-analisis").show();
});
$("#btnMensajes").click(function () {
    $("#fragment-usuarios").hide();
    $("#fragment-servicios").hide();
    $("#fragment-imagenes").hide();
    $("#fragment-analisis").hide();
    $("#fragment-alertas").hide();
    $("#fragment-buzon").show();
});
$("#btnAlertas").click(function () {
    $("#fragment-usuarios").hide();
    $("#fragment-servicios").hide();
    $("#fragment-imagenes").hide();
    $("#fragment-analisis").hide();
    $("#fragment-buzon").hide();
    $("#fragment-alertas").show();
});

/* Menu desplegable lateral  fin*/

/* DataTable*/
$(document).ready(function () {
    $('#dtUsuarios').DataTable();
    $('.dataTables_length').addClass('bs-select');
});
$(document).ready(function () {
    $('#dtServicios').DataTable();
    $('.dataTables_length').addClass('bs-select');
});
$(document).ready(function () {
    $('#dtImagenes').DataTable();
    $('.dataTables_length').addClass('bs-select');
});
$(document).ready(function () {
    $('#dtBuzon').DataTable();
    $('.dataTables_length').addClass('bs-select');
});
$(document).ready(function () {
    $('#dtAlerta').DataTable();
    $('.dataTables_length').addClass('bs-select');
});
/* DataTable Fin*/

/* Reporte modal */
$("#text-area-reporte").hide();
$('#select-reporte').change(function () {
    if ($('#select-reporte').val() === 'Otro') {
        $("#text-area-reporte").show();
    } else {
        $("#text-area-reporte").hide();
    }
});

/* Reporte modal Fin*/



function inicio() {

    document.getElementById('precio').addEventListener('change', cambioPrecio, false);
    document.getElementById('rangoSinPrecio').addEventListener('click', desabilitar, false);
}

function imagenCargada() {
    document.getElementById("inputLabel0").innerHTML = document.getElementById('input0').files[0].name;
    document.getElementById("inputLabel1").innerHTML = document.getElementById('input1').files[0].name;
    document.getElementById("inputLabel2").innerHTML = document.getElementById('input2').files[0].name;
    document.getElementById("inputLabel3").innerHTML = document.getElementById('input3').files[0].name;
    document.getElementById("inputLabel4").innerHTML = document.getElementById('input4').files[0].name;
    document.getElementById("inputLabel5").innerHTML = document.getElementById('input5').files[0].name;
}

function imagenGuardar() {
    document.getElementById("inputGuardarLabel").innerHTML = document.getElementById('inputGuardar').files[0].name;
}




function deshabilitarBtnBusquedaIndex() {
    const mensaje = document.getElementById("mensaje");
    const boton = document.getElementById("btnBuscar");
    console.log(boton);
    if (mensaje.value.trim() !== "" && mensaje.value.length > 2) {
        console.log("Se muestra");
        boton.removeAttribute('disabled');
    } else {
        boton.setAttribute('disabled', "true");
    }
}

function contarCaracteres() {
    let texto = document.getElementById("message").value;
    let cantidadDeCaracteres = texto.length;
    document.getElementById("caracteres").innerHTML = cantidadDeCaracteres;
}

var checkbox = document.getElementById('checkUser');
const btnUserRegister = document.getElementById('btnRegisterUser');
checkbox.addEventListener('change', function ()
{
    if (this.checked) {
        btnUserRegister.removeAttribute('disabled');
    } else {
        btnUserRegister.setAttribute('disabled', "true");
    }
});
function cambioPrecio() {
    document.getElementById('pre').innerHTML = document.getElementById('precio').value;
}

window.setTimeout(function () {
    $(".alert").fadeTo(700, 0).slideUp(700, function () {
        $(this).remove();
    });
}, 5000);
function desabilitar()
{
    let precio = document.getElementById('precio');
    if (precio.disabled === false) {
        precio.disabled = true;
        document.getElementById('pre').innerHTML = "0";
    } else if (precio.disabled === true) {
        precio.disabled = false;
    }
}

function desabilitar2()
{
    document.getElementById('precioNo').disabled = true;
    document.getElementById('precioNo').value = " ";
}
function habilitar() {
    document.getElementById('precioNo').disabled = false;
}

var slideIndex = 1;
showDivs(slideIndex);
function plusDivs(n) {
    showDivs(slideIndex += n);
}

function currentDiv(n) {
    showDivs(slideIndex = n);
}

function showDivs(n) {
    var i;
    var x = document.getElementsByClassName("mySlides");
    var dots = document.getElementsByClassName("demo");
    if (n > x.length) {
        slideIndex = 1;
    }
    if (n < 1) {
        slideIndex = x.length;
    }
    for (i = 0; i < x.length; i++) {
        x[i].style.display = "none";
    }
    for (i = 0; i < dots.length; i++) {
        dots[i].className = dots[i].className.replace(" w3-white", "");
    }
    x[slideIndex - 1].style.display = "block";
    dots[slideIndex - 1].className += " w3-white";
}

