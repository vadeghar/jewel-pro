//var _reportContext = '/permissions'
//var url = baseUrl + _reportContext;
//$(document).ready(function() {
//
//    loadPermissionGroup();
//
//})
//$('#permissionGroupId').on('change keyup', function() {
//    $('#prefix').val($('#permissionGroupId option:selected').text());
//    $('#effectivePermissionName').text($('#prefix').val()+'_'+$('#name').val());
//})
//$('#name').on('input', function(){
//    $('#effectivePermissionName').text($('#prefix').val()+'_'+$(this).val());
//});
//$('#savePermission').on('click', function(){
//    var reportRequest = formToJson('#permissionForm');
//    reportRequest['permissionGroup'] = {};
//    reportRequest.permissionGroup['id'] = $('#permissionGroupId').val();
//    console.log(JSON.stringify(reportRequest));
//    simpleCall(url, 'post', '', '', JSON.stringify(reportRequest), saveCallback);
//})
//
//function saveCallback(response) {
//    $('#id').val(response.id);
//    showSuccessMsg('Permission: '+response.name+' successfully saved.')
//}
//
//function loadPermission(response) {
//    $('#id').val(response.id);
//    $('#name').val(response.name);
//    $('#desc').val(response.desc);
//    $('#permissionGroupId').val(response.permissionGroupId);
//    $('#prefix').val($('#permissionGroupId option:selected').text());
//    $('#effectivePermissionName').text($('#prefix').val()+'_'+$('#name').val());
//}
//
//function loadPermissionGroup() {
//    simpleCall(baseUrl + '/permission-groups', 'get', '', '', '', setPermissionGroups);
//}
//
//function setPermissionGroups(response) {
//    console.log(JSON.stringify(response));
//    var select = $('#permissionGroupId');
//        select.empty();
//        select.append('<option value="">Select</option>');
//        $.each(response, function(index, group) {
//            select.append('<option value="' + group.id + '">' + group.name + '</option>');
//        });
//
//    var urlParams = getUrlVars();
//    if(urlParams.id) {
//        // Get the value of the 'id' parameter
//        id = urlParams.id;
//        console.log("ID:", id);
//        simpleCall(url+'/'+id, 'get', '', '', '', loadPermission);
//    }
//}