var _customerContext = '/permission-groups'
var url = baseUrl + _customerContext;
$(document).ready(function() {
    simpleCall(url, 'get', '', '', '', loadDatatable);
});

function loadDatatable(response) {
    $('#permissionGroupsTable').DataTable().destroy();
    $('#permissionGroupsTable').DataTable({
        data: response,
        columns: [
            {data: 'displayOrder'},
            {data: 'name'},
            {data: 'desc'},
            {
              data: 'id',
              render: function (data, type, row) {
                  return  '<button class="btn btn-sm btn-primary editRowCls" data-id="' + data +'" title="Edit Permission"><i class="fas fa-edit"></i></button> '+
                  '<button class="btn btn-sm btn-danger deleteRowCls" data-id="' + data +'" title="Delete Permission"><i class="fas fa-trash"></i></button> ';
              }
            }
        ],
        responsive: true,
        "columnDefs": [{
                "targets": 'no-sort', // Target the columns with the class 'no-sort'
                "orderable": false,   // Disable sorting for these columns
            }],
        order: [[1, 'desc']]
    });
}

$(document).on('click', '.deleteRowCls', function() {
    var id = $(this).data('id');
    createDynamicModal(id, "Delete Permission Group?.", "Are you sure want to delete?", "Yes", deletePermissionGroup)
});

$(document).on('click', '.editRowCls', function() {
    var id = $(this).data('id');
    simpleCall(url+'/'+id, 'get', '', '', '', loadPermissionGroup);
});


function deletePermissionGroup(id) {
    simpleCall(url+'/'+id, 'delete', '', '', '', function(){simpleCall(url, 'get', '', '', '', loadDatatable);});
}

function loadPermissionGroup(response) {
    $('#id').val(response.id);
    $('#name').val(response.name);
    $('#desc').val(response.desc);
    $('#displayOrder').val(response.displayOrder);
    $('#cardHeader').text('Edit Permission Group')
}

$('#savePermissionGroup').on('click', function(){
    var reportRequest = formToJson('#permissionGroupForm');
    console.log(JSON.stringify(reportRequest));
    simpleCall(url, 'post', '', '', JSON.stringify(reportRequest), saveCallback);
})

$(document).on('click', '#resetPermissionGroup', function() {
    resetForm('#permissionGroupForm');
    $('#cardHeader').text('New Permission Group')
});

function saveCallback(response) {
    resetForm('#permissionGroupForm');
    showSuccessMsg('Permission Group: '+response.name+' successfully saved.')
    $('#cardHeader').text('New Permission Group')
    simpleCall(url, 'get', '', '', '', loadDatatable);
}