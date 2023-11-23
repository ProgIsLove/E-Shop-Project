$(document).ready(function () {
    $('#btnCancel').on("click", function () {
        window.location = moduleURL;
    });

    $('#fileImage').change(function () {
        let fileSize = this.files[0].size;
        const MEGABYTE = 1048576

        if (fileSize > MEGABYTE) {
            this.setCustomValidity("You must choose an image less than 1MB!");
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