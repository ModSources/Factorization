package factorization.servo;

import codechicken.nei.WorldOverlayRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import factorization.api.Coord;
import factorization.common.FactoryType;
import factorization.shared.Core.TabType;
import factorization.shared.Core;
import factorization.shared.ItemFactorization;
import factorization.shared.NetworkFactorization;
import factorization.shared.NetworkFactorization.MessageType;

public class ItemCommenter extends ItemFactorization {

    public ItemCommenter(int itemId, String name) {
        super(itemId, name, TabType.SERVOS);
        setMaxStackSize(1);
    }

    @Override
    public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int side, float vx,
            float vy, float vz) {
        if (player == null) {
            return false;
        }
        Coord at = new Coord(world, x, y, z);
        TileEntityServoRail rail = at.getTE(TileEntityServoRail.class);
        if (rail == null) {
            return false;
        }
        if (!world.isRemote) {
            rail.broadcastMessage(player, MessageType.ServoRailEditComment, rail.comment == null ? "" : rail.comment);
        }
        return true;
    }
}
