# IoT-CW
Repo for IoT Course Work for Big Data team

## Database

This project uses a [Cassandra](https://cassandra.apache.org/_/index.html) database to store data.

Start Cassandra instance using docker:

`docker run --name cassandra -p 9042:9042 -d cassandra`

Create `iot_cw` keyspace and `measurements` table:
```
CREATE KEYSPACE iot_cw WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : '1' };
CREATE TABLE iot_cw.measurements (id UUID PRIMARY KEY, device_id text, temperature float, humidity float, mes_timestamp timestamp);
```

