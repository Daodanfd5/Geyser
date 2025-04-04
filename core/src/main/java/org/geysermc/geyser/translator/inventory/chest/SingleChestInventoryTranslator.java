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

package org.geysermc.geyser.translator.inventory.chest;

import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType;
import org.geysermc.geyser.inventory.Container;
import org.geysermc.geyser.inventory.Inventory;
import org.geysermc.geyser.inventory.holder.BlockInventoryHolder;
import org.geysermc.geyser.inventory.holder.InventoryHolder;
import org.geysermc.geyser.level.block.Blocks;
import org.geysermc.geyser.level.block.property.ChestType;
import org.geysermc.geyser.level.block.property.Properties;
import org.geysermc.geyser.level.block.type.BlockState;
import org.geysermc.geyser.session.GeyserSession;

public class SingleChestInventoryTranslator extends ChestInventoryTranslator {
    private final InventoryHolder holder;

    public SingleChestInventoryTranslator(int size) {
        super(size, 27);
        this.holder = new BlockInventoryHolder(Blocks.CHEST.defaultBlockState().withValue(Properties.CHEST_TYPE, ChestType.SINGLE), ContainerType.CONTAINER,
                Blocks.ENDER_CHEST, Blocks.TRAPPED_CHEST, Blocks.BARREL) {
            @Override
            protected boolean isValidBlock(BlockState blockState) {
                if (blockState.is(Blocks.ENDER_CHEST) || blockState.is(Blocks.BARREL)) {
                    // Can't have double ender chests or barrels
                    return true;
                }

                // Add provision to ensure this isn't a double chest
                return super.isValidBlock(blockState) && blockState.getValue(Properties.CHEST_TYPE) == ChestType.SINGLE;
            }
        };
    }

    @Override
    public boolean prepareInventory(GeyserSession session, Inventory inventory) {
        return holder.prepareInventory(session, (Container) inventory);
    }

    @Override
    public void openInventory(GeyserSession session, Inventory inventory) {
        holder.openInventory(session, (Container) inventory);
    }

    @Override
    public void closeInventory(GeyserSession session, Inventory inventory) {
        holder.closeInventory(session, (Container) inventory, ContainerType.CONTAINER);
    }
}
