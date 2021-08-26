package server.domain;

import java.util.LinkedList;

public abstract class Subject<E> implements Runnable
{
	protected LinkedList<Observer> observers = new LinkedList<Observer>();

	Thread thread;

	protected E state;

	// OBSERVER
	// ----------------------------------------

	/**
	 * Adds an Observer to this Subject
	 * @param observer the Observer to add
	 */
	public void addObserver(Observer observer)
	{
		observers.add(observer);
	}

	// --------------------

	/**
	 * Removes an Observer from this Subject
	 * @param observer the Observer to remove
	 */
	public synchronized void removeObserver(Observer observer)
	{
		observers.remove(observer);
	}

	// NOTIFY OBSERVERS
	// ----------------------------------------

	/**
	 * Notifies all observers that state has changed
	 */
	public void notifyObservers()
	{
		for (Observer observer : observers)
		{
			observer.update();
		}
	}

	// GET STATE
	// ----------------------------------------

	/**
	 * Gets the current State
	 * @return the current State
	 */
	public E getState()
	{
		return state;
	}

	// THREADING
	// ----------------------------------------

	/**
	 * Initializes and starts the Thread
	 */
	protected void initThread()
	{
		thread = new Thread(this);
		thread.start();
	}

	// --------------------

	/**
	 * Interrupts the thread
	 */
	public void interrupt()
	{
		thread.interrupt();
	}
}