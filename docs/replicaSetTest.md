res0:PRIMARY> rs.status()
{
	"set" : "res0",
	"date" : ISODate("2020-01-17T14:19:47.915Z"),
	"myState" : 1,
	"term" : NumberLong(1),
	"syncingTo" : "",
	"syncSourceHost" : "",
	"syncSourceId" : -1,
	"heartbeatIntervalMillis" : NumberLong(2000),
	"optimes" : {
		"lastCommittedOpTime" : {
			"ts" : Timestamp(1579270781, 1),
			"t" : NumberLong(1)
		},
		"readConcernMajorityOpTime" : {
			"ts" : Timestamp(1579270781, 1),
			"t" : NumberLong(1)
		},
		"appliedOpTime" : {
			"ts" : Timestamp(1579270781, 1),
			"t" : NumberLong(1)
		},
		"durableOpTime" : {
			"ts" : Timestamp(1579270781, 1),
			"t" : NumberLong(1)
		}
	},
	"lastStableCheckpointTimestamp" : Timestamp(1579270761, 1),
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
	"ok" : 1,
	"operationTime" : Timestamp(1579270781, 1),
	"$clusterTime" : {
		"clusterTime" : Timestamp(1579270781, 1),
		"signature" : {
			"hash" : BinData(0,"AAAAAAAAAAAAAAAAAAAAAAAAAAA="),
			"keyId" : NumberLong(0)
		}
	}
}



res0:PRIMARY> rs.status()
{
	"set" : "res0",
	"date" : ISODate("2020-01-17T14:25:21.336Z"),
	"myState" : 1,
	"term" : NumberLong(2),
	"syncingTo" : "",
	"syncSourceHost" : "",
	"syncSourceId" : -1,
	"heartbeatIntervalMillis" : NumberLong(2000),
	"optimes" : {
		"lastCommittedOpTime" : {
			"ts" : Timestamp(1579271117, 1),
			"t" : NumberLong(2)
		},
		"readConcernMajorityOpTime" : {
			"ts" : Timestamp(1579271117, 1),
			"t" : NumberLong(2)
		},
		"appliedOpTime" : {
			"ts" : Timestamp(1579271117, 1),
			"t" : NumberLong(2)
		},
		"durableOpTime" : {
			"ts" : Timestamp(1579271117, 1),
			"t" : NumberLong(2)
		}
	},
	"lastStableCheckpointTimestamp" : Timestamp(1579271074, 1),
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
	"ok" : 1,
	"operationTime" : Timestamp(1579271117, 1),
	"$clusterTime" : {
		"clusterTime" : Timestamp(1579271117, 1),
		"signature" : {
			"hash" : BinData(0,"AAAAAAAAAAAAAAAAAAAAAAAAAAA="),
			"keyId" : NumberLong(0)
		}
	}
}


res0:SECONDARY> rs.status()
{
	"set" : "res0",
	"date" : ISODate("2020-01-17T14:27:28.299Z"),
	"myState" : 2,
	"term" : NumberLong(2),
	"syncingTo" : "172.16.0.72:27017",
	"syncSourceHost" : "172.16.0.72:27017",
	"syncSourceId" : 2,
	"heartbeatIntervalMillis" : NumberLong(2000),
	"optimes" : {
		"lastCommittedOpTime" : {
			"ts" : Timestamp(1579271247, 4),
			"t" : NumberLong(2)
		},
		"readConcernMajorityOpTime" : {
			"ts" : Timestamp(1579271247, 4),
			"t" : NumberLong(2)
		},
		"appliedOpTime" : {
			"ts" : Timestamp(1579271247, 4),
			"t" : NumberLong(2)
		},
		"durableOpTime" : {
			"ts" : Timestamp(1579271247, 4),
			"t" : NumberLong(2)
		}
	},
	"lastStableCheckpointTimestamp" : Timestamp(1579271065, 2),
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
	"ok" : 1,
	"operationTime" : Timestamp(1579271247, 4),
	"$clusterTime" : {
		"clusterTime" : Timestamp(1579271247, 4),
		"signature" : {
			"hash" : BinData(0,"AAAAAAAAAAAAAAAAAAAAAAAAAAA="),
			"keyId" : NumberLong(0)
		}
	}
}