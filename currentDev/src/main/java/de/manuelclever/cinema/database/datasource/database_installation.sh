mkdir /data/cinemadb &&
sudo mkdir /var/lib/pgsql &&
sudo chown $USER:$USER pgsql
sudo apt install postgresql-server &&
sudo /usr/bin/postgresql-setup --initdb &&
sudo chown -r postgres:postgres pgsql &&
sudo mkdir /data/cinemadb &&
sudo chown -r postgres:postgres /data/cinemadb &&
sudo chcon unconfined_u:object_r:postgresql_db_t:s0 /data &&
sudo chcon unconfined_u:object_r:postgresql_db_t:s0 /data/cinemadb &&
sudo nano /var/lib/pgsql/data/pg_hba.conf &&
pause &&
psql -h localhost -U postgres &&
ALTER ROLE postgres WITH PASSWORD '1551'; &&
exit; &&
sudo nano /var/lib/pgsql/data/pg_hba.conf
