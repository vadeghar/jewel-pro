$(document).ready(function(){
    $('.existingItem').change(function(){
        if($(this).val() == 'YES'){
            $('#itemTagNoId').removeClass('d-none')
        } else {
            $('#itemTagNoId').addClass('d-none')
            $('#itemContent').removeClass('d-none')
        }
    });

    if($('.existingItem').val() == 'YES'){
        $('#itemTagNoId').removeClass('d-none')
        $('#itemContent').removeClass('d-none')
    } if($('.existingItem').val() == 'NO'){
//          $('#itemTagNoId').removeClass('d-none')
          $('#itemContent').removeClass('d-none')
      } else {
        $('#itemTagNoId').addClass('d-none')
        $('#itemContent').removeClass('d-none')
    }

    $("#saveEstimation").click(function(){
        // Change the action attribute of the form
        $("#spinner-div").show();
        var formData = $("#estimationForm").serializeArray();
        var jsonData = {};
        $.each(formData, function() {
            if (jsonData[this.name]) {
                if (!jsonData[this.name].push) {
                    jsonData[this.name] = [jsonData[this.name]];
                }
                jsonData[this.name].push(this.value || '');
            } else {
                jsonData[this.name] = this.value || '';
            }
        });

        // Convert JSON object to a JSON string
        var jsonData = JSON.stringify(jsonData);
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/estimation/save", // Change the URL to the desired endpoint
            data: jsonData,
            contentType: "application/json",
            success: function(response){
                // Handle success response
                console.log("AJAX request successful!");
                $('#saveEstimation').addClass('d-none');
                $('#saveAndPrintEstimation').addClass('d-none');
                console.log("Response:", JSON.stringify(response));
                $("#spinner-div").hide();
            },
            error: function(xhr, status, error){
                // Handle error response
                console.log("AJAX request failed!");
                console.log("Error:", error);
                $("#spinner-div").hide();
            }
        });
    });


    $("#printEstimation").click(function(){
        // Change the action attribute of the form
        $("#spinner-div").show();
        var formData = $("#estimationForm").serializeArray();
        var jsonData = {};
        $.each(formData, function() {
            if (jsonData[this.name]) {
                if (!jsonData[this.name].push) {
                    jsonData[this.name] = [jsonData[this.name]];
                }
                jsonData[this.name].push(this.value || '');
            } else {
                jsonData[this.name] = this.value || '';
            }
        });

        // Convert JSON object to a JSON string
        var jsonData = JSON.stringify(jsonData);
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/estimation/print", // Change the URL to the desired endpoint
            data: jsonData,
            contentType: "application/json",
            success: function(response){
                // Handle success response
                console.log("AJAX request successful!");
                $('#saveEstimation').addClass('d-none');
                $('#printEstimation').addClass('d-none');
                $('#saveAndPrintEstimation').addClass('d-none');
                console.log("Response:", JSON.stringify(response));
                $("#spinner-div").hide();
            },
            error: function(xhr, status, error){
                // Handle error response
                console.log("AJAX request failed!");
                console.log("Error:", error);
                $("#spinner-div").hide();
            }
        });
    });

});