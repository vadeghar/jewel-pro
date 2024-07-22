var _customerContext = '/roles'
var url = baseUrl + _customerContext;

$(document).ready(function() {
    simpleCall(url+'/all', 'get', '', '', '', loadRoles);
});


function loadRoles(response) {
    $('#rolesTable').DataTable().destroy();
    $('#rolesTable').DataTable({
        data: response,
        columns: [
            {data: 'id'},
            {data: 'name'},
            {
              data: 'id',
              render: function (data, type, row) {
                  return  '<button class="btn btn-sm btn-primary editRowCls" data-id="' + data +'" title="Edit Permission"><i class="fas fa-edit"></i></button> ';
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
    getPermissions();
}

$(document).on('click', '.editRowCls', function() {
    var id = $(this).data('id');
    simpleCall(url+'/'+id, 'get', '', '', '', loadRoleAndPermissions);
});


$('#resetRole').on('click', function(){
    resetForm('#roleForm');
    var checkboxes = $('#permissionsContainer input:checkbox');
    checkboxes.prop('checked', false);
})

$('#saveRole').on('click', function(){
    var roleRequest = formToJson('#roleForm');
    var checkedPermissions = [];
    $('#permissionsContainer input:checkbox:checked').each(function() {
        var permissionId = $(this).attr('id') || $(this).attr('name');
        var permission = {
          "id": permissionId
        };
        checkedPermissions.push(permission);
    });
    roleRequest['permissions'] = checkedPermissions;
    console.log(JSON.stringify(roleRequest));
    if (roleRequest.id == '') {
        simpleCall(url, 'post', '', '', JSON.stringify(roleRequest), saveCallback);
    } else {
        simpleCall(url+'/'+roleRequest.id, 'put', '', '', JSON.stringify(roleRequest), saveCallback);
    }

})

function saveCallback(response) {
    resetForm('#roleForm');
    var checkboxes = $('#permissionsContainer input:checkbox');
    checkboxes.prop('checked', false);
    showSuccessMsg('Role: '+response.name+' successfully saved.')
    $('#roleFormHeading').text('New Role')
    simpleCall(url+'/all', 'get', '', '', '', loadRoles);
}


function loadRoleAndPermissions(response) {
    $('#name').val(response.name);
    $('#id').val(response.id);
    $('#roleFormHeading').text('Edit Role')
    var checkboxes = $('#permissionsContainer input:checkbox');
    $.each(response.permissions, function(index, permission) {
        var permissionId = permission.id;
        var matchingCheckbox = checkboxes.filter(function() {
            return $(this).attr('id') == permissionId;
        });
        if (matchingCheckbox) {
            matchingCheckbox.prop('checked', true);
        }
    });


}

function getPermissions() {
    simpleCall(baseUrl+'/permission-groups', 'get', '', '', '', loadPermissions);
}

function loadPermissions(data) {
      $('#permissionsContainer').html('')
      data.sort(function(a, b) {
        return a.displayOrder - b.displayOrder;
      });
      $.each(data, function(index, role) {
        var col = $('<div class="col-lg-3"></div>');
        var card = $('<div class="card mb-4 border-left-primary" style="min-height: 160px"></div>');
        var cardHeader = $('<div class="card-header">' + role.name + '</div>');
        card.append(cardHeader);
        var cardBody = $('<div class="card-body"></div>');
        $.each(role.permissions, function(i, permission) {
          var permissionItem = $('<div class="custom-control custom-checkbox"></div>');
          permissionItem.append($('<input type="checkbox" class="custom-control-input" id="' + permission.id + '">')
                                 .attr('name', permission.id).prop('checked', false));
          permissionItem.append($('<label class="custom-control-label" for="' + permission.id + '">' + permission.desc + '</label>'));
          cardBody.append(permissionItem);
        });
        card.append(cardBody);
        col.append(card);
        $('#permissionsContainer').append(col); // Replace '#yourContainer' with your actual container element ID
      });
}