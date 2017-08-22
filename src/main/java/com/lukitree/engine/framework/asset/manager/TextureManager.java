package com.lukitree.engine.framework.asset.manager;

import com.lukitree.engine.framework.asset.*;
import com.lukitree.engine.framework.asset.loader.*;
import com.lukitree.engine.framework.asset.manager.enums.*;

public class TextureManager extends AssetManager<Textures, Texture>
{
	TextureLoader loader = new TextureLoader();

	public TextureManager()
	{
		System.out.println("Loading textures...");
		load(Textures.CUBE, "cube.png", 10, 1);
		load(Textures.EARTH, "earth.jpg", 100, 1);
		load(Textures.WHITE, "white.png", 1000, 1);
		load(Textures.GRASS, "grass.png");
		load(Textures.GRASS_BUSH, "grassBush.png");
		load(Textures.SUN, "sun.png", 100, 0.1f);
		load(Textures.TREE, "tree.png");
		load(Textures.GOLD, "gold.png", 0.1f, 0.15f);
		System.out.println("done.");
	}

	private void load(Textures id, String filename, float shineDamper, float reflectivity)
	{
		System.out.println(filename);
		Texture texture = loader.loadTexture(filename);
		texture.setShineDamper(shineDamper);
		texture.setReflectivity(reflectivity);
		assets.put(id, texture);
	}

	private void load(Textures id, String filename)
	{
		load(id, filename, 1, 0);
	}

	@Override
	public void close()
	{
		assets.clear();
		loader.close();
	}
}

