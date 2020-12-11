addEventListener('load', inicio, false);

$(document).ready(function () {
    $('[data-toggle="popover"]').popover();
});


$('#pop').popover().click(function () {
    setTimeout(function () {
        $('#pop').popover('hide');
    }, 5000);
});


function inicio()
{

    document.getElementById('precio').addEventListener('change', cambioPrecio, false);
    document.getElementById('rangoSinPrecio').addEventListener('click', desabilitar, false);
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
checkbox.addEventListener('change', function () {
    if (this.checked) {
        btnUserRegister.removeAttribute('disabled');
    } else {
        btnUserRegister.setAttribute('disabled', "true");
    }
});

//POPOVER





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

