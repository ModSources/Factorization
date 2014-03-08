package factorization.shared;

import factorization.common.FactoryType;
import net.minecraft.client.renderer.RenderBlocks;

public class BlockRenderDefault extends FactorizationBlockRender {

    @Override
    public boolean render(RenderBlocks rb) {
        if (world_mode) {
            TileEntityCommon c = getCoord().getTE(TileEntityCommon.class);
            if (c == null) {
                return false;
            }
            renderNormalBlock(rb, c.getFactoryType().md);
            return true;
        } else {
            renderNormalBlock(rb, metadata);
        }
        return false;
    }

    @Override
    public FactoryType getFactoryType() {
        return null;
    }

}
