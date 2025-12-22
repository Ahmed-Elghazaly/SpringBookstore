FROM postgres:18

COPY schema.sql /docker-entrypoint-initdb.d/1-schema.sql
COPY triggers.sql /docker-entrypoint-initdb.d/2-triggers.sql
COPY seed.sql /docker-entrypoint-initdb.d/3-seed.sql