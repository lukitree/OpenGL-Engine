package com.lukitree.engine.framework.asset.manager;

import com.lukitree.engine.framework.asset.*;
import com.lukitree.engine.framework.asset.loader.*;
import com.lukitree.engine.framework.asset.manager.enums.*;

public class ModelManager extends AssetManager<Models, Model>
{
	ModelLoader loader = new ModelLoader();

	public ModelManager(TextureManager textures)
	{
		System.out.println("Loading models...");
		load(Models.DRAGON, "dragon.obj", textures.get(Textures.GOLD), false, false, 0.1f);
		load(Models.CUBE, "cube.obj", textures.get(Textures.CUBE));
		load(Models.EARTH, "sphere.obj", textures.get(Textures.EARTH));
		load(Models.GRASS, "grass.obj", textures.get(Textures.GRASS_BUSH), true, true);
		load(Models.SUN, "sun.obj", textures.get(Textures.SUN), false, false, 2.5f);
		load(Models.TREE, "tree.obj", textures.get(Textures.TREE), false, false);
		load(Models.TERRAIN_FLAT, "terrain.obj");
		load(Models.PLAYER, "player.obj", textures.get(Textures.PLAYER));
		System.out.println("done!");
	}

	private void load(Models id, String filename)
	{
		Model model = loader.loadOBJ(filename);
		assets.put(id, model);
	}

	private void load(Models id, String filename, Texture texture, boolean useFakeLighting, boolean hasTransparency)
	{
		load(id, filename, texture, useFakeLighting, hasTransparency, 0.2f);
	}

	private void load(Models id, String filename, Texture texture, boolean useFakeLighting, boolean hasTransparency, float ambientLightLevel)
	{
		System.out.println("-> " + filename);
		TexturedModel model = new TexturedModel(loader.loadOBJ(filename), texture);
		model.setUseFakeLighting(useFakeLighting);
		model.setHasTransparency(hasTransparency);
		model.setAmbientLightLevel(ambientLightLevel);
		assets.put(id, model);
	}

	private void load(Models id, String filename, Texture texture)
	{
		load(id, filename, texture, false, false);
	}
}
