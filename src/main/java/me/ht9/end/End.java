/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package me.ht9.end;

import me.ht9.end.core.Core;
import me.ht9.end.event.bus.EventBus;
import me.ht9.end.threading.Threading;
import me.ht9.end.util.Globals;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class End implements ClientModInitializer, Globals
{
	private static final Logger logger = LogManager.getLogger("end");

	private static final EventBus bus = new EventBus();
	
	@Override
	public void onInitializeClient()
	{
		logger.info("loading end...");
		double startTime = Threading.timedSyncRun(Core::init);
		End.bus().register(this);
		logger.info(String.format(
				"successfully loaded end in %ss.",
				startTime
		));
	}

	public static EventBus bus()
	{
		return bus;
	}

	public static Logger getLogger()
	{
		return logger;
	}
}
