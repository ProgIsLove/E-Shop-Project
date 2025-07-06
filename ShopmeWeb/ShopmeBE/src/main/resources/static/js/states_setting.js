$(document).ready(function () {
    let buttonLoadCountriesForStates = $("#buttonLoadCountriesForStates");
    let dropDownCountriesForStates = $("#dropDownCountriesForStates");
    let dropDownStates = $("#dropDownStates");
    let buttonAddState = $("#buttonAddState");
    let buttonUpdateState = $("#buttonUpdateState");
    let buttonDeleteState = $("#buttonDeleteState");
    let labelStateName = $("#labelStateName");
    let fieldStateName = $("#fieldStateName");

    buttonLoadCountriesForStates.click(function () {
        loadCountriesForStates(buttonLoadCountriesForStates, dropDownCountriesForStates);
    });

    dropDownCountriesForStates.on("change", function () {
        loadStatesForCountry(dropDownStates);
    });

    dropDownStates.on("change", function () {
        changeFormStateToSelectedState(dropDownStates,
            buttonAddState,
            buttonUpdateState,
            buttonDeleteState,
            labelStateName,
            fieldStateName)
    });

    buttonAddState.click(function () {
        if (buttonAddState.val() === "Add") {
            addState(fieldStateName, dropDownStates);
        } else {
            changeFormStateToNew(buttonUpdateState,
                buttonAddState,
                buttonDeleteState,
                labelStateName,
                fieldStateName);
        }
    });

    buttonUpdateState.click(function () {
        updateState(fieldStateName,
            dropDownStates);
    })

    buttonDeleteState.click(function () {
        deleteState(dropDownStates,
            buttonUpdateState,
            buttonAddState,
            buttonDeleteState,
            labelStateName,
            fieldStateName);
    });
});

function addState(fieldStateName, dropDownStates) {
    if (!validateFormState()) return;

    const URL = `${contextPath}/states`;
    let stateName = fieldStateName.val().trim();

    let selectedCountry = $("#dropDownCountriesForStates option:selected");
    let countryId = selectedCountry.val();
    let countryName = selectedCountry.text();

    let jsonData = {name: stateName, countryDTO: {id: countryId, countryName: countryName}};

    $.ajax({
        type: 'POST',
        url: URL,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json',
    }).done(function (response) {
        selectNewlyAddedState(response.id, response.name, fieldStateName, dropDownStates);
        showToastMessage(`The new state has been added`);
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

function updateState(fieldStateName, dropDownStates) {
    if (!validateFormState()) return;

    let stateId = dropDownStates.val();
    const URL = `${contextPath}/states/${stateId}`;
    let stateName = fieldStateName.val().trim();

    let selectedCountry = $("#dropDownCountriesForStates option:selected");
    let countryId = selectedCountry.val();
    let countryName = selectedCountry.text();

    let jsonData = {id: stateId, name: stateName, countryDTO: {id: countryId, name: countryName}};

    $.ajax({
        type: 'PUT',
        url: URL,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json',
    }).done(function (response) {
        $("#dropDownStates option:selected").text(response.name);
        showToastMessage("The state has been updated");
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
    })

}

function deleteState(dropDownStates,
                     buttonUpdateState,
                     buttonAddState,
                     buttonDeleteState,
                     labelStateName,
                     fieldStateName) {
    let stateId = dropDownStates.val();
    console.log(stateId)
    const URL = `${contextPath}/states/${stateId}`;

    $.ajax({
        type: 'DELETE',
        url: URL,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function () {
        $("#dropDownStates option[value='" + stateId + "']").remove();
        changeFormStateToNew(buttonUpdateState,
            buttonAddState,
            buttonDeleteState,
            labelStateName,
            fieldStateName)
        showToastMessage("The state has been deleted");
    }).fail(function () {
        showToastMessage("Error: Could not connect to server or server encountered an error");
    });
}

function changeFormStateToSelectedState(dropDownStates, buttonAddState,
                                        buttonUpdateState,
                                        buttonDeleteState,
                                        labelStateName,
                                        fieldStateName) {
    buttonAddState.prop("value", "New");
    buttonUpdateState.prop("disabled", false);
    buttonDeleteState.prop("disabled", false);

    labelStateName.text("State/Province Name:");

    let selectedStateName = $("#dropDownStates option:selected").text();
    fieldStateName.val(selectedStateName);
}

function changeFormStateToNew(buttonUpdateState,
                              buttonAddState,
                              buttonDeleteState,
                              labelStateName,
                              fieldStateName) {
    buttonAddState.val("Add");
    labelStateName.text("State/Province Name:");

    buttonUpdateState.prop("disabled", true);
    buttonDeleteState.prop("disabled", true);

    fieldStateName.val("").focus();
}

function validateFormState() {
    const FORM_COUNTRY = document.getElementById("formState");
    if (!FORM_COUNTRY.checkValidity()) {
        FORM_COUNTRY.reportValidity();
        return false;
    }
    return true;
}

function selectNewlyAddedState(stateId, stateName, fieldStateName, dropDownStates) {
    $("<option>").val(stateId).text(stateName).appendTo(dropDownStates);

    $("#dropDownStates option[value='" + stateId + "']").prop("selected", true);

    fieldStateName.val("").focus();
}

function loadStatesForCountry(dropDownStates) {
    let selectedCountry = $("#dropDownCountriesForStates option:selected");
    let countryId = selectedCountry.val();
    const URL = `${contextPath}/states/${countryId}`;


    $.get(URL, function (responseJSON) {
        dropDownStates.empty();

        $.each(responseJSON, function (index, state) {
            $("<option>").val(state.id).text(state.name).appendTo(dropDownStates);
        });
    }).done(function () {
        showToastMessage(`All states have been loaded for country ${selectedCountry.text()}`);
    }).fail(function () {
        showToastMessage("Error: Could not connect to server or server encountered an error");
    });
}

function loadCountriesForStates(buttonLoadCountriesForStates, dropDownCountriesForStates) {
    const URL = `${contextPath}/countries`;

    $.get(URL, function (responseJSON) {
        dropDownCountriesForStates.empty();

        $.each(responseJSON, function (index, country) {
            $("<option>").val(country.id).text(country.name).appendTo(dropDownCountriesForStates);
        });
    }).done(function () {
        buttonLoadCountriesForStates.val("Refresh Country List");
        showToastMessage("All countries have been loaded");
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}