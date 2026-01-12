create table clients (
  id bigserial primary key,
  name varchar(120) not null,
  phone varchar(20) not null,
  created_at timestamp not null default now()
);

create table payments (
  id bigserial primary key,
  client_id bigint not null references clients(id),
  type varchar(30) not null,
  token varchar(120) not null,
  created_at timestamp not null default now()
);

create table recharges (
  id bigserial primary key,
  client_id bigint not null references clients(id),
  payment_id bigint not null references payments(id),
  phone varchar(20) not null,
  amount numeric(10,2) not null,
  status varchar(20) not null,
  failure_reason varchar(255),
  created_at timestamp not null default now(),
  updated_at timestamp not null default now()
);
