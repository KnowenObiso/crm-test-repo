onPopupOpened = function (evt) {
	    evt.cancel();
	    var customEvent = AdfCustomEvent.queue(evt.getSource(), "handleCommandProcessing",
	    {
	    },
	false);
	}