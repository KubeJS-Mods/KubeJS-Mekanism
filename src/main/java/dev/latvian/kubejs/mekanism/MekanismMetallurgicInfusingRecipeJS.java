package dev.latvian.kubejs.mekanism;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.kubejs.item.ingredient.IngredientStackJS;
import dev.latvian.kubejs.recipe.RecipeJS;
import dev.latvian.kubejs.util.ListJS;

/**
 * @author LatvianModder
 */
public class MekanismMetallurgicInfusingRecipeJS extends RecipeJS
{
	public String infusionTag = "mekanism:redstone";
	public int infusionAmount = 10;

	@Override
	public void create(ListJS args)
	{
		outputItems.add(parseResultItem(args.get(0)));
		inputItems.add(parseIngredientItem(args.get(1)).asIngredientStack());

		if (args.size() >= 3)
		{
			infusionTag = args.get(2).toString();

			if (args.size() >= 4)
			{
				infusionAmount = ((Number) args.get(3)).intValue();
			}
		}
	}

	@Override
	public void deserialize()
	{
		outputItems.add(parseResultItem(json.get("output")));
		inputItems.add(parseIngredientItem(json.get("itemInput")).asIngredientStack());

		if (json.has("infusionInput"))
		{
			JsonObject o = json.get("infusionInput").getAsJsonObject();

			if (o.has("tag"))
			{
				infusionTag = o.get("tag").getAsString();
			}

			if (o.has("amount"))
			{
				infusionAmount = o.get("amount").getAsInt();
			}
		}
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
			json.add("itemInput", inputItems.get(0).toJson());

			JsonObject o = new JsonObject();
			o.addProperty("tag", infusionTag);
			o.addProperty("amount", infusionAmount);
			json.add("infusionInput", o);
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