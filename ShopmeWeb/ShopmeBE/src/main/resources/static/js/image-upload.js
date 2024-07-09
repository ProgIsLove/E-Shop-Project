$(document).ready(function () {
    $('#btnCancel').on("click", function () {
        window.location = MODULE_URL;
    });

    $('#fileImage').change(function () {
        let fileSize = this.files[0].size;

        if (fileSize > MAX_FILE_SIZE) {
            this.setCustomValidity(`You must choose an image less than ${MAX_FILE_SIZE} bytes!`);
            this.reportValidity();
        } else {
            this.setCustomValidity("");
            showImageThumbnail(this);
        }
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