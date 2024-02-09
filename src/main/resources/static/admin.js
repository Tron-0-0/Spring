$(async function () {
    await allUsers();
    await newUser();
    deleteUser();
    editCurrentUser();
});

async function allUsers() {
    const table = $('#bodyAllUserTable');
    table.empty()
    fetch("http://localhost:8080/api/admin")
        .then(r => r.json())
        .then(data => {
            data.forEach(user => {
                let users = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.firstname}</td>
                            <td>${user.lastname}</td>                         
                            <td>${user.age}</td>
                            <td>${user.username}</td>
                            <td>${user.roles.map(role => " " + role.toString().substring(5))}</td>
                            <td>
                                <button type="button" class="btn btn-info" data-toggle="modal" id="buttonEdit" data-action="edit" data-id="${user.id}" data-target="#edit">Edit</button>
                            </td>
                            <td>
                                <button type="button" class="btn btn-danger" data-toggle="modal" id="buttonDelete" data-action="delete" data-id="${user.id}" data-target="#delete">Delete</button>
                            </td>
                        </tr>)`;
                table.append(users);
            })
        })
        .catch((error) => {
            alert(error);
        })
}

async function newUser() {
    await fetch("http://localhost:8080/api/roles")
        .then(r => r.json())
        .then(roles => {
            roles.forEach(role => {
                let element = document.createElement("option");
                element.text = role.toString().substring(5);
                // element.value = role.id;
                $('#rolesNewUser')[0].appendChild(element);
            })
        })

    const formAddNewUser = document.forms["formAddNewUser"];

    formAddNewUser.addEventListener('submit', function (event) {
        event.preventDefault();
        let rolesNewUser = [];
        for (let i = 0; i < formAddNewUser.roles.options.length; i++) {
            if (formAddNewUser.roles.options[i].selected)
                rolesNewUser.push(formAddNewUser.roles.options[i].value)
        }

        fetch("http://localhost:8080/api/admin", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                firstname: formAddNewUser.firstName.value,
                lastname: formAddNewUser.lastName.value,
                age: formAddNewUser.age.value,
                username: formAddNewUser.username.value,
                password: formAddNewUser.password.value,
                roles: rolesNewUser
            })
        }).then(() => {
            formAddNewUser.reset();
            allUsers();
            $('#allUsersTable').click();
        })
            .catch((error) => {
                alert(error);
            })
    })

}

function deleteUser() {
    const deleteForm = document.forms["formDeleteUser"];
    deleteForm.addEventListener("submit", function (event) {
        event.preventDefault();
        fetch("http://localhost:8080/api/admin/" + deleteForm.id.value, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(() => {
                $('#deleteFormCloseButton').click();
                allUsers();
            })
            .catch((error) => {
                alert(error);
            });
    })
}


$(document).ready(function () {
    $('#delete').on("show.bs.modal", function (event) {
        const button = $(event.relatedTarget);
        const id = button.data("id");
        viewDeleteModal(id);
    })
})

async function viewDeleteModal(id) {
    let userDelete = await getUser(id);
    let formDelete = document.forms["formDeleteUser"];
    formDelete.id.value = userDelete.id;
    formDelete.firstName.value = userDelete.firstName;
    formDelete.lastName.value = userDelete.lastName;
    formDelete.age.value = userDelete.age;
    formDelete.username.value = userDelete.username;

    $('#deleteRolesUser').empty();

    await fetch("http://localhost:8080/api/roles")
        .then(r => r.json())
        .then(roles => {
            roles.forEach(role => {
                let selectedRole = false;
                for (let i = 0; i < userDelete.roles.length; i++) {
                    if (userDelete.roles[i].toString() === role.toString()) {
                        selectedRole = true;
                        break;
                    }
                }
                let element = document.createElement("option");
                element.text = role.toString().substring(5);
                element.value = role.id;
                if (selectedRole) element.selected = true;
                $('#deleteRolesUser')[0].appendChild(element);
            })
        })
        .catch((error) => {
            alert(error);
        })
}

async function getUser(id) {

    let url = "http://localhost:8080/api/admin/" + id;
    let response = await fetch(url);
    return await response.json();
}

function editCurrentUser() {
    const editForm = document.forms["formEditUser"];
    editForm.addEventListener("submit", function (event) {
        event.preventDefault();

        let editUserRoles = [];
        for (let i = 0; i < editForm.roles.options.length; i++) {
            if (editForm.roles.options[i].selected)
                editUserRoles.push(editForm.roles.options[i].value)
        }

        fetch("http://localhost:8080/api/admin", {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: editForm.id.value,
                firstname: editForm.firstName.value,
                lastname: editForm.lastName.value,
                age: editForm.age.value,
                username: editForm.username.value,
                password: editForm.password.value,
                roles: editUserRoles
            })
        }).then(() => {
            $('#editFormCloseButton').click();
            allUsers();
        })
            .catch((error) => {
                alert(error);
            })
    })
}

$(document).ready(function () {
    $('#edit').on("show.bs.modal", function (event) {
        const button = $(event.relatedTarget);
        const id = button.data("id");
        viewEditModal(id);
    })
})

async function viewEditModal(id) {
    let userEdit = await getUser(id);
    let form = document.forms["formEditUser"];
    form.id.value = userEdit.id;
    form.firstName.value = userEdit.firstname;
    form.lastName.value = userEdit.lastname;
    form.age.value = userEdit.age;
    form.username.value = userEdit.username;
    form.password.value = userEdit.password;

    $('#editRolesUser').empty();

    await fetch("http://localhost:8080/api/roles")
        .then(r => r.json())
        .then(roles => {
            roles.forEach(role => {
                let selectedRole = false;

                for (let i = 0; i < userEdit.roles.length; i++) {
                    if (userEdit.roles.includes(role)) {
                        selectedRole = true;
                        break;
                    }
                }
                let element = document.createElement("option");
                element.text = role.toString().substring(5);
                // element.value = role.id;
                if (selectedRole) element.selected = true;
                $('#editRolesUser')[0].appendChild(element);
            })
        })
        .catch((error) => {
            alert(error);
        })
}