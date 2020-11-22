package dev.latvian.kubejs.mekanism;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.kubejs.item.ingredient.IngredientStackJS;
import dev.latvian.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.kubejs.recipe.RecipeJS;
import dev.latvian.kubejs.util.ListJS;

/**
 * @author LatvianModder
 */
public class MekanismCombiningRecipeJS extends RecipeJS
{
	@Override
	public void create(ListJS args)
	{
		if (args.size() < 3)
		{
			throw new RecipeExceptionJS("Mekanism combining recipe has to have 3 arguments - ouptut, input 1, input 2!");
		}

		outputItems.add(parseResultItem(args.get(0)));
		inputItems.add(parseIngredientItem(args.get(1)).asIngredientStack());
		inputItems.add(parseIngredientItem(args.get(2)).asIngredientStack());
	}

	@Override
	public void deserialize()
	{
		outputItems.add(parseResultItem(json.get("output")));
		inputItems.add(parseIngredientItem(json.get("mainInput")).asIngredientStack());
		inputItems.add(parseIngredientItem(json.get("extraInput")).asIngredientStack());
	}

	@Override
	public void serialize()
	{
		if (serializeOutputs)
		{
			json.add("output", outputItems.get(0).toResultJson());
		}

		if (serializeInputs)
		{
			json.add("mainInput", inputItems.get(0).toJson());
			json.add("extraInput", inputItems.get(1).toJson());
		}
	}

	@Override
	public JsonElement serializeIngredientStack(IngredientStackJS in)
	{
		JsonObject json = new JsonObject();
		json.add("ingredient", in.ingredient.toJson());

		if (in.getCount() > 1)
		{
			json.addProperty("amount", in.getCount());
		}

		return json;
	}
}