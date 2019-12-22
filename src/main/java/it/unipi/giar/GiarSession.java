package it.unipi.giar;

import it.unipi.giar.Data.User;

public class GiarSession {
	
	private static GiarSession session = null;
	private User logged = null;
	private String registered = null;
	private boolean deleted = false;
	
	private GiarSession() {}
	
	public static GiarSession getInstance() {
		if(session == null) {
			session = new GiarSession();
		} 
		
		return session;
	}
	
	public void setLoggedUser(String nickname) {
		if(session == null) {
			throw new RuntimeException("Session is not active.");
		} else {
			session.logged = new User(nickname);
		}
	}
	
	public User getLoggedUser() {
		if(session == null) {
			throw new RuntimeException("Session is not active.");
		} else {
			return session.logged;
		}
	}
	
	public void setRegistered(String nickname) {
		if(session == null) {
			throw new RuntimeException("Session is not active.");
		} else {
			session.registered = nickname;
		}
	}
	
	public String getRegistered() {
		if(session == null) {
			throw new RuntimeException("Session is not active.");
		} else {
			return session.registered;
		}
	}
	
	public void setDeleted(boolean newDeleted) {
		if(session == null) {
			throw new RuntimeException("Session is not active.");
		} else {
			session.deleted = newDeleted;
		}
	}
	
	public boolean getDeleted() {
		if(session == null) {
			throw new RuntimeException("Session is not active.");
		} else {
			return session.deleted;
		}
	}
 	
}
