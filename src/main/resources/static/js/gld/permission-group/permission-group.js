//var _reportContext = '/permission-groups'
//var url = baseUrl + _reportContext;
//$(document).ready(function() {
//    var urlParams = getUrlVars();
//    if(urlParams.id) {
//        // Get the value of the 'id' parameter
//        id = urlParams.id;
//        console.log("ID:", id);
//        simpleCall(url+'/'+id, 'get', '', '', '', loadPermission);
//    }
//})
//$('#savePermissionGroup').on('click', function(){
//    var reportRequest = formToJson('#permissionGroupForm');
//    console.log(JSON.stringify(reportRequest));
//    simpleCall(url, 'post', '', '', JSON.stringify(reportRequest), saveCallback);
//})
//
//function saveCallback(response) {
//    $('#id').val(response.id);
//    showSuccessMsg('Permission Group: '+response.name+' successfully saved.')
//}
//
//function loadPermission(response) {
//    $('#id').val(response.id);
//    $('#name').val(response.name);
//    $('#desc').val(response.desc);
//}