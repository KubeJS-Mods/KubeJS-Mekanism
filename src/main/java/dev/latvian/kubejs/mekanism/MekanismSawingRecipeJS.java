package dev.latvian.kubejs.mekanism;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.kubejs.item.ItemStackJS;
import dev.latvian.kubejs.item.ingredient.IngredientStackJS;
import dev.latvian.kubejs.recipe.RecipeJS;
import dev.latvian.kubejs.util.ListJS;

/**
 * @author LatvianModder
 */
public class MekanismSawingRecipeJS extends RecipeJS
{
	@Override
	public void create(ListJS args)
	{
		outputItems.add(parseResultItem(args.get(0)));
		inputItems.add(parseIngredientItem(args.get(1)).asIngredientStack());

		if (args.size() >= 3)
		{
			outputItems.add(parseResultItem(args.get(2)));
		}
	}

	@Override
	public void deserialize()
	{
		outputItems.add(parseResultItem(json.get("mainOutput")));
		inputItems.add(parseIngredientItem(json.get("input")).asIngredientStack());

		if (json.has("secondaryOutput"))
		{
			ItemStackJS stackJS = parseResultItem(json.get("secondaryOutput"));

			if (json.has("secondaryChance"))
			{
				stackJS.setChance(json.get("secondaryChance").getAsDouble());
			}

			outputItems.add(stackJS);
		}
	}

	@Override
	public void serialize()
	{
		if (serializeOutputs)
		{
			json.add("mainOutput", outputItems.get(0).toResultJson());

			if (outputItems.size() >= 2)
			{
				ItemStackJS stackJS = outputItems.get(1).getCopy();
				double c = stackJS.getChance();
				stackJS.setChance(-1D);
				json.add("secondaryOutput", stackJS.toResultJson());
				json.addProperty("secondaryChance", c);
			}
		}

		if (serializeInputs)
		{
			json.add("input", inputItems.get(0).toJson());
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