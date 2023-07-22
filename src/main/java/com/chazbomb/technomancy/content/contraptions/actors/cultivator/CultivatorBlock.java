package com.chazbomb.technomancy.content.contraptions.actors.cultivator;

import com.mojang.authlib.GameProfile;
import com.simibubi.create.content.contraptions.actors.AttachedActorBlock;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.FakePlayer;

import java.util.UUID;

public class CultivatorBlock extends AttachedActorBlock {

	public CultivatorBlock(Properties props) {
		super(props);
	}

	static class CultivatorFakePlayer extends FakePlayer {

		public static final GameProfile CULTIVATOR_PROFILE =
				new GameProfile(UUID.fromString("dc1747bd-9f20-46c8-8c88-d04c3114e176"), "Cultivator");

		public CultivatorFakePlayer(ServerLevel world) {
			super(world, CULTIVATOR_PROFILE);
		}

	}
}
