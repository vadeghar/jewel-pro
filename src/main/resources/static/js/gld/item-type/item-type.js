var url = baseUrl+'/itemtype';


$(document).ready(function(){

$('#saveItemTypeBtn').on('click', function(e){
    e.preventDefault();
    validateForm();
    save();
});

var itemTypeId = $('#id').val();
if(itemTypeId) {
    loadItemType(itemTypeId);
}

});

function loadItemType(itemTypeId) {
    simpleCall(url+'/'+itemTypeId, 'get', '', '', '', loadCallback);
}

function loadCallback(response) {
    $('#itemType').val(response.itemType);
    $('#description').val(response.description);
}

function validateForm() {

}

function save() {
    var formData = $("#itemTypeForm").serializeArray();
    var formObject = {};
    $.each(formData, function(index, field) {
        formObject[field.name] = field.value;
    });
    var jsonData = JSON.stringify(formObject);
    console.log(jsonData);
    simpleCall(url, 'post', '', '', jsonData, saveCallback);
}

function saveCallback(response) {
    $('#id').val(response.id);
    $('#alert').html('<div class="alert alert-success fade show" role="alert">'+
                        '<strong>Success!</strong> Item type '+response.itemType+' is successfully saved.'+
                      '</div>');
}