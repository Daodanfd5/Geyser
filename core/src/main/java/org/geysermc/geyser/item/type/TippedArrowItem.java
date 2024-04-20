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

package org.geysermc.geyser.item.type;

import com.github.steveice10.mc.protocol.data.game.item.component.DataComponents;
import com.github.steveice10.opennbt.tag.builtin.StringTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.inventory.item.TippedArrowPotion;
import org.geysermc.geyser.registry.type.ItemMapping;
import org.geysermc.geyser.registry.type.ItemMappings;
import org.geysermc.geyser.translator.item.ItemTranslator;

public class TippedArrowItem extends ArrowItem {
    public TippedArrowItem(String javaIdentifier, Builder builder) {
        super(javaIdentifier, builder);
    }

    @Override
    public ItemData.Builder translateToBedrock(int count, DataComponents components, ItemMapping mapping, ItemMappings mappings) {
        Tag potionTag = itemStack.getNbt().get("Potion");
        if (potionTag instanceof StringTag) {
            TippedArrowPotion tippedArrowPotion = TippedArrowPotion.getByJavaIdentifier(((StringTag) potionTag).getValue());
            if (tippedArrowPotion != null) {
                return ItemData.builder()
                        .definition(mapping.getBedrockDefinition())
                        .damage(tippedArrowPotion.getBedrockId())
                        .count(count);
            }
            GeyserImpl.getInstance().getLogger().debug("Unknown Java potion (tipped arrow): " + potionTag.getValue());
        }
        return super.translateToBedrock(count, components, mapping, mappings);
    }
}
