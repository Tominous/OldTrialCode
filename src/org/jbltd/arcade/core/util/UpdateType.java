package org.jbltd.arcade.core.util;

public enum UpdateType
{
	
	TICK(1L), SECOND(20L), SHORT(3L), MINUTE(1200L), HOUR(72000L);
	
	long value;
	long _last;
	
	UpdateType(long value)
	{
		this.value = value;
		this._last = System.currentTimeMillis();
	}
	
	public boolean elapsed()
	{
		if (elapsed(this._last, this.value))
		{
			this._last = System.currentTimeMillis();
			return true;
		}
		return false;
	}
	
	public boolean elapsed(long from, long required)
	{
		return System.currentTimeMillis() - from > required;
	}
	
}