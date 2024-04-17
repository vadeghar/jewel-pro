function simpleCall(url, method, title, tabId, requestData, responseCallback) {
	doSignAndSend({
		url: url,
		type: method,
		contentType: 'application/json',
		data: requestData,
		beforeSend: function() {
            $("#spinner-div").show();
		},
		success: function(data, textStatus, jqXHR) {
		    $("#spinner-div").hide();
			responseCallback(data);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			responseCallback(null);
			$("#spinner-div").hide();
		},
		complete: function() {
		    $("#spinner-div").hide();
		}
	});
}


function doSignAndSend(settings) {
	$.ajax(settings);
}