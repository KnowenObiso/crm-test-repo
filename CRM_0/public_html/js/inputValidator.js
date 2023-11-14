$(document).ready(function () {
    initInputVerifier();
});

function initInputVerifier() {
    try {
        var inputArrays = [];
        inputArrays = document.getElementsByTagName("input");
        var lgh = inputArrays.length;
        var input;
        while (lgh--) {
            input = inputArrays[lgh];
            if (input.type != "text" || input.className == 'x2b' || input.className.indexOf('x2b') !=  - 1) {
                continue;
            }
            else {
                document.getElementById("" + input.id.toString()).addEventListener("change", function () {
                    verify(this.value, this.id);
                });
            }
        }
    }
    catch (ex) {
        console.log(ex);
    }
}

function isAlphanumeric(value) {
    return (/[~!#$^&*()|+\=;:"<>\{\}\[\]\\\/]/gi).test(value);
}

function showCharacterError(id) {
    AdfPage.PAGE.clearAllMessages('' + id);
    AdfPage.PAGE.addMessage('' + id, new AdfFacesMessage(AdfFacesMessage.TYPE_ERROR, "Invalid Value", "Special Characters are Not Allowed"));
    AdfPage.PAGE.showMessages('' + id);
    document.getElementById(id).value = '';
}

function verify(value, id) {
    if (value == '') {
        return;
    }
    var spanIds = id.split("::");
    var spanId = spanIds[0];

    if (isAlphanumeric(value)) {
        document.getElementById(spanId).className += " p_AFError";
        showCharacterError(id);
    }
    else {
        var className = document.getElementById(spanId).className;
        if (className.indexOf("p_AFError") >  - 1) {
            document.getElementById(spanId).className = className.replace("p_AFError", "")
            document.getElementById(spanId).title = "";
        }
    }
}