package dev.latvian.mods.kubejs.mekanism;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.DataResult;
import dev.latvian.mods.kubejs.error.KubeRuntimeException;
import dev.latvian.mods.kubejs.holder.HolderWrapper;
import dev.latvian.mods.kubejs.script.KubeJSContext;
import dev.latvian.mods.kubejs.script.SourceLine;
import dev.latvian.mods.kubejs.util.ID;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Wrapper;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mojang.serialization.DataResult.error;
import static com.mojang.serialization.DataResult.success;

public interface MekanismChemicalWrapper {
	TypeInfo CHEMICAL_TYPE_INFO = TypeInfo.of(Chemical.class);
	TypeInfo CHEMICAL_HOLDER_TYPE_INFO = TypeInfo.of(Holder.class).withParams(TypeInfo.of(Chemical.class));
	TypeInfo CHEMICAL_STACK_TYPE_INFO = TypeInfo.of(ChemicalStack.class);
	TypeInfo CHEMICAL_INGREDIENT_TYPE_INFO = TypeInfo.of(ChemicalIngredient.class);
	TypeInfo CHEMICAL_STACK_INGREDIENT_TYPE_INFO = TypeInfo.of(ChemicalStackIngredient.class);

	DataResult<ChemicalStack> EMPTY_STACK_RESULT = success(ChemicalStack.EMPTY);
	DataResult<ChemicalIngredient> EMPTY_INGREDIENT_RESULT = success(EmptyChemicalIngredient.INSTANCE);

	@HideFromJS
	private static Holder<Chemical> wrapAsHolder(Chemical chem) {
		return MekanismAPI.CHEMICAL_REGISTRY.wrapAsHolder(chem);
	}

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

	static ChemicalStack stackOf(Context cx, Object from) {
		return tryWrapStack(cx, from)
			.getOrThrow(error -> new KubeRuntimeException("Failed to read chemical stack from %s: %s".formatted(from, error))
				.source(SourceLine.of(cx)));
	}

	@HideFromJS
	@Nullable
	@SuppressWarnings({"rawtypes", "unchecked"})
	static ChemicalStack wrapStackTrivial(Context cx, Object from) {
		while (from instanceof Wrapper w) {
			from = w.unwrap();
		}

		var registries = RegistryAccessContainer.of(cx);

		return switch (from) {
			case null -> ChemicalStack.EMPTY;
			case ChemicalStack id -> id;
			case Chemical chem -> wrapStackTrivial(cx, wrapAsHolder(chem));
			case Holder holder when holder.is(MekanismAPI.EMPTY_CHEMICAL_KEY) -> ChemicalStack.EMPTY;
			case Holder holder -> new ChemicalStack(holder, FluidType.BUCKET_VOLUME);
			default -> null;
		};
	}

	@HideFromJS
	static DataResult<ChemicalStack> tryWrapStack(Context cx, Object from) {
		while (from instanceof Wrapper w) {
			from = w.unwrap();
		}

		var registries = RegistryAccessContainer.of(cx);

		var trivial = wrapStackTrivial(cx, from);
		if (trivial != null) {
			return DataResult.success(trivial);
		}

		// TODO: improve
		var str = from.toString();

		if (str.isEmpty() || str.equals("mekanism:empty")) {
			return EMPTY_STACK_RESULT;
		} else {
			try {
				return readStack(new StringReader(str));
			} catch (CommandSyntaxException ex) {
				return error(ex::getMessage);
			}
		}
	}

	@HideFromJS
	@Nullable
	static ChemicalIngredient wrapIngredientTrivial(Context cx, Object from) {
		return switch (from) {
			case null -> EmptyChemicalIngredient.INSTANCE;
			case ChemicalIngredient c -> c;
			case ChemicalStackIngredient c -> c.ingredient();
			case ChemicalStack c -> new SingleChemicalIngredient(c.getChemicalHolder());
			case Chemical c -> new SingleChemicalIngredient(wrapAsHolder(c));
			default -> null;
		};
	}

	@HideFromJS
	static DataResult<ChemicalIngredient> tryWrapIngredient(Context cx, Object from) {
		if (from instanceof Wrapper w) {
			from = w.unwrap();
		}

		var trivial = wrapIngredientTrivial(cx, from);
		if (trivial != null) {
			return DataResult.success(trivial);
		}

		return switch (from) {
			case Iterable<?> itr -> {
				var results = new ArrayList<ChemicalIngredient>();

				var failed = false;
				Stream.Builder<String> errors = Stream.builder();

				for (var elem : itr) {
					var ingredient = tryWrapIngredient(cx, elem);

					ingredient.resultOrPartial()
						.filter(ingr -> !ingr.isEmpty())
						.ifPresent(results::add);

					if (ingredient.isError()) {
						failed = true;
						errors.add(elem + ": " + ingredient.error().orElseThrow().message());
					}
				}

				if (failed) {
					var msg = errors.build().collect(Collectors.joining("; "));
					yield DataResult.error(() -> "Failed to parse chemical ingredient list: " + msg);
				} else {
					yield DataResult.success(switch (results.size()) {
						case 0 -> EmptyChemicalIngredient.INSTANCE;
						case 1 -> results.getFirst();
						default -> new CompoundChemicalIngredient(results);
					});
				}
			}
			case CharSequence cs when cs.isEmpty() -> EMPTY_INGREDIENT_RESULT;
			case CharSequence cs when cs.equals("mekanism:empty") -> EMPTY_INGREDIENT_RESULT;
			default -> {
				try {
					yield readIngredient(cx, new StringReader(from.toString()));
				} catch (CommandSyntaxException ex) {
					yield error(ex::getMessage);
				}
			}
		};
	}

