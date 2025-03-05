package com.ubo.tp.message.components.message.model;

/**
 * Interface d'observation du model messages
 */
public interface IMessageObserver {
	void notifyMessageAdded();
	void notifyMessageRemoved();
}
