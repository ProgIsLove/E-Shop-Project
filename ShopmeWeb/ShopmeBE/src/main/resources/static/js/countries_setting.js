var buttonLoad;
var dropDownCountry;

$(document).ready(function () {
   buttonLoad = $("#buttonLoadCountries");
   dropDownCountry = $("#dropDownCountries");

   buttonLoad.click(function () {
      loadCountries();
   });
});

function loadCountries() {
    const URL = `${contextPath}/countries`;
    $.get(URL, function (responseJSON) {
       dropDownCountry.empty();

       $.each(responseJSON, function (index, country) {
         let optionValue = country.id + "-" + country.code;
         alert(optionValue);
       });
    });
}