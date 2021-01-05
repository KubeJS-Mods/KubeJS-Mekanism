package dev.latvian.kubejs.mekanism;

import dev.latvian.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.kubejs.util.ListJS;

/**
 * @author LatvianModder
 */
public class PressurizedReactionRecipeJS extends MekanismRecipeJS
{
	@Override
	public void create(ListJS args)
	{
		throw new RecipeExceptionJS("Creation not supported yet!");
	}

	@Override
	public void deserialize()
	{
		if (json.has("itemOutput"))
		{
			outputItems.add(parseResultItem(json.get("itemOutput")));
		}

		inputItems.add(parseIngredientItem(json.get("itemInput")).asIngredientStack());
	}

	@Override
	public void serialize()
	{
		if (serializeInputs)
		{
			json.add("itemInput", inputItems.get(0).toJson());
		}

		if (serializeOutputs)
		{
			if (outputItems.size() >= 1)
			{
				json.add("itemOutput", outputItems.get(0).toResultJson());
			}
		}
	}
}