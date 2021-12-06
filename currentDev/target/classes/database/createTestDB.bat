#pg_dump --schema=cinemaUser cinemadb > backupCinemaDB.sql
#psql -d testdb -h localhost -U cinemaUser < backupfile.sql

pg_dump cinemadb --schema cinemaUser | psql -h localhost -U cinemaUser -d testdb