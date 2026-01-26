create or replace function set_recharges_updated_at()
returns trigger as $$
begin
  new.updated_at = now();
  return new;
end;
$$ language plpgsql;

drop trigger if exists trg_recharges_updated_at on recharges;

create trigger trg_recharges_updated_at
before update on recharges
for each row
execute function set_recharges_updated_at();