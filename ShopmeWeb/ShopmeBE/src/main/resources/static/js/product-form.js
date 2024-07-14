var extraImagesCount = 0;
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
    getCategories();

    $("input[name='extraImage']").each(function (index) {
        $(this).change(function () {
            checkImageSize(this, index);
            // showExtraImageThumbnail(this, index);
        })
    });
});

function showExtraImageThumbnail(fileInput, index) {
    let file = fileInput.files[0];

    let fileName = file.name;

    let imageNameHiddenField = $("#imageName" + index);
    if (imageNameHiddenField.length) {
        imageNameHiddenField.val(fileName);
    }

    let reader = new FileReader();
    reader.onload = function (e) {
        $('#extraThumbnail' + index).attr("src", e.target.result);
    };

    reader.readAsDataURL(file);

    console.log(`Index ${index} and counter ${extraImagesCount}`)
    if (index > extraImagesCount - 1) {
        addNextExtraImageSection(index + 1);
    }
}

function addNextExtraImageSection(index) {
    let htmlExtraImage = `
        <div class="col border m-3 p-2" id="divExtraImage${index}">
            <div id="extraImageHeader${index}"><label>Extra Image #${index + 1}:</label></div>
            <div class="m-2">
                <img id="extraThumbnail${index}" alt="Extra image #${index + 1} preview" class="img-fluid"
                src="${DEFAULT_IMAGE_THUMBNAIL_SRC}"/>
            </div>
            <div>
                <input type="file" name="extraImage"
                       onchange="checkImageSize(this, ${index})"
                       accept="image/png, image/jpeg" />
            </div>
        </div>
    `;

    let htmlLinkRemove = `
        <a class="btn fa fa-times-circle fa-2x icon-dark float-end" 
        href="javascript:removeExtraImage(${index - 1})"
        title="Remove this image"></a>
    `;


    $(`#divProductImages`).append(htmlExtraImage);
    $(`#extraImageHeader` + (index - 1)).append(htmlLinkRemove);

    extraImagesCount++;
    console.log(extraImagesCount);
}

function removeExtraImage(index) {
    $(`#divExtraImage` + index).remove();
}

function checkImageSize(fileInput, index) {
        if (fileInput.files[0].size > MAX_FILE_SIZE) {
            $(fileInput).val(null);
            fileInput.setCustomValidity(`You must choose an image less than ${(MAX_FILE_SIZE / 1000)} KB!`);
            fileInput.reportValidity();
            $("#extraThumbnail" + index).attr("src", DEFAULT_IMAGE_THUMBNAIL_SRC);
            return;
        }
        showExtraImageThumbnail(fileInput, index);
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