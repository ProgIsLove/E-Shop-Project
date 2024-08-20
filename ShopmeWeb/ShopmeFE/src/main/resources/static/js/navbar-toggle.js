document.addEventListener('DOMContentLoaded', function() {
    const toggleButton = document.getElementById('toggleButton');

    if (toggleButton) {
        toggleButton.addEventListener('click', function () {
            toggleCollapse('topNavbar');
            toggleCollapse('searchNavbar');
        });
    }
});

function toggleCollapse(elementId) {
    const element = document.getElementById(elementId);

    if (element) {
        const collapseInstance = new bootstrap.Collapse(element);

        if (element.classList.contains('show')) {
            collapseInstance.hide();
        } else {
            collapseInstance.show();
        }
    }
}


