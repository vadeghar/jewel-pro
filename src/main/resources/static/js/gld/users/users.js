var _customerContext = '/users'
var url = baseUrl + _customerContext;

$(document).ready(function() {
    simpleCall(url, 'get', '', '', '', loadUsers);
});


function loadUsers(response) {
    $('#usersTable').DataTable().destroy();
    $('#usersTable').DataTable({
        data: response,
        columns: [
            {data: 'username'},
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
    getRoles();
}

$(document).on('click', '.editRowCls', function() {
    var id = $(this).data('id');
    simpleCall(url+'/'+id, 'get', '', '', '', loadUserAndRoles);
});

function loadRoles(response) {
    $('#rolesContainer').html('')
    $.each(response, function(index, role) {
        var col = $('<div class="col-lg-3"></div>');
        var card = $('<div class="card mb-4 border-left-primary"></div>');
        var cardHeader = $('<div class="card-header"></div>');
//        card.append(cardHeader);
        var cardBody = $('<div class="card-body"></div>');


        var permissionItem = $('<div class="custom-control custom-checkbox"></div>');
        permissionItem.append($('<input type="checkbox" class="custom-control-input" id="' + role.id + '">')
                         .attr('name', role.id).prop('checked', false));
        permissionItem.append($('<label class="custom-control-label" for="' + role.id + '">' + role.name + '</label>'));
        cardHeader.append(permissionItem);


        card.append(cardHeader);
        col.append(card);
        $('#rolesContainer').append(col); // Replace '#yourContainer' with your actual container element ID
    });
}

function getRoles() {
     simpleCall(baseUrl+'/roles/all', 'get', '', '', '', loadRoles);
}
function loadUserAndRoles(response) {
    console.log(JSON.stringify(response));
    $('#name').val(response.name);
    $('#id').val(response.id);
    $('#password').val(response.password);
    $('#username').val(response.username);
    $('#userFormHeading').text('Edit User')
}

$('#resetUser').on('click', function(){
    resetForm('#userForm');
    var checkboxes = $('#rolesContainer input:checkbox');
    checkboxes.prop('checked', false);
})
