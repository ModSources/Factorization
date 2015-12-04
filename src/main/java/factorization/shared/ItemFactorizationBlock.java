package factorization.shared;

import factorization.api.Coord;
import factorization.ceramics.TileEntityGreenware;
import factorization.ceramics.TileEntityGreenware.ClayState;
import factorization.charge.TileEntityLeydenJar;
import factorization.common.FactoryType;
import factorization.util.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemFactorizationBlock extends ItemBlock {
    public ItemFactorizationBlock(Block id) {
        super(id);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public boolean placeBlockAt(ItemStack is, EntityPlayer player,
            World w, BlockPos pos, EnumFacing side, float hitX, float hitY,
            float hitZ, IBlockState newState) {
        Coord here = new Coord(w, pos);
        FactoryType f = FactoryType.fromMd((byte) is.getItemDamage());
        if (f == null) {
            is.stackSize = 0;
            return false;
        }
        TileEntityCommon tec = f.makeTileEntity();
        if (tec == null) return false;
        here.setAsTileEntityLocation(tec);
        Coord placedAgainst = here.add(side.getOpposite());
        boolean good = tec.canPlaceAgainst(player, placedAgainst, side);
        if (!good) {
            return false;
        }
        if (super.placeBlockAt(is, player, w, pos, side, hitX, hitY, hitZ, newState)) {
            here.setAsTileEntityLocation(tec);
            tec.onPlacedBy(player, is, side, hitX, hitY, hitZ);
            tec.getBlockClass().enforce(here);
            here.setTE(tec);
            
            here.markBlockForUpdate();
            return true;
        }
        return false;
    }

    @Override
    public int getMetadata(int i) {
        return 15;
    }
    
    @Override
    public String getUnlocalizedName(ItemStack is) {
        int md = is.getItemDamage();
        FactoryType ft = FactoryType.fromMd((byte) md);
        return "factorization.factoryBlock." + ft;
    }

    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List infoList, boolean verbose) {
        if (Core.registry.greenware_item != null && is.isItemEqual(Core.registry.greenware_item) /* required to not compare NBT here */) {
            NBTTagCompound tag = is.getTagCompound();
            if (tag != null) {
                TileEntityGreenware teg = (TileEntityGreenware) FactoryType.CERAMIC.getRepresentative();
                teg.readFromNBT(tag);
                ClayState state = teg.getState();
                infoList.add(teg.parts.size() + " parts");
                infoList.add(state.toString());
            }
        }
        if (ItemUtil.similar(is, Core.registry.leydenjar_item)) {
            int perc = 0;
            if (is.hasTagCompound()) {
                FactoryType ft = FactoryType.LEYDENJAR;
                TileEntityLeydenJar jar = (TileEntityLeydenJar) ft.getRepresentative();
                jar.loadFromStack(is);
                perc = (int)(jar.getLevel()*100);
                //infoList.add(( + "% charged"));
            }
            infoList.add(StatCollector.translateToLocalFormatted("factorization.factoryBlock.LEYDENJAR.perc", perc));
        }
        Core.brand(is, player, infoList, verbose);
    }
    
    @Override
    public boolean getShareTag() {
        return true;
    }
}
