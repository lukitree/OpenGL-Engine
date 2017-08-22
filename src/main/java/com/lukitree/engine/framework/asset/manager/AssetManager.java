package com.lukitree.engine.framework.asset.manager;

import java.util.*;

public abstract class AssetManager<E,A>
{
	protected Map<E,A> assets = new HashMap<>();

	public A get(E asset)
	{
		return assets.get(asset);
	}

	public void close()
	{
		assets.clear();
	}
}
