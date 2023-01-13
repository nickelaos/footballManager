
const postPutHeaders = {
    'Accept': 'application/json', 'Content-Type': 'application/json'
};

const getTeams = () => {
    fetch('http://localhost:8080/teams', {method: 'GET'})
        .then(response => response.json())
        .then(teams => onTeamLoaded(teams));
}

const getTeam = (id) => {
    return fetch(`http://localhost:8080/teams/${id}`, {method: 'GET'})
        .then(response => response.json());
}

const updateTeam = (id, body) => {
    return fetch(`http://localhost:8080/teams/${id}`, {
        method: 'PUT', headers: postPutHeaders, body: JSON.stringify(body)
    })
        .then(response => {
            if ([200, 201, 204].includes(response.status)) {
                closeModal();
                getTeams();
            }
        });
}
const createTeam = (body) => {
    return fetch(`http://localhost:8080/teams`, {
        method: 'POST', headers: postPutHeaders, body: JSON.stringify(body)
    }).then(response => {
        if ([200, 201, 204].includes(response.status)) {
            closeModal();
            getTeams();
        }
    });
}
const deleteTeam = (id) => {
    fetch(`http://localhost:8080/teams/${id}`, {method: 'DELETE'}).then(() => getTeams());
}

const save = (event) => {
    const form = document.getElementById('form-content');
    if (!form.checkValidity()) {
        return;
    }
    event.preventDefault();
    const id = +document.getElementById('team-id').value || undefined;

    const team = {
        name: document.getElementById('team-name').value,
        commissionForTransfer: +document.getElementById('team-commission-for-transfer').value,
        balance: +document.getElementById('team-balance').value
    };

    id ? updateTeam(id, team) : createTeam(team);
}

const editTeam = (id) => {
    getTeam(id).then(team => setForm(team));
}

const setForm = (team) => {
    const commissionForTransfer = team.commissionForTransfer || 0;
    document.getElementById('modal-name').innerText = team.id ? `Update ${team.name}` : 'Create new';
    document.getElementById('team-id').value = team.id || '';
    document.getElementById('team-name').value = team.name || '';
    document.getElementById('team-commission-for-transfer').value = commissionForTransfer;
    document.getElementById('team-balance').value = team.balance || 0;
    document.getElementById('team-commission-for-transfer-value').innerText = commissionForTransfer * 100 + '%';
}


const tableBuilder = (team) => {

    return `
        <tr>
            <td>${team.name}</td>
            <td>${team.commissionForTransfer * 100}%</td>
            <td>${team.balance}</td>
            <td>
                <button class="btn btn-primary" onclick="editTeam(${team.id})" data-bs-toggle="modal" data-bs-target="#create-update-modal">
                    <i class="bi bi-pencil"></i>
                </button>
                <button class="btn btn-danger" onclick="deleteTeam(${team.id})">
                    <i class="bi bi-trash"></i>
                </button>
            </td>
        </tr>
    `
}

const closeModal = () => {
    const close = document.getElementById('team-close');
    close.click();
}

const onCommissionChanged = () => {
    const value = document.getElementById('team-commission-for-transfer').value;
    document.getElementById('team-commission-for-transfer-value').innerText = (value || 0) * 100 + '%';
}

const onTeamLoaded = (teams) => {
    const tableContent = teams.reduce((a, b) => a + tableBuilder(b), '');

    const tableBody = document.getElementById('team-table-body');
    tableBody.innerHTML = tableContent;
}

getTeams();