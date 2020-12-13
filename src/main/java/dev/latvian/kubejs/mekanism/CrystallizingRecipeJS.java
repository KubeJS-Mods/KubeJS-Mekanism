package dev.latvian.kubejs.mekanism;

import dev.latvian.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.kubejs.util.ListJS;

/**
 * @author LatvianModder
 */
public class CrystallizingRecipeJS extends MekanismRecipeJS
{
	@Override
	public void create(ListJS args)
	{
		throw new RecipeExceptionJS("Creation not supported yet!");
	}

	@Override
	public void deserialize()
	{
		outputItems.add(parseResultItem(json.get("output")));
	}

	@Override
	public void serialize()
	{
		if (serializeOutputs)
		{
			json.add("output", outputItems.get(0).toResultJson());
		}
	}
}