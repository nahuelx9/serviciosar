
addEventListener('load', inicio, false);
function inicio()
{

    document.getElementById('precio').addEventListener('change', cambioPrecio, false);
    document.getElementById('rangoSinPrecio').addEventListener('click', desabilitar, false);

}

function cambioPrecio()
{
    document.getElementById('pre').innerHTML = document.getElementById('precio').value;

}


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
        slideIndex = 1
    }
    if (n < 1) {
        slideIndex = x.length
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
