package factorization.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatisticsFile;

public class StatUtil {
    public static StatisticsFile getStatsFile(EntityPlayer player) {
        MinecraftServer server = MinecraftServer.getServer();
        if (server == null) return null;
        ServerConfigurationManager cm = server.getConfigurationManager();
        return cm.getPlayerStatsFile(player);
    }

    public static class FzStat {
        final EntityPlayer player;
        final StatBase stat;
        final StatisticsFile statsFile;

        public FzStat(EntityPlayer player, StatBase stat) {
            this.player = player;
            this.stat = stat;
            statsFile = getStatsFile(player);
        }

        public int get() {
            if (statsFile == null) return 0;
            // NORELEASE: How do you read from stats files now?
            return 0;
        }

        public void set(int val) {
            if (statsFile == null) return;
            statsFile.unlockAchievement(player, stat, val);
        }

        public int add(int val) {
            set(get() + val);
            return get();
        }
    }

    public static FzStat load(EntityPlayer player, StatBase field) {
        return new FzStat(player, field);
    }
}
