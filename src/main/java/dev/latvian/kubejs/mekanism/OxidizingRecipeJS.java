package dev.latvian.kubejs.mekanism;

import dev.latvian.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.kubejs.util.ListJS;

/**
 * @author LatvianModder
 */
public class OxidizingRecipeJS extends MekanismRecipeJS
{
	@Override
	public void create(ListJS args)
	{
		throw new RecipeExceptionJS("Creation not supported yet!");
	}

	@Override
	public void deserialize()
	{
		inputItems.add(parseIngredientItem(json.get("input")).asIngredientStack());
	}

	@Override
	public void serialize()
	{
		if (serializeInputs)
		{
			json.add("input", inputItems.get(0).toJson());
		}
	}
}