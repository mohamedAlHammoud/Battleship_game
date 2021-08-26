package server.domain;

public abstract class Observer {
	protected Subject<?> subject;

	public abstract void update();
	public abstract void update(Subject<?> subject);

	/**
	 * Sets the Subject of this Observer
	 * 
	 * @param subject
	 */
	protected void setSubject(Subject<?> subject) {
		this.subject = subject;
	}
}