	static ChemicalIngredient ingredientOf(Context cx, Object from) {
		return tryWrapIngredient(cx, from)
			.getOrThrow(error -> new KubeRuntimeException("Failed to read chemical ingredient from %s: %s".formatted(from, error))
				.source(SourceLine.of(cx)));
	}

	@HideFromJS
	static DataResult<ChemicalStackIngredient> tryWrapStackIngredient(Context cx, Object from) {
		while (from instanceof Wrapper w) {
			from = w.unwrap();
		}

		return switch (from) {
			case null -> error(() -> "Chemical stack ingredient cannot be parsed from null!");
			case ChemicalIngredient c when c.isEmpty() -> error(() -> "Cannot use empty ingredient for chemical stack ingredient!");
			case ChemicalStack c when c.isEmpty() -> error(() -> "Cannot use empty chemical stack for chemical stack ingredient!");
			case ChemicalStackIngredient c -> success(c);
			case ChemicalIngredient c -> success(new ChemicalStackIngredient(c, FluidType.BUCKET_VOLUME));
			case ChemicalStack c -> success(new ChemicalStackIngredient(
				new SingleChemicalIngredient(c.getChemicalHolder()),
				c.getAmount()
			));
			case Chemical c -> success(new ChemicalStackIngredient(
				new SingleChemicalIngredient(wrapAsHolder(c)),
				FluidType.BUCKET_VOLUME
			));
			default -> {
				try {
					yield readStackIngredient(new StringReader(from.toString()));
				} catch (CommandSyntaxException ex) {
					yield error(ex::getMessage);
				}
			}
		};
	}

	static ChemicalStackIngredient stackIngredientOf(Context cx, Object from) {
		return tryWrapStackIngredient(cx, from)
			.getOrThrow(error -> new KubeRuntimeException("Failed to read chemical stack ingredient from %s: %s".formatted(from, error))
				.source(SourceLine.of(cx)));
	}

	@HideFromJS
	private static DataResult<Holder<Chemical>> find(ResourceLocation id) {
		return MekanismAPI.CHEMICAL_REGISTRY
			.getHolder(id)
			.map(DataResult::success)
			.orElseGet(() -> DataResult.error(() -> "Chemical with ID " + id + " does not exist!"))
			.map(Function.identity());
	}

	static DataResult<Holder<Chemical>> read(StringReader reader) {
		reader.skipWhitespace();

		return ID.read(reader).flatMap(MekanismChemicalWrapper::find);
	}

	static DataResult<ChemicalStack> readStack(StringReader reader) throws CommandSyntaxException {
		reader.skipWhitespace();

		var amount = readChemicalAmount(reader);
		var chemical = read(reader);

		return chemical.apply2(ChemicalStack::new, amount);
	}

	static DataResult<ChemicalIngredient> readIngredient(Context cx, StringReader reader) throws CommandSyntaxException {
		reader.skipWhitespace();

		if (reader.peek() == '#') {
			reader.skip();

			return ID.read(reader)
				.map(id -> TagKey.create(MekanismAPI.CHEMICAL_REGISTRY_NAME, id))
				.map(TagChemicalIngredient::new);
		}

		return read(reader).map(SingleChemicalIngredient::new);
	}

	static DataResult<ChemicalStackIngredient> readStackIngredient(StringReader reader) throws CommandSyntaxException {
		reader.skipWhitespace();

		var amount = readChemicalAmount(reader);
		var ingredient = readIngredient(null, reader);

		return ingredient.apply2(ChemicalStackIngredient::new, amount);
	}


	@HideFromJS
	static DataResult<Long> readChemicalAmount(StringReader reader) throws CommandSyntaxException {
		reader.skipWhitespace();

		if (reader.canRead() && StringReader.isAllowedNumber(reader.peek())) {
			var amountd = reader.readDouble();
			reader.skipWhitespace();

			if (reader.peek() == 'b' || reader.peek() == 'B') {
				reader.skip();
				reader.skipWhitespace();
				amountd *= FluidType.BUCKET_VOLUME;
			}

			if (reader.peek() == '/') {
				reader.skip();
				reader.skipWhitespace();
				amountd = amountd / reader.readDouble();
			}

			var amount = (long) amountd;
			reader.expect('x');
			reader.skipWhitespace();

			if (amount < 1) {
				return error(() -> "Fluid amount smaller than 1 is not allowed!");
			}

			return success(amount);
		}

		return success((long) FluidType.BUCKET_VOLUME);
	}
}
