$(document).ready(() => {
    $('#logoutLink').on("click", e => {
        e.preventDefault();
        document.logoutForm.submit();
    });

    customizeDropDownMenu();
    customizeTabs();
});

function customizeDropDownMenu() {
    $(".navbar .dropdown").hover(
        function () {
            $(this).find('.dropdown-menu').first().stop(true, true).delay(250).slideDown();
        },
        function () {
            $(this).find('.dropdown-menu').first().stop(true, true).delay(100).slideUp();
        }
    );

    $(".dropdown > a").click(function () {
        location.href = this.href;
    });
}

function customizeTabs() {
    // Function to activate the correct tab based on hash in the URL
    function activateTabFromHash(hash) {
        if (hash) {
            // Select the tab link that matches the hash
            let tabTriggerEl = document.querySelector(`.nav-tabs button[data-bs-target="#${hash}"]`);
            if (tabTriggerEl) {
                // Create or retrieve the Bootstrap tab instance and show the tab
                let tab = bootstrap.Tab.getOrCreateInstance(tabTriggerEl);
                tab.show();
            }
        }
    }

    // Initial activation based on URL hash when the page loads
    const url = document.location.toString();
    const baseUrl = new URL(url).pathname; // Get the base path without the hash
    const hash = url.includes('#') ? url.split('#')[1] : null;

    // Activate a tab only if we're on the settings page and a hash exists
    if (baseUrl.includes('/settings') && hash) {
        activateTabFromHash(hash);
    }

    // Update the URL hash when a new tab is shown
    document.querySelectorAll('.nav-tabs button[data-bs-toggle="tab"]').forEach(tabLink => {
        tabLink.addEventListener('shown.bs.tab', function (e) {
            // Update the hash in the URL without reloading the page
            const newHash = e.target.dataset.bsTarget.substring(1); // Remove the "#" prefix
            history.replaceState(null, null, `${baseUrl}#${newHash}`);
        });
    });

    // Handle dropdown-item click to navigate tabs
    document.querySelectorAll('.dropdown-item.settings').forEach(dropdownItem => {
        dropdownItem.addEventListener('click', function (e) {
            e.preventDefault(); // Prevent default link behavior to avoid page reload
            const href = this.getAttribute('href');
            const [path, hashPart] = href.split('#');
            const targetPath = new URL(path, document.location.origin).pathname;
            const targetHash = hashPart ? hashPart : null;

            // Navigate to settings only if we're on the same base path
            if (baseUrl.includes('/settings') && targetHash) {
                activateTabFromHash(targetHash);
                history.replaceState(null, null, `${targetPath}#${targetHash}`);
            } else {
                // Navigate to the correct base path (e.g., /brands or /settings)
                document.location.href = href;
            }
        });
    });
}


