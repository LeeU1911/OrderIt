package com.dinogroup.util.flow;

public interface TakesResult<T> {
	void receive(T result);
}
