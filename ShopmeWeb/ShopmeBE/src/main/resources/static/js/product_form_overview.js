const DROP_DOWN_BRANDS = $("#brand");
const DROP_DOWN_CATEGORIES = $("#category");


$(document).ready(function () {

    $("#shortDescription").richText();
    $("#fullDescription").richText();


    DROP_DOWN_BRANDS.change(function () {
        DROP_DOWN_CATEGORIES.empty();
        getCategories();
    });
    //categories to show the categories of the first brand
    getCategoriesForNewForm();
});

function getCategoriesForNewForm() {
    let categoryIdField = $('#categoryId');
    let editMode = false;

    if (categoryIdField.length) {
        editMode = true;
    }

    if (!editMode) return getCategories();
}

function getCategories() {
    const brandId = DROP_DOWN_BRANDS.val();
    const url = `${BRAND_MODULE_URL}/${brandId}/categories`;

    $.get(url, (responseJson) => {
        responseJson.forEach((category) => {
            $("<option>")
                .val(category.id)
                .text(category.name)
                .appendTo(DROP_DOWN_CATEGORIES);
        });
    });
}

function checkUnique(form) {
    let requestData = {
        id: $("#id").val(),
        name: $("#name").val().trim(),
    };

    let csrfToken = $("input[name='_csrf']").val();

    $.post({
        url: CHECK_UNIQUE_PRODUCT,
        contentType: "application/json",
        data: JSON.stringify(requestData),
        dataTYpe: "json",
        headers: {
            "X-CSRF-TOKEN": csrfToken
        },
        success: function (response) {
            switch (response.status) {
                case "OK":
                    form.submit();
                    break;
                default:
                    showErrorModal(`Unknown response from server`);
                    break;
            }
        },
        error: function (xhr, status, error) {
            if (xhr.status === 409) {
                let response = JSON.parse(xhr.responseText);
                showWarningModal(response.details);
            } else {
                showErrorModal(`Could not connect to the sever`);
            }
        }
    });

    return false;
}

$(document).ready(function() {
    $('form').on('submit', function() {
        return checkUnique(this);
    });
});

function showModalDialog(title, message) {
    $('#modalTitle').text(title);
    $('#modalBody').text(message);
    $('#modalDialog').show();
    $('#modalDialogClose, #modalDialogCross').on('click', function () {
        $('#modalDialog').hide();
    })
}

function showErrorModal(message) {
    showModalDialog("Error", message);
}

function showWarningModal(message) {
    showModalDialog("Warning", message)
}