/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.signal;

import java.util.Set;
import java.util.UUID;
import net.minecraft.util.math.BlockPos;

/**
 * Created by CovertJaguar on 7/26/2017 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface TokenRing extends SignalNetwork<TokenSignal> {

  Set<UUID> getTrackedCarts();

  UUID getId();

  BlockPos getCentroid();
}
