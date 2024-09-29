var buttonLoad;
var dropDownCountry;
var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;

$(document).ready(function () {
   buttonLoad = $("#buttonLoadCountries");
   dropDownCountry = $("#dropDownCountries");
   buttonAddCountry = $("#buttonAddCountry");
   buttonUpdateCountry = $("#buttonUpdateCountry");
   buttonDeleteCountry = $("#buttonDeleteCountry");
   labelCountryName = $("#labelCountryName");
   fieldCountryName = $("#fieldCountryName");
    fieldCountryCode = $("#fieldCountryCode");

   buttonLoad.click(function () {
      loadCountries();
   });

   dropDownCountry.on("change", function () {
       changeFormStateToSelectedCountry()
   });

   buttonAddCountry.click(function () {
       if (buttonAddCountry.val() === "Add") {
           addCountry();
       } else {
           changeFormStateToNew();
       }
   });

   buttonUpdateCountry.click(function () {
      updateCountry();
   });

   buttonDeleteCountry.click(function () {
       deleteCountry();
   }) ;
});

function deleteCountry() {
    let optionValue = dropDownCountry.val();
    let countryId = optionValue.split("-")[0];

    const URL = `${contextPath}/countries/${countryId}`;

    $.ajax({
        type: 'DELETE',
        url: URL,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function () {
        $("#dropDownCountries option[value='" + optionValue + "']").remove();
        changeFormStateToNew();
        showToastMessage("The country has been deleted");
    }).fail(function () {
        showToastMessage("Error: Could not connect to server or server encountered an error");
    });
}

function updateCountry() {
    let countryId = dropDownCountry.val().split("-")[0];

    const URL = `${contextPath}/countries/${countryId}`;
    let countryName = fieldCountryName.val();
    let countryCode = fieldCountryCode.val();

    let jsonDate = {id: countryId, name: countryName, code: countryCode};

    $.ajax({
        type: 'PUT',
        url: URL,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonDate),
        contentType: 'application/json',
    }).done(function () {
        let $dropDownCountries = $("#dropDownCountries option:selected");
        $dropDownCountries.val(countryCode);
        $dropDownCountries.text(countryName);
        showToastMessage("The country has been updated");

        changeFormStateToNew();
    }).fail(function () {
        showToastMessage("Error: Could not connect to server or server encountered an error");
    });
}

function changeFormStateToSelectedCountry() {
    buttonAddCountry.prop("value", "New");
    buttonUpdateCountry.prop("disabled", false);
    buttonUpdateCountry.prop("disabled", false);
    buttonDeleteCountry.prop("disabled", false);

    labelCountryName.text("Selected Country:");

    let selectedCountryName = $("#dropDownCountries option:selected").text();
    fieldCountryName.val(selectedCountryName);

    let countryCode = dropDownCountry.val().split("-")[1];
    fieldCountryCode.val(countryCode);
}

function changeFormStateToNew() {
    buttonAddCountry.val("Add");
    labelCountryName.text("Country Name:");

    buttonUpdateCountry.prop("disabled", true);
    buttonDeleteCountry.prop("disabled", true);

    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}

function addCountry() {
     const URL = `${contextPath}/countries`;
     let countryName = fieldCountryName.val();
     let countryCode = fieldCountryCode.val();

     let jsonDate = {name: countryName, code: countryCode};

     $.ajax({
         type: 'POST',
         url: URL,
         beforeSend: function (xhr) {
             xhr.setRequestHeader(csrfHeaderName, csrfValue);

         },
         data: JSON.stringify(jsonDate),
         contentType: 'application/json',
     }).done(function () {
        selectNewlyAddedCountry(countryCode, countryName);
        showToastMessage("The new country has been added");
     }).fail(function () {
         showToastMessage("Error: Could not connect to server or server encountered an error");
     });
}

function selectNewlyAddedCountry(countryCode, countryName) {
    let optionValue =  countryCode;
    $("<option>").val(optionValue).text(countryName).appendTo(dropDownCountry);

    $("#dropDownCountries option[value='" + optionValue + "']").prop("selected", true);

    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}

function loadCountries() {
    const URL = `${contextPath}/countries`;
    $.get(URL, function (responseJSON) {
       dropDownCountry.empty();

       $.each(responseJSON, function (index, country) {
         let optionValue = country.id + "-" + country.code;
         $("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry)
       });
    }).done(function () {
        buttonLoad.val("Refresh Country list");
        showToastMessage("All countries have been loaded");
    }).fail(function () {
        showToastMessage("Error: Could not connect to server or server encountered an error");
    });
}

function showToastMessage(message) {
    $("#toastMessage").text(message);
    $(".toast").show()
}