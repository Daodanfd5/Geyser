/*
 * Copyright (c) 2019-2022 GeyserMC. http://geysermc.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author GeyserMC
 * @link https://github.com/GeyserMC/Geyser
 */

package org.geysermc.geyser.registry.loader;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Supplier;

/**
 * Holds common {@link RegistryLoader}s or utility methods surrounding them.
 */
public final class RegistryLoaders {
    /**
     * The {@link RegistryLoader} responsible for loading NBT.
     */
    public static final NbtRegistryLoader NBT = new NbtRegistryLoader();

    /**
     * The {@link RegistryLoader} responsible for loading biome data.
     */
    public static final BiomeLoader BIOME_LOADER = new BiomeLoader();

    /**
     * The {@link RegistryLoader} responsible for loading resource packs.
     */
    public static final ResourcePackLoader RESOURCE_PACKS = new ResourcePackLoader();

    /**
     * Wraps the surrounding {@link Supplier} in a {@link RegistryLoader} which does
     * not take in any input value.
     *
     * @param supplier the supplier
     * @param <V> the value
     * @return a RegistryLoader wrapping the given Supplier
     */
    public static <V> RegistryLoader<Object, V> empty(@NonNull Supplier<V> supplier) {
        return input -> supplier.get();
    }

    /**
     * Returns a {@link RegistryLoader} which has not taken
     * in any input value.
     *
     * @param <I> the input
     * @param <V> the value
     * @return a RegistryLoader that is yet to contain a value.
     */
    public static <I, V> RegistryLoader<I, V> uninitialized() {
        return input -> null;
    }

    private RegistryLoaders() {
    }
}
