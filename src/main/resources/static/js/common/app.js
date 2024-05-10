var baseUrl = 'http://localhost:8080/api/v1';

$(document).ready(function () {
    $('#accordionSidebar a').click(function () {
        var navItemId = $(this).attr('id');
        console.log("Clicked nav item ID:", navItemId);
        // You can perform any further actions with the navItemId here
    });

    if($('#menuItem').val()) {
        setActiveCollapseItem($('#menuItem').val())
    }
});

function setActiveCollapseItem(itemId) {
    // Remove "active" class from all collapse-items
    $('.collapse-item').removeClass('active');

    // Add "active" class to the collapse-item with the given ID
    $('#' + itemId).addClass('active');

    // Find the parent collapse div and add the "show" class
    $('#' + itemId).closest('.collapse').addClass('show');

    // Find the parent nav-link and remove the "collapsed" class
    $('#' + itemId).closest('.nav-item').find('.nav-link').removeClass('collapsed');

    // Set aria-expanded attribute to "true"
    $('#' + itemId).closest('.nav-item').find('.nav-link').attr('aria-expanded', true);
}

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

function createDynamicModal(id, title, content, btnText, btnAction) {
    // Create modal markup
    var modalMarkup = `
      <div class="modal fade" id="dynamicModal" tabindex="-1" role="dialog" aria-labelledby="dynamicModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="dynamicModalLabel">${title}</h5>
              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              ${content}
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
              <button type="button" class="btn btn-primary" id="dynamicModalBtn">${btnText}</button>
            </div>
          </div>
        </div>
      </div>
    `;

    // Append modal markup to body
    $("body").append(modalMarkup);

    // Show the modal
    $("#dynamicModal").modal('show');

    // Attach button action
    $("#dynamicModalBtn").click(function() {
        $("#dynamicModal").modal('hide');
      btnAction(id); // Pass content to the button action function

    });
  }


function toCurrency(val) {
    if(val == null || val == '') {
        return '0.00'
    } else {
        return parseFloat(val).toFixed(2);
    }
}

function toWt(val) {
    if(val == null || val == '') {
        return '0.000'
    } else {
        return parseFloat(val).toFixed(3);
    }
}

function navigateWindow(url) {
    $(window).attr('location', url)
}
