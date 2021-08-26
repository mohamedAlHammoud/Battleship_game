package client.domain;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
	// Constants
	private static final long serialVersionUID = -6635341076366698125L;

	// Attributes
	private byte user;
	private String msg;
	private Date timeStamp;

	// Constructors
	/**
	 * User defined constructor
	 * 
	 * @param setName user name
	 * @param msg     message being transported
	 */
	public Message(byte setName, String msg) {
		this.user = setName;
		this.msg = msg;

	}

	// Getter and Setter Methods
	/**
	 * @return the user
	 */
	public byte getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(byte user) {
		this.user = user;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the timeStamp
	 */
	public Date getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	// Operational Methods
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Message [msg=" + msg + ", timeStamp=" + timeStamp + ", user=" + user + "]";
	}
}