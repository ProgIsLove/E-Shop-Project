$(document).ready(function () {
    let buttonLoad = $("#buttonLoadCountries");
    let dropDownCountry = $("#dropDownCountries");
    let buttonAddCountry = $("#buttonAddCountry");
    let buttonUpdateCountry = $("#buttonUpdateCountry");
    let buttonDeleteCountry = $("#buttonDeleteCountry");
    let labelCountryName = $("#labelCountryName");
    let fieldCountryName = $("#fieldCountryName");
    let fieldCountryCode = $("#fieldCountryCode");

    buttonLoad.click(function () {
        loadCountries(buttonLoad, dropDownCountry);
    });

    dropDownCountry.on("change", function () {
        changeFormStateToSelectedCountry(dropDownCountry,
            buttonAddCountry,
            buttonUpdateCountry,
            buttonDeleteCountry,
            labelCountryName,
            fieldCountryName,
            fieldCountryCode)
    });

    buttonAddCountry.click(function () {
        if (buttonAddCountry.val() === "Add") {
            addCountry(dropDownCountry,
                fieldCountryName,
                fieldCountryCode,
                buttonAddCountry);
        } else {
            changeFormStateToNew(buttonAddCountry,
                buttonUpdateCountry,
                buttonDeleteCountry,
                labelCountryName,
                fieldCountryName,
                fieldCountryCode);
        }
    });

    buttonUpdateCountry.click(function () {
        updateCountry(dropDownCountry,
            buttonAddCountry,
            buttonUpdateCountry,
            buttonDeleteCountry,
            labelCountryName,
            fieldCountryName,
            fieldCountryCode);
    });

    buttonDeleteCountry.click(function () {
        deleteCountry(dropDownCountry,
            buttonAddCountry,
            buttonUpdateCountry,
            buttonDeleteCountry,
            labelCountryName,
            fieldCountryName,
            fieldCountryCode);
    });
});

function deleteCountry(dropDownCountry,
                       buttonAddCountry,
                       buttonUpdateCountry,
                       buttonDeleteCountry,
                       labelCountryName,
                       fieldCountryName,
                       fieldCountryCode) {
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
        changeFormStateToNew(buttonAddCountry,
            buttonUpdateCountry,
            buttonDeleteCountry,
            labelCountryName,
            fieldCountryName,
            fieldCountryCode);
        showToastMessage("The country has been deleted");
    }).fail(function () {
        showToastMessage("Error: Could not connect to server or server encountered an error");
    });
}

function updateCountry(dropDownCountry,
                       buttonAddCountry,
                       buttonUpdateCountry,
                       buttonDeleteCountry,
                       labelCountryName,
                       fieldCountryName,
                       fieldCountryCode) {
    let countryId = dropDownCountry.val().split("-")[0];
    const URL = `${contextPath}/countries/${countryId}`;

    let countryName = fieldCountryName.val().trim()
    let countryCode = fieldCountryCode.val().replaceAll(/\s/g,'').toUpperCase()

    let jsonData = {id: countryId, name: countryName, code: countryCode};

    $.ajax({
        type: 'PUT',
        url: URL,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json',
    }).done(function (response) {
        let $dropDownCountries = $("#dropDownCountries option:selected");
        $dropDownCountries.val(`${response.id} - ${response.code}`);
        $dropDownCountries.text(response.name);
        showToastMessage("The country has been updated");

        changeFormStateToNew(buttonAddCountry,
            buttonUpdateCountry,
            buttonDeleteCountry,
            labelCountryName,
            fieldCountryName,
            fieldCountryCode);
    }).fail(function (xhr) {
        const statusHandlers = {
            409: () => {
                let response = xhr.responseJSON;
                showToastMessage(`${response.title} - ${response.details}`);
            },
            400: () => {
                let response = xhr.responseJSON;
                showToastMessage(`${response.code}`);
            }
        };

        const handler = statusHandlers[xhr.status];
        if (handler) {
            handler();
        } else {
            showToastMessage("Error: Could not connect to server or server encountered an error");
        }
    });
}

function changeFormStateToSelectedCountry(dropDownCountry,
                                          buttonAddCountry,
                                          buttonUpdateCountry,
                                          buttonDeleteCountry,
                                          labelCountryName,
                                          fieldCountryName,
                                          fieldCountryCode) {
    buttonAddCountry.prop("value", "New");
    buttonUpdateCountry.prop("disabled", false);
    buttonDeleteCountry.prop("disabled", false);

    labelCountryName.text("Selected Country:");

    let selectedCountryName = $("#dropDownCountries option:selected").text();
    fieldCountryName.val(selectedCountryName);

    let countryCode = dropDownCountry.val().split("-")[1]
        .replaceAll(/\s/g,'')
        .toUpperCase();
    fieldCountryCode.val(countryCode);
}

function changeFormStateToNew(buttonAddCountry,
                              buttonUpdateCountry,
                              buttonDeleteCountry,
                              labelCountryName,
                              fieldCountryName,
                              fieldCountryCode) {
    buttonAddCountry.val("Add");
    labelCountryName.text("Country Name:");

    buttonUpdateCountry.prop("disabled", true);
    buttonDeleteCountry.prop("disabled", true);

    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}

function addCountry(dropDownCountry,
                    fieldCountryName,
                    fieldCountryCode,
                    buttonAddCountry) {
    const URL = `${contextPath}/countries`;

    let countryName = fieldCountryName.val().trim();
    let countryCode = fieldCountryCode.val().replaceAll(/\s/g,'').toUpperCase();

    buttonAddCountry.disabled = countryName === "" && countryCode === "";

    let jsonData = {name: countryName, code: countryCode};

    $.ajax({
        type: 'POST',
        url: URL,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json',
    }).done(function (response) {
        let countryId = response.id;
        selectNewlyAddedCountry(countryId,
            countryCode,
            countryName,
            dropDownCountry,
            fieldCountryName,
            fieldCountryCode);
        showToastMessage("The new country has been added");
    }).fail(function (xhr) {
        const statusHandlers = {
            409: () => {
                let response = xhr.responseJSON;
                showToastMessage(`${response.title} - ${response.details}`);
            },
            400: () => {
                let response = xhr.responseJSON;
                showToastMessage(`${response.code}`);
            }
        };

        const handler = statusHandlers[xhr.status];
        if (handler) {
            handler();
        } else {
            showToastMessage("Error: Could not connect to server or server encountered an error");
        }
    });
}

function selectNewlyAddedCountry(countryId,
                                 countryCode,
                                 countryName,
                                 dropDownCountry,
                                 fieldCountryName,
                                 fieldCountryCode) {

    let optionValue = `${countryId} - ${countryCode}`;
    $("<option>").val(optionValue).text(countryName).appendTo(dropDownCountry);

    $("#dropDownCountries option[value='" + optionValue + "']").prop("selected", true);

    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}

function loadCountries(buttonLoad,
                       dropDownCountry) {
    const URL = `${contextPath}/countries`;

    $.get(URL, function (responseJSON) {
        dropDownCountry.empty();

        $.each(responseJSON, function (index, country) {
            let optionValue = `${country.id} - ${country.code}`;
            $("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry);
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