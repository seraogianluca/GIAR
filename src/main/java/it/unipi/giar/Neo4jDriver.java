package it.unipi.giar;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

public class Neo4jDriver {
	private static Neo4jDriver neo = null;
    private final Driver driver;

    private Neo4jDriver() {
        driver = GraphDatabase.driver("bolt://172.16.0.70:7687", AuthTokens.basic("neo4j", "bifecco"));
    }
    
    public static Neo4jDriver getInstance() {
    	if(neo == null)
    		neo = new Neo4jDriver();
    	
    	return neo;
    }
    
    public static void close() {
    	if(neo == null)
    		throw new RuntimeException("Connection doesn't exist.");
    	else
    		neo.driver.close();
    }
	
}
