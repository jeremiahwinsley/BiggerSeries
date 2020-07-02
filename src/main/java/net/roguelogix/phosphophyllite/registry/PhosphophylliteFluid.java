package net.roguelogix.phosphophyllite.registry;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.state.StateContainer;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import javax.annotation.Nonnull;

public class PhosphophylliteFluid extends ForgeFlowingFluid {
    
    protected PhosphophylliteFluid(Properties properties) {
        super(properties);
        setDefaultState(getDefaultState().with(LEVEL_1_8, 8));
    }
    
    @Override
    protected void fillStateContainer(@Nonnull StateContainer.Builder<Fluid, IFluidState> builder) {
        super.fillStateContainer(builder);
        builder.add(LEVEL_1_8);
    }
    
    boolean isSource = false;
    PhosphophylliteFluid flowingVariant;
    
    @Override
    public boolean isSource(@Nonnull IFluidState state) {
        return isSource;
    }
    
    @Override
    public int getLevel(IFluidState state) {
        return state.get(LEVEL_1_8);
    }
}
