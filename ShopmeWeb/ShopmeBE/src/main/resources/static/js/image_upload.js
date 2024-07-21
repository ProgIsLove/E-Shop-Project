$(document).ready(function () {
    $('#btnCancel').on("click", function () {
        window.location = MODULE_URL;
    });

    $('#fileImage').change(function () {
        if(!checkFileSize(this)) {
            return;
        }
        showImageThumbnail(this);
    });
});

function showImageThumbnail(fileInput) {
    let file = fileInput.files[0];
    let reader = new FileReader();
    reader.onload = function (e) {
        $('#thumbnail').attr("src", e.target.result);
    };

    reader.readAsDataURL(file);
}

function checkFileSize(fileInput) {
    let fileSize = fileInput.files[0].size;

    if (fileSize > MAX_FILE_SIZE) {
        fileInput.setCustomValidity(`You must choose an image less than ${MAX_FILE_SIZE} bytes!`);
        fileInput.reportValidity();
        return false;
    } else {
        fileInput.setCustomValidity("");

        return true;
    }
}