# Work-group activity report - Test

## Table of contents
1. [Introduction](#1-introduction)
2. [Test](#2-test)
3. [Replica set status](#3-replica-set-status)

## 1. Introduction
The MongoDB database for the GIAR application is deployed on a replica set. MongoDB replica set has a primary node and several secondary nodes (our database has one primary and two secondary as stated in [Implementation Document](./Implementation.md)). Write operations are routed only to the primary node. Read operations are routed on the node with the lowest network latency to have the fastest response. If the primary node fails, an election protocol starts between the online secondary nodes to elect a new primary node. In our database, if the primary node is offline for ten seconds the election protocol start.

## 2. Test
To test if the system is tolerant to a primary fault, the following test is performed:
- Three users log-in into the application and start to use it.
- Shutdown the primary server.
- Three users continue to use the application.
- Restart the server.

After the shutdown of the primary and before the new primary election, due to the read preference setting, accept reads on secondaries, the system will continue to accept read operations. The application was still working even with an higher latency on reads. Conversely, the replica set cannot process write operations until the election completes successfully. However, the MongoDB drivers can detect the loss of the primary and automatically retry certain write operations a single time if they encounter network errors, or if they cannot find a healthy primary in the replica set. Thus, the application will apply write operations when the election concludes succesfully. The official MongoDB 4.2-compatible drivers enable Retryable Writes by default.

## 3. Replica set status
To see the status of the replica set the following command is performed:
```
res0:PRIMARY> rs.status()
```

The initial status of the replica set was:
```
{
	...
	"members" : [
		{
			"_id" : 0,
			"name" : "172.16.0.70:27017",
			"health" : 1,
			"state" : 1,
			"stateStr" : "PRIMARY",
			"uptime" : 12153,
			"optime" : {
				"ts" : Timestamp(1579270781, 1),
				"t" : NumberLong(1)
			},
			"optimeDate" : ISODate("2020-01-17T14:19:41Z"),
			"syncingTo" : "",
			"syncSourceHost" : "",
			"syncSourceId" : -1,
			"infoMessage" : "",
			"electionTime" : Timestamp(1579258679, 1),
			"electionDate" : ISODate("2020-01-17T10:57:59Z"),
			"configVersion" : 5,
			"self" : true,
			"lastHeartbeatMessage" : ""
		},
		{
			"_id" : 1,
			"name" : "172.16.0.71:27017",
			"health" : 1,
			"state" : 2,
			"stateStr" : "SECONDARY",
			"uptime" : 11449,
			"optime" : {
				"ts" : Timestamp(1579270781, 1),
				"t" : NumberLong(1)
			},
			"optimeDurable" : {
				"ts" : Timestamp(1579270781, 1),
				"t" : NumberLong(1)
			},
			"optimeDate" : ISODate("2020-01-17T14:19:41Z"),
			"optimeDurableDate" : ISODate("2020-01-17T14:19:41Z"),
			"lastHeartbeat" : ISODate("2020-01-17T14:19:46.221Z"),
			"lastHeartbeatRecv" : ISODate("2020-01-17T14:19:46.248Z"),
			"pingMs" : NumberLong(0),
			"lastHeartbeatMessage" : "",
			"syncingTo" : "172.16.0.70:27017",
			"syncSourceHost" : "172.16.0.70:27017",
			"syncSourceId" : 0,
			"infoMessage" : "",
			"configVersion" : 5
		},
		{
			"_id" : 2,
			"name" : "172.16.0.72:27017",
			"health" : 1,
			"state" : 2,
			"stateStr" : "SECONDARY",
			"uptime" : 11376,
			"optime" : {
				"ts" : Timestamp(1579270781, 1),
				"t" : NumberLong(1)
			},
			"optimeDurable" : {
				"ts" : Timestamp(1579270781, 1),
				"t" : NumberLong(1)
			},
			"optimeDate" : ISODate("2020-01-17T14:19:41Z"),
			"optimeDurableDate" : ISODate("2020-01-17T14:19:41Z"),
			"lastHeartbeat" : ISODate("2020-01-17T14:19:46.875Z"),
			"lastHeartbeatRecv" : ISODate("2020-01-17T14:19:47.784Z"),
			"pingMs" : NumberLong(0),
			"lastHeartbeatMessage" : "",
			"syncingTo" : "172.16.0.71:27017",
			"syncSourceHost" : "172.16.0.71:27017",
			"syncSourceId" : 1,
			"infoMessage" : "",
			"configVersion" : 5
		}
	],
	...
}
```

To shutdown the primary the following command is performed:
```
db.adminCommand({"shutdown" : 1})
```

The status of the replica set after the election of the new primary was:
```
{
	...
	"electionCandidateMetrics" : {
		"lastElectionReason" : "stepUpRequestSkipDryRun",
		"lastElectionDate" : ISODate("2020-01-17T14:24:33.457Z"),
		"electionTerm" : NumberLong(2),
		"lastCommittedOpTimeAtElection" : {
			"ts" : Timestamp(1579271065, 2),
			"t" : NumberLong(1)
		},
		"lastSeenOpTimeAtElection" : {
			"ts" : Timestamp(1579271065, 2),
			"t" : NumberLong(1)
		},
		"numVotesNeeded" : 2,
		"priorityAtElection" : 1,
		"electionTimeoutMillis" : NumberLong(10000),
		"priorPrimaryMemberId" : 0,
		"numCatchUpOps" : NumberLong(0),
		"newTermStartDate" : ISODate("2020-01-17T14:24:34.513Z"),
		"wMajorityWriteAvailabilityDate" : ISODate("2020-01-17T14:24:35.475Z")
	},
	"members" : [
		{
			"_id" : 0,
			"name" : "172.16.0.70:27017",
			"health" : 0,
			"state" : 8,
			"stateStr" : "(not reachable/healthy)",
			"uptime" : 0,
			"optime" : {
				"ts" : Timestamp(0, 0),
				"t" : NumberLong(-1)
			},
			"optimeDurable" : {
				"ts" : Timestamp(0, 0),
				"t" : NumberLong(-1)
			},
			"optimeDate" : ISODate("1970-01-01T00:00:00Z"),
			"optimeDurableDate" : ISODate("1970-01-01T00:00:00Z"),
			"lastHeartbeat" : ISODate("2020-01-17T14:25:20.438Z"),
			"lastHeartbeatRecv" : ISODate("2020-01-17T14:24:32.417Z"),
			"pingMs" : NumberLong(0),
			"lastHeartbeatMessage" : "Error connecting to 172.16.0.70:27017 :: caused by :: Connection refused",
			"syncingTo" : "",
			"syncSourceHost" : "",
			"syncSourceId" : -1,
			"infoMessage" : "",
			"configVersion" : -1
		},
		{
			"_id" : 1,
			"name" : "172.16.0.71:27017",
			"health" : 1,
			"state" : 1,
			"stateStr" : "PRIMARY",
			"uptime" : 11787,
			"optime" : {
				"ts" : Timestamp(1579271117, 1),
				"t" : NumberLong(2)
			},
			"optimeDate" : ISODate("2020-01-17T14:25:17Z"),
			"syncingTo" : "",
			"syncSourceHost" : "",
			"syncSourceId" : -1,
			"infoMessage" : "could not find member to sync from",
			"electionTime" : Timestamp(1579271073, 1),
			"electionDate" : ISODate("2020-01-17T14:24:33Z"),
			"configVersion" : 5,
			"self" : true,
			"lastHeartbeatMessage" : ""
		},
		{
			"_id" : 2,
			"name" : "172.16.0.72:27017",
			"health" : 1,
			"state" : 2,
			"stateStr" : "SECONDARY",
			"uptime" : 11711,
			"optime" : {
				"ts" : Timestamp(1579271117, 1),
				"t" : NumberLong(2)
			},
			"optimeDurable" : {
				"ts" : Timestamp(1579271117, 1),
				"t" : NumberLong(2)
			},
			"optimeDate" : ISODate("2020-01-17T14:25:17Z"),
			"optimeDurableDate" : ISODate("2020-01-17T14:25:17Z"),
			"lastHeartbeat" : ISODate("2020-01-17T14:25:19.500Z"),
			"lastHeartbeatRecv" : ISODate("2020-01-17T14:25:19.562Z"),
			"pingMs" : NumberLong(0),
			"lastHeartbeatMessage" : "",
			"syncingTo" : "172.16.0.71:27017",
			"syncSourceHost" : "172.16.0.71:27017",
			"syncSourceId" : 1,
			"infoMessage" : "",
			"configVersion" : 5
		}
	],
	...
}
```

The replica set status after the server restart:
```
{
	...
	"members" : [
		{
			"_id" : 0,
			"name" : "172.16.0.70:27017",
			"health" : 1,
			"state" : 2,
			"stateStr" : "SECONDARY",
			"uptime" : 36,
			"optime" : {
				"ts" : Timestamp(1579271247, 4),
				"t" : NumberLong(2)
			},
			"optimeDate" : ISODate("2020-01-17T14:27:27Z"),
			"syncingTo" : "172.16.0.72:27017",
			"syncSourceHost" : "172.16.0.72:27017",
			"syncSourceId" : 2,
			"infoMessage" : "",
			"configVersion" : 5,
			"self" : true,
			"lastHeartbeatMessage" : ""
		},
		{
			"_id" : 1,
			"name" : "172.16.0.71:27017",
			"health" : 1,
			"state" : 1,
			"stateStr" : "PRIMARY",
			"uptime" : 31,
			"optime" : {
				"ts" : Timestamp(1579271247, 3),
				"t" : NumberLong(2)
			},
			"optimeDurable" : {
				"ts" : Timestamp(1579271247, 3),
				"t" : NumberLong(2)
			},
			"optimeDate" : ISODate("2020-01-17T14:27:27Z"),
			"optimeDurableDate" : ISODate("2020-01-17T14:27:27Z"),
			"lastHeartbeat" : ISODate("2020-01-17T14:27:27.398Z"),
			"lastHeartbeatRecv" : ISODate("2020-01-17T14:27:26.702Z"),
			"pingMs" : NumberLong(0),
			"lastHeartbeatMessage" : "",
			"syncingTo" : "",
			"syncSourceHost" : "",
			"syncSourceId" : -1,
			"infoMessage" : "",
			"electionTime" : Timestamp(1579271073, 1),
			"electionDate" : ISODate("2020-01-17T14:24:33Z"),
			"configVersion" : 5
		},
		{
			"_id" : 2,
			"name" : "172.16.0.72:27017",
			"health" : 1,
			"state" : 2,
			"stateStr" : "SECONDARY",
			"uptime" : 31,
			"optime" : {
				"ts" : Timestamp(1579271247, 3),
				"t" : NumberLong(2)
			},
			"optimeDurable" : {
				"ts" : Timestamp(1579271247, 3),
				"t" : NumberLong(2)
			},
			"optimeDate" : ISODate("2020-01-17T14:27:27Z"),
			"optimeDurableDate" : ISODate("2020-01-17T14:27:27Z"),
			"lastHeartbeat" : ISODate("2020-01-17T14:27:27.410Z"),
			"lastHeartbeatRecv" : ISODate("2020-01-17T14:27:28.009Z"),
			"pingMs" : NumberLong(0),
			"lastHeartbeatMessage" : "",
			"syncingTo" : "172.16.0.71:27017",
			"syncSourceHost" : "172.16.0.71:27017",
			"syncSourceId" : 1,
			"infoMessage" : "",
			"configVersion" : 5
		}
	],
	...
}
```
