/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.signal;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface SignalControllerProvider extends Signal {

  SignalController getSignalController();
}
