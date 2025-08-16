package dev.latvian.mods.kubejs.mekanism;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.latvian.mods.kubejs.fluid.FluidWrapper;
import dev.latvian.mods.kubejs.holder.HolderWrapper;
import dev.latvian.mods.kubejs.script.KubeJSContext;
import dev.latvian.mods.rhino.type.TypeInfo;
import dev.latvian.mods.rhino.util.HideFromJS;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.chemical.ChemicalIngredient;
import mekanism.api.recipes.ingredients.chemical.CompoundChemicalIngredient;
import mekanism.api.recipes.ingredients.chemical.DifferenceChemicalIngredient;
import mekanism.api.recipes.ingredients.chemical.EmptyChemicalIngredient;
import mekanism.api.recipes.ingredients.chemical.SingleChemicalIngredient;
import mekanism.api.recipes.ingredients.chemical.TagChemicalIngredient;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.ArrayList;
import java.util.List;

public interface MekanismChemicalWrapper {
	TypeInfo CHEMICAL_TYPE_INFO = TypeInfo.of(Chemical.class);
	TypeInfo CHEMICAL_HOLDER_TYPE_INFO = TypeInfo.of(Holder.class).withParams(TypeInfo.of(Chemical.class));
	TypeInfo CHEMICAL_STACK_TYPE_INFO = TypeInfo.of(ChemicalStack.class);
	TypeInfo CHEMICAL_INGREDIENT_TYPE_INFO = TypeInfo.of(ChemicalIngredient.class);
	TypeInfo CHEMICAL_STACK_INGREDIENT_TYPE_INFO = TypeInfo.of(ChemicalStackIngredient.class);

	static Holder<Chemical> of(KubeJSContext cx, Object from) {
		return switch (from) {
			case null -> MekanismAPI.EMPTY_CHEMICAL_HOLDER;
			case ChemicalStack c -> c.getChemicalHolder();
			default -> (Holder) HolderWrapper.wrap(cx, from, CHEMICAL_TYPE_INFO);
		};
	}

	static ChemicalStack stack(Holder<Chemical> chemical, long amount) {
		return new ChemicalStack(chemical, amount);
	}

	static ChemicalIngredient ingredientExcept(ChemicalIngredient ingredient, ChemicalIngredient except) {
		return new DifferenceChemicalIngredient(ingredient, except);
	}

	static ChemicalIngredient ingredientIntersection(List<ChemicalIngredient> children) {
		return new CompoundChemicalIngredient(children);
	}

	static ChemicalStackIngredient ingredientStack(ChemicalIngredient ingredient, long amount) {
		return new ChemicalStackIngredient(ingredient, amount);
	}

	static ChemicalStack stackOf(Object from) {
		return switch (from) {
			case null -> null;
			case ChemicalStack c -> c;
			case Chemical c -> new ChemicalStack(c.getAsHolder(), FluidType.BUCKET_VOLUME);
			case Holder<?> c -> new ChemicalStack((Holder) c, FluidType.BUCKET_VOLUME);
			default -> {
				var str = from.toString();

				if (str.isEmpty() || str.equals("mekanism:empty")) {
					yield ChemicalStack.EMPTY;
				} else {
					try {
						yield readStack(new StringReader(str));
					} catch (CommandSyntaxException ex) {
						ex.printStackTrace();
						yield ChemicalStack.EMPTY;
					}
				}
			}
		};
	}

	static Holder<Chemical> read(StringReader reader) throws CommandSyntaxException {
		reader.skipWhitespace();
		return MekanismAPI.CHEMICAL_REGISTRY.getHolder(ResourceLocation.read(reader)).get();
	}

	static ChemicalStack readStack(StringReader reader) throws CommandSyntaxException {
		reader.skipWhitespace();
		var amount = FluidWrapper.readFluidAmount(reader);
		var chemical = read(reader);
		return new ChemicalStack(chemical, amount);
	}

	static ChemicalIngredient ingredientOf(Object from) {
		return switch (from) {
			case null -> EmptyChemicalIngredient.INSTANCE;
			case ChemicalIngredient c -> c;
			case ChemicalStackIngredient c -> c.ingredient();
			case ChemicalStack c -> new SingleChemicalIngredient(c.getChemicalHolder());
			case Chemical c -> new SingleChemicalIngredient(c.getAsHolder());
			case Iterable<?> itr -> {
				var a = new ArrayList<ChemicalIngredient>();

				for (var o : itr) {
					var i = ingredientOf(o);

					if (!i.isEmpty()) {
						a.add(i);
					}
				}

				yield a.isEmpty() ? EmptyChemicalIngredient.INSTANCE : a.size() == 1 ? a.getFirst() : new CompoundChemicalIngredient(a);
			}
			default -> {
				var str = from.toString();

				if (str.isEmpty() || str.equals("mekanism:empty")) {
					yield EmptyChemicalIngredient.INSTANCE;
				} else {
					try {
						yield readIngredient(new StringReader(str));
					} catch (CommandSyntaxException ex) {
						ex.printStackTrace();
						yield EmptyChemicalIngredient.INSTANCE;
					}
				}
			}
		};
	}

	static ChemicalIngredient readIngredient(StringReader reader) throws CommandSyntaxException {
		reader.skipWhitespace();

		if (reader.peek() == '#') {
			reader.skip();
			return new TagChemicalIngredient(TagKey.create(MekanismAPI.CHEMICAL_REGISTRY_NAME, ResourceLocation.read(reader)));
		}

		return new SingleChemicalIngredient(read(reader));
	}

	@HideFromJS
	static ChemicalStackIngredient stackIngredientOf(Object from) {
		return switch (from) {
			case null -> new ChemicalStackIngredient(EmptyChemicalIngredient.INSTANCE, 1);
			case ChemicalStackIngredient c -> c;
			case ChemicalIngredient c -> new ChemicalStackIngredient(c, FluidType.BUCKET_VOLUME);
			case ChemicalStack c -> new ChemicalStackIngredient(new SingleChemicalIngredient(c.getChemicalHolder()), c.getAmount());
			case Chemical c -> new ChemicalStackIngredient(new SingleChemicalIngredient(c.getAsHolder()), FluidType.BUCKET_VOLUME);
			default -> {
				var str = from.toString();

				try {
					yield readStackIngredient(new StringReader(str));
				} catch (CommandSyntaxException ex) {
					ex.printStackTrace();
					yield new ChemicalStackIngredient(EmptyChemicalIngredient.INSTANCE, 1);
				}
			}
		};
	}

	static ChemicalStackIngredient readStackIngredient(StringReader reader) throws CommandSyntaxException {
		reader.skipWhitespace();
		var amount = FluidWrapper.readFluidAmount(reader);
		reader.skipWhitespace();
		var ingredient = readIngredient(reader);
		return new ChemicalStackIngredient(ingredient, amount);
	}
}
