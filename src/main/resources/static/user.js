$(async function () {
    await loadUser();
});

async function loadUser() {
    fetch("http://localhost:8080/api/user")
        .then(r => r.json())
        .then(data => {
            $('#navUsername').append(data.username);
            let roles = data.roles.map(role => " " + role.toString().substring(5));
            $('#navRoles').append(roles);
            let user = `$(
            <tr>
                <td>${data.id}</td>
                <td>${data.firstname}</td>
                <td>${data.lastname}</td>
                <td>${data.age}</td>
                <td>${data.username}</td>
                <td>${roles}</td>)`;
            $('#userPanelBody').append(user);
        })
        .catch((error) => {
            alert(error);
        });
}
