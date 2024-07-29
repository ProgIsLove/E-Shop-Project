var imageStack = [];

$(document).ready(function () {
    $("input[name='extraImage']").each(function (index) {
        $(this).change(function () {
            checkImageSize(this, index);
        });
    });

    $("a[id='linkRemoveExtraImage']").each(function () {
        let id = $(this).closest("[id^='divExtraImage']").attr('id');
        $(this).click(function () {
            removeExtraImage(id);
        });
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

    if (index >= imageStack.length) {
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

    imageStack.push(index);
}

function removeExtraImage(id) {
    $(`#` + id).remove();
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