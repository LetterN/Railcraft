/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.item;

import net.minecraft.item.ItemStack;

/**
 * Created by CovertJaguar on 5/26/2017 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface Filter {

    default boolean matches(ItemStack matcher, ItemStack target) {
        return false;
    }
}
