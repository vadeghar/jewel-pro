var _customerContext = '/permissions'
var url = baseUrl + _customerContext;

$(document).ready(function() {
    loadPermissionGroup();
    simpleCall(url, 'get', '', '', '', loadDatatable);
});

function loadPermissionGroup() {
    simpleCall(baseUrl + '/permission-groups', 'get', '', '', '', setPermissionGroups);
}

function loadDatatable(response) {
    $('#permissionsTable').DataTable().destroy();
    $('#permissionsTable').DataTable({
        data: response,
        columns: [
            {data: 'permissionGroupName', render: function (data, type, row) {return data != undefined ? data : ''}},
            {data: 'name'},
            {data: 'desc'},
            {
              data: 'id',
              render: function (data, type, row) {
                  return '<button class="btn btn-sm btn-primary editRowCls" data-id="' + data +'" title="Edit Permission"><i class="fas fa-edit"></i></button> '+
                  '<button class="btn btn-sm btn-danger deleteRowCls" data-id="' + data +'" title="Delete Permission"><i class="fas fa-trash"></i></button> ';
              }
            }
        ],
        responsive: true,
        "columnDefs": [{
                "targets": 'no-sort', // Target the columns with the class 'no-sort'
                "orderable": false,   // Disable sorting for these columns
            }],
        order: [[0, 'asc']]
    });
}

$(document).on('click', '.editRowCls', function() {
    var id = $(this).data('id');
    simpleCall(url+'/'+id, 'get', '', '', '', loadPermission);
});

$(document).on('click', '.deleteRowCls', function() {
    var id = $(this).data('id');
    createDynamicModal(id, "Delete Permission Group?.", "Are you sure want to delete?", "Yes", deletePermission)
});


$('#savePermission').on('click', function(){
    var reportRequest = formToJson('#permissionForm');
    simpleCall(url, 'post', '', '', JSON.stringify(reportRequest), saveCallback);
})
$(document).on('click', '#resetPermission', function() {
    resetForm('#permissionForm');
    $('#cardHeader').text('New Permission')
});
$('#permissionGroupId').on('change keyup', function() {
    $('#prefix').val($('#permissionGroupId option:selected').text());
    $('#effectivePermissionName').text($('#prefix').val()+'_'+$('#name').val());
})
function saveCallback(response) {
    $('#id').val('');
    $('#name').val('');
    $('#desc').val('');
    $('#permissionGroupId').val('');
    $('#prefix').val('');
    $('#effectivePermissionName').text('');
    showSuccessMsg('Permission: '+response.name+' successfully saved.')
    $('#cardHeader').text('New Permission')
    simpleCall(url, 'get', '', '', '', loadDatatable);
}
function loadPermission(response) {
    $('#id').val(response.id);
    $('#name').val(response.name);
    $('#desc').val(response.desc);
    $('#permissionGroupId').val(response.permissionGroupId);
    $('#prefix').val($('#permissionGroupId option:selected').text());
    $('#effectivePermissionName').text($('#prefix').val()+'_'+$('#name').val());
    $('#cardHeader').text('Edit Permission')
}

function deletePermission(id) {
    simpleCall(url+'/'+id, 'delete', '', '', '', function(){
        simpleCall(url, 'get', '', '', '', loadDatatable);
    });
}

function setPermissionGroups(response) {
    console.log(JSON.stringify(response));
    var select = $('#permissionGroupId');
        select.empty();
        select.append('<option value="">Select</option>');
        $.each(response, function(index, group) {
            select.append('<option value="' + group.id + '">' + group.name + '</option>');
        });

//    var urlParams = getUrlVars();
//    if(urlParams.id) {
//        // Get the value of the 'id' parameter
//        id = urlParams.id;
//        console.log("ID:", id);
//        simpleCall(url+'/'+id, 'get', '', '', '', loadPermission);
//    }
}