# Flink SQL Executor
This is an example Flink job that can be used to run sequence of SQL 
queries in FLINK environment.

It should work for both CP Flink, or Open Source Flink.

The goal is to support SQL as application in CPF application mode.


# How to run

## Development phase
You may use Flink open source to develop and collect appropriate queries.

For example:

The following docker compose file starts OSS Flink with SQL:

```yaml
version: "2.2"
services:
  jobmanager:
    image: flink:1.20.0-scala_2.12
    ports:
      - "8081:8081"
    command: jobmanager
    environment:
      - |
        FLINK_PROPERTIES=
        jobmanager.rpc.address: jobmanager        

  taskmanager:
    image: flink:1.20.0-scala_2.12
    depends_on:
      - jobmanager
    command: taskmanager
    scale: 1
    environment:
      - |
        FLINK_PROPERTIES=
        jobmanager.rpc.address: jobmanager
        taskmanager.numberOfTaskSlots: 2        
  sql-client:
    image: flink:1.20.0-scala_2.12
    command: bin/sql-client.sh
    depends_on:
      - jobmanager
    environment:
      - |
        FLINK_PROPERTIES=
        jobmanager.rpc.address: jobmanager
        rest.address: jobmanager 
```

You can start the OSS Flink cluster with:
```bash
$ docker compose up -d
```

An OSS Flink cluster will be started.

You may develop you SQL using interactive shell. For example:

```bash
$ docker compose run sql-client
                                   ▒▓██▓██▒
                               ▓████▒▒█▓▒▓███▓▒
                            ▓███▓░░        ▒▒▒▓██▒  ▒
                          ░██▒   ▒▒▓▓█▓▓▒░      ▒████
                          ██▒         ░▒▓███▒    ▒█▒█▒
                            ░▓█            ███   ▓░▒██
                              ▓█       ▒▒▒▒▒▓██▓░▒░▓▓█
                            █░ █   ▒▒░       ███▓▓█ ▒█▒▒▒
                            ████░   ▒▓█▓      ██▒▒▒ ▓███▒
                         ░▒█▓▓██       ▓█▒    ▓█▒▓██▓ ░█░
                   ▓░▒▓████▒ ██         ▒█    █▓░▒█▒░▒█▒
                  ███▓░██▓  ▓█           █   █▓ ▒▓█▓▓█▒
                ░██▓  ░█░            █  █▒ ▒█████▓▒ ██▓░▒
               ███░ ░ █░          ▓ ░█ █████▒░░    ░█░▓  ▓░
              ██▓█ ▒▒▓▒          ▓███████▓░       ▒█▒ ▒▓ ▓██▓
           ▒██▓ ▓█ █▓█       ░▒█████▓▓▒░         ██▒▒  █ ▒  ▓█▒
           ▓█▓  ▓█ ██▓ ░▓▓▓▓▓▓▓▒              ▒██▓           ░█▒
           ▓█    █ ▓███▓▒░              ░▓▓▓███▓          ░▒░ ▓█
           ██▓    ██▒    ░▒▓▓███▓▓▓▓▓██████▓▒            ▓███  █
          ▓███▒ ███   ░▓▓▒░░   ░▓████▓░                  ░▒▓▒  █▓
          █▓▒▒▓▓██  ░▒▒░░░▒▒▒▒▓██▓░                            █▓
          ██ ▓░▒█   ▓▓▓▓▒░░  ▒█▓       ▒▓▓██▓    ▓▒          ▒▒▓
          ▓█▓ ▓▒█  █▓░  ░▒▓▓██▒            ░▓█▒   ▒▒▒░▒▒▓█████▒
           ██░ ▓█▒█▒  ▒▓▓▒  ▓█                █░      ░░░░   ░█▒
           ▓█   ▒█▓   ░     █░                ▒█              █▓
            █▓   ██         █░                 ▓▓        ▒█▓▓▓▒█░
             █▓ ░▓██░       ▓▒                  ▓█▓▒░░░▒▓█░    ▒█
              ██   ▓█▓░      ▒                    ░▒█▒██▒      ▓▓
               ▓█▒   ▒█▓▒░                         ▒▒ █▒█▓▒▒░░▒██
                ░██▒    ▒▓▓▒                     ▓██▓▒█▒ ░▓▓▓▓▒█▓
                  ░▓██▒                          ▓░  ▒█▓█  ░░▒▒▒
                      ▒▓▓▓▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒░░▓▓  ▓░▒█░
          
    ______ _ _       _       _____  ____  _         _____ _ _            _  BETA   
   |  ____| (_)     | |     / ____|/ __ \| |       / ____| (_)          | |  
   | |__  | |_ _ __ | | __ | (___ | |  | | |      | |    | |_  ___ _ __ | |_ 
   |  __| | | | '_ \| |/ /  \___ \| |  | | |      | |    | | |/ _ \ '_ \| __|
   | |    | | | | | |   <   ____) | |__| | |____  | |____| | |  __/ | | | |_ 
   |_|    |_|_|_| |_|_|\_\ |_____/ \___\_\______|  \_____|_|_|\___|_| |_|\__|
          
        Welcome! Enter 'HELP;' to list all available commands. 'QUIT;' to exit.

Command history file path: /opt/flink/.flink-sql-history

Flink SQL> CREATE TABLE Orders (
>      order_number BIGINT,
>      price        DECIMAL(32,2),
>      buyer        ROW<first_name STRING, last_name STRING>,
>      order_time   TIMESTAMP(3)
> ) WITH (
>    'connector' = 'datagen'
>  );
[INFO] Execute statement succeeded.

Flink SQL> create table total_orders (
>     total_orders bigint, 
>     max_price DECIMAL(32,2)
> ) with ('connector' = 'print');
[INFO] Execute statement succeeded.

Flink SQL> insert into total_orders select count(*) as total_orders, 
> max(price) as max_price from Orders;
[INFO] Submitting SQL update statement to the cluster...
[INFO] SQL update statement has been successfully submitted to the cluster:
Job ID: 5ab977d26030e5b3d70c76c4e2bea358
```
With the SQL session open,
You can inspect the job manager's output, and see the `print` connector printing data.
```bash
$ docker ps
CONTAINER ID   IMAGE                     COMMAND                  CREATED          STATUS          PORTS                                                 NAMES
41d32d2664bb   flink:1.20.0-scala_2.12   "/docker-entrypoint.…"   2 minutes ago    Up 2 minutes    6123/tcp, 8081/tcp                                    oss-sql-client-run-bd640c982b6c
c7bbcd40317c   flink:1.20.0-scala_2.12   "/docker-entrypoint.…"   11 minutes ago   Up 11 minutes   6123/tcp, 8081/tcp                                    oss-taskmanager-1
4b42706d2d40   flink:1.20.0-scala_2.12   "/docker-entrypoint.…"   11 minutes ago   Up 11 minutes   6123/tcp, 0.0.0.0:8081->8081/tcp, :::8081->8081/tcp   oss-jobmanager-1
```

```bash
$ docker logs -n 10 c7bbcd40317c
-U[1379995, 999999705457858686023173668864.00]
+U[1379996, 999999705457858686023173668864.00]
-U[1379996, 999999705457858686023173668864.00]
+U[1379997, 999999705457858686023173668864.00]
-U[1379997, 999999705457858686023173668864.00]
+U[1379998, 999999705457858686023173668864.00]
-U[1379998, 999999705457858686023173668864.00]
+U[1379999, 999999705457858686023173668864.00]
-U[1379999, 999999705457858686023173668864.00]
+U[1380000, 999999705457858686023173668864.00]
```

This proves the SQL queries are satisfactory.

## Configure the SQL query file

You can create a sql file that contains many queries you want to run.

For example, 

Create a file called "queries" with the following content (equivalent to the above SQLs in dev phase).
```sql
CREATE TABLE Orders (
     order_number BIGINT,
     price        DECIMAL(32,2),
     buyer        ROW<first_name STRING, last_name STRING>,
     order_time   TIMESTAMP(3)
) WITH (
   'connector' = 'datagen'
 );
-----
create table total_orders (
    total_orders bigint, 
    max_price DECIMAL(32,2)
) with ('connector' = 'print');
-----
insert into total_orders select count(*) as total_orders, 
max(price) as max_price from Orders;
```

Note: Queries must be separated by a single line of `-----`. You can't have empty queries. 

## Packaging Application
Sample config of your job in JSON:

```json
{
  "apiVersion": "cmf.confluent.io/v1alpha1",
  "kind": "FlinkApplication",
  "metadata": {
    "name": "sql-executor"
  },
  "spec": {
    "image": "confluentinc/cp-flink:1.19.1-cp1",
    "flinkVersion": "v1_19",
    "serviceAccount": "flink",
    "podTemplate": {
        "spec": {
          "containers": [
            {
              "name": "flink-main-container",
              "volumeMounts": [
                {
                  "mountPath": "/opt/flink/downloads",
                  "name": "downloads"
                }
              ]
            }
          ],
          "volumes": [
            {
              "name": "downloads",
              "emptyDir": {}
            }
          ]
        }
      },
    "jobManager": {
      "podTemplate": {

        "spec": {
            "initContainers": [
    {
      "name": "mc",
      "image": "minio/mc",
      "volumeMounts": [
        {
          "mountPath": "/opt/flink/downloads",
          "name": "downloads"
        }
      ],
      "command": [
        "/bin/sh",
        "-c",
        "mc alias set dev-minio http://minio-service.minio-dev.svc.cluster.local:9000 minioadmin minioadmin && mc cp dev-minio/flink/flink-sql-executor-1.0.jar /opt/flink/downloads/flink-sql-executor-1.0.jar && mc cp dev-minio/flink/queries /opt/flink/downloads/queries"
      ]
    }
  ]
        }
      },
      "resource": {
        "memory": "1024m",
        "cpu": 1
      }
    },
    "taskManager": {
      "resource": {
        "memory": "1024m",
        "cpu": 1
      }
    },
    "job": {
      "jarURI": "local:///opt/flink/downloads/flink-sql-executor-1.0.jar",
      "args": ["/opt/flink/downloads/queries"],
      "state": "running",
      "parallelism": 1,
      "upgradeMode": "stateless"
    }
  },
  "status": null
}

```

In this example, we are using Confluent Platform Flink Standard Docker image.
It is quite complicated. But in short what it does is:

1. Upon start, download the flink-sql-executor-1.0.jar from minio bucket.
2. Upon start, download queries file from minio bucket.
3. Start the local downloaded jar, with 1 argument of full path of the queries file

You may follow instructions here on how to deploy it:
https://docs.confluent.io/platform/current/flink/get-started.html

Follow step 2.

Therefore, it is technically reuse the same image (no need to build other image)
to dynamically download jars, and required parameters to the jar file and run.

Of course, you may simplify the above by building docker file that:
1. Copy the jar file into the container
2. Copy the queris file into the container
3. Start the docker.

Example of Dockerfile:

```
FROM confluentinc/cp-flink:1.19.1-cp1
COPY ./build/libs/flink-sql-executor-1.0.jar /opt/flink/downloads/flink-sql-executor-1.0.jar
COPY ./queries /opt/flink/downloads/queries
```

In this case, you may save the minio part of the application.json.


