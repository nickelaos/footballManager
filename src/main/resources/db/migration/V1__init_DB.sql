
create table players (
    id BIGSERIAL not null,
    date_of_birth date,
    full_name varchar(255) not null,
    start_of_career date,
    team int8,
    primary key (id)
);
create table teams (
    id BIGSERIAL not null,
    name varchar(255) not null,
    balance numeric(19, 2),
    commission_for_transfer float4,
    primary key (id)
);


alter table if exists players
    add constraint team_fk
    foreign key (team) references teams;


