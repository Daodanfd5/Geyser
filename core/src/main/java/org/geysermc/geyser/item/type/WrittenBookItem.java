/*
 * Copyright (c) 2019-2023 GeyserMC. http://geysermc.org
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

import com.github.steveice10.mc.protocol.data.game.item.component.DataComponentType;
import com.github.steveice10.mc.protocol.data.game.item.component.DataComponents;
import com.github.steveice10.mc.protocol.data.game.item.component.Filterable;
import com.github.steveice10.mc.protocol.data.game.item.component.WrittenBookContent;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.ListTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.cloudburstmc.nbt.NbtType;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.item.BedrockItemBuilder;
import org.geysermc.geyser.translator.text.MessageTranslator;

import java.util.ArrayList;
import java.util.List;

public class WrittenBookItem extends Item {
    public static final int MAXIMUM_PAGE_EDIT_LENGTH = 1024;
    public static final int MAXIMUM_PAGE_LENGTH = 32768;
    public static final int MAXIMUM_PAGE_COUNT = 100; // Java edition limit. Bedrock edition has a limit of 50 pages.
    public static final int MAXIMUM_TITLE_LENGTH = 16;

    public WrittenBookItem(String javaIdentifier, Builder builder) {
        super(javaIdentifier, builder);
    }

    @Override
    public void translateComponentsToBedrock(@NonNull GeyserSession session, @NonNull DataComponents components, @NonNull BedrockItemBuilder builder) {
        super.translateComponentsToBedrock(session, components, builder);

        WrittenBookContent bookContent = components.get(DataComponentType.WRITTEN_BOOK_CONTENT);
        if (bookContent == null) {
            return;
        }
        List<NbtMap> bedrockPages = new ArrayList<>();
        for (Filterable<Component> page : bookContent.getPages()) {
            NbtMapBuilder pageBuilder = NbtMap.builder();
            pageBuilder.putString("photoname", "");
            pageBuilder.putString("text", MessageTranslator.convertMessage(page.getRaw()));
            bedrockPages.add(pageBuilder.build());
        }
        builder.putList("pages", NbtType.COMPOUND, bedrockPages);

        builder.putString("title", bookContent.getTitle().getRaw())
                .putString("author", bookContent.getAuthor());
        // TODO isResolved
    }

    private boolean isValidWrittenBook(CompoundTag tag) {
        if (!(tag.get("title") instanceof StringTag title)) {
            return false;
        }
        if (title.getValue().length() > (MAXIMUM_TITLE_LENGTH * 2)) {
            // Java rejects books with titles more than 2x the maximum length allowed in the input box
            return false;
        }

        if (!(tag.get("author") instanceof StringTag)) {
            return false;
        }

        if (!(tag.get("pages") instanceof ListTag pages)) {
            return false;
        }
        for (Tag pageTag : pages) {
            if (pageTag instanceof StringTag page) {
                if (page.getValue().length() > MAXIMUM_PAGE_LENGTH) {
                    return false;
                }
            }
        }
        return true;
    }
}
