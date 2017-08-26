package com.lukitree.engine.framework.asset.manager;

import com.lukitree.engine.framework.asset.*;
import com.lukitree.engine.framework.asset.loader.*;
import com.lukitree.engine.framework.asset.manager.enums.*;

import static com.lukitree.engine.framework.asset.manager.enums.Textures.*;

public class TextureManager extends AssetManager<Textures, Texture>
{
	TextureLoader loader = new TextureLoader();

	public TextureManager()
	{
		System.out.println("Loading textures...");
		load(CUBE, "cube.png", 10, 1);
		load(EARTH, "earth.jpg", 100, 1);
		load(WHITE, "white.png", 1000, 1);
		load(GRASS, "grass.png");
		load(FLOWERS, "grassFlowers.png");
		load(MUD, "mud.png");
		load(PATH, "path.png");
		load(GRASS_BUSH, "grassBush.png");
		load(SUN, "sun.png", 100, 0.1f);
		load(TREE, "tree.png");
		load(GOLD, "gold.png", 500f, 1f);
		load(PLAYER, "player.png");
		System.out.println("done!");

		System.out.println("Loading blend maps...");
		load(BLEND_MAP01, "blendmaps/blendMap.png");
		System.out.println("done!");
	}

	private void load(Textures id, String filename, float shineDamper, float reflectivity)
	{
		System.out.println("-> " + filename);
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

