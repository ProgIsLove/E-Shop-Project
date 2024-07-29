$(document).ready(function () {
    $("a[id='linkRemoveDetail']").each(function () {
        let id = $(this).closest("[id^='divDetail']").attr('id');
        $(this).click(function () {
            removeDetailSectionById(id);
        });
    });
});

function addNextDetailSection() {
    let allDivDetails = $("[id^='divDetail']");
    let divDetailsCount = allDivDetails.length;

    let htmlDetailSection = `
     <div class="row col-sm-8 mb-3 mt-3" id="divDetail${divDetailsCount}">
        <input type="hidden" name="detailIDs" value="0" />
        <label for="detailNames" class="col-sm-1 col-form-label">Name:</label>
        <div class="col-sm-3">
            <input type="text" class="form-control" name="detailNames" maxlength="255" id="detailNames">
        </div>
        <label for="detailValues" class="col-sm-1 col-form-label">Value:</label>
        <div class="col-sm-3">
            <input type="text" class="form-control" name="detailValues" maxlength="255" id="detailValues">
        </div>
    </div>`;

    $(`#divProductDetails`).append(htmlDetailSection);

    let previousDivDetailSection = allDivDetails.last();
    let previousDivDetailID = previousDivDetailSection.attr(`id`);

    let htmlLinkRemove = `
        <a class="btn fa fa-times-circle fa-2x icon-dark col-sm-1" 
        href="javascript:removeDetailSectionById('${previousDivDetailID}')"
        title="Remove this detail"></a>
    `;

    previousDivDetailSection.append(htmlLinkRemove);
}

function removeDetailSectionById(id) {
    $("#" + id).remove();
}
