
window.addEventListener('load', function () {
    if (window.parent) {
        // If in an iframe, notify parent that we have loaded, they can send us
        // any data they need to send
        window.parent.postMessage('orbeonFrameLoaded', '*');
    }
});

window.addEventListener('message', event => {
    // If we are in an iframe and parent sends a message, check if they sent
    // an auth token, then store that in cookies to be used with subsequent
    // requests
    const { accessToken, setToken } = event.data;

    if (setToken !== undefined && typeof setToken === "boolean") {
        if (setToken === true) {
            document.cookie = `${authCookieName}=${accessToken}; path=/`
        } else {
            document.cookie = `${authCookieName}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/`
        }
        window.location.reload()
    }
});

window.addEventListener('DOMContentLoaded', function() {
    // On loading the DOMContent (Orbeon API), we can now embed our orbeon form
    if (window.ORBEON) {
        ORBEON.fr.API.embedForm(
            document.getElementById("my-form"),
            "/orbeon",
            "orbeon",
            selectedForm,
            "new",
            undefined,
            "Authorization='OAuth " + authorizationToken + "'"
        );
    }
});
