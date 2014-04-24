package com.xgame.server.pool;

public interface IPool<T>
{
	T getObject();

	void returnObject( T obj );
}
