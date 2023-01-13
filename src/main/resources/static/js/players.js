const postPutHeaders = {
    'Accept': 'application/json', 'Content-Type': 'application/json'
};

const getPlayer = async (id) => {
    return await fetch(`http://localhost:8080/players/${id}`, {method: 'GET'})
    .then(response => response.json())
}

const getPlayers = async () => {
    await fetch('http://localhost:8080/players/all', {method: 'GET'})
   .then(response => response.json())
   .then(players => onPlayerLoaded(players));
}

const getTeams = async () => {
    return await fetch('http://localhost:8080/teams', {method: 'GET'})
    .then(response => response.json());
}

const selectBuilder = async (teams) => {
   const selectBody = document.getElementById('team-of-player');

    selectContent = await getTeams().then(
        teams => teams.reduce((a, team) => a + `<option value="${team.id}">${team.name}</option>`, '')
    );
    selectBody.innerHTML = selectContent;
}

const onPlayerLoaded = (players) => {
    const tableContent = players.reduce((a, b) => a + tableBuilder(b), '');
    const tableBody = document.getElementById('player-table-body');
    tableBody.innerHTML = tableContent;
}


const tableBuilder = (player) =>{

 return `
        <tr>
           <td>${player.fullName}</td>
           <td>${player.team.name}</td>
           <td>${player.dateOfBirth}</td>
           <td>${player.startOfCareer}</td>
           <td>
               <button class="btn btn-primary" onclick="editPlayer(${player.id})" data-bs-toggle="modal" data-bs-target="#create-update-modal">
                   <i class="bi bi-pencil"></i>
               </button>
               <button class="btn btn-danger" onclick="deletePlayer(${player.id})">
                   <i class="bi bi-trash"></i>
               </button>
           </td>
        </tr>
    `
 }

const editPlayer = async (id) => {
    let player = await getPlayer(id);
    setForm(player);
}

const setForm = (player) => {

    document.getElementById('player-id').value = player.id || '';
    document.getElementById('player-fullname').value = player.fullName || '';
    document.getElementById('team-of-player').value = player.team.name || '';
    document.getElementById('player-date-of-birth').value = player.dateOfBirth || '';
    document.getElementById('start-of-career').value = player.startOfCareer || '';
}

const setEmptyForm = () => {
     document.getElementById('player-id').value = '';
     document.getElementById('player-fullname').value = '';
     document.getElementById('team-of-player').value = '';
     document.getElementById('player-date-of-birth').value = '';
     document.getElementById('start-of-career').value = '';
 }

const  updatePlayer = async (id, body) => {
    return await fetch(`http://localhost:8080/players/${id}`, {
           method: 'PUT', headers: postPutHeaders, body: JSON.stringify(body)
       })
           .then(response => {

               if ([200, 201, 204].includes(response.status)) {
                   closeModal();
                   getPlayers();
               }
           });

}

const createPlayer = async (body) => {
     return await fetch(`http://localhost:8080/players`, {
            method: 'POST', headers: postPutHeaders, body: JSON.stringify(body)
        }).then(response => {
            if ([200, 201, 204].includes(response.status)) {
                closeModal();
                getPlayers();
            }
        });
}

const save = (event) => {
    const form = document.getElementById('form-content');

    if (!form.checkValidity()) {
        console.log("invalid data")
        return;
    }
    event.preventDefault();
    const id = +document.getElementById('player-id').value || undefined;

    const player = {
        fullName: document.getElementById('player-fullname').value || "",
        team: +document.getElementById('team-of-player').value || "",
        dateOfBirth: document.getElementById('player-date-of-birth').value || "",
        startOfCareer: document.getElementById('start-of-career').value || ""
    };

    id ? updatePlayer(id,player) : createPlayer(player);

}

const deletePlayer = (id) => {
    fetch(`http://localhost:8080/players/${id}`, {method: 'DELETE'}).then(() => getPlayers());
}

const closeModal = () => {
    const close = document.getElementById('player-close');
    close.click();
}
selectBuilder();

getPlayers();


