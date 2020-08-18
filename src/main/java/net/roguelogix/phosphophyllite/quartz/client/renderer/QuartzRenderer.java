package net.roguelogix.phosphophyllite.quartz.client.renderer;

import net.roguelogix.phosphophyllite.quartz.client.QuartzClientConfig;
import net.roguelogix.phosphophyllite.quartz.client.gl21java.QuartzRendererGL21Java;
import org.joml.Vector3i;

public abstract class QuartzRenderer {
    
    public static QuartzRenderer INSTANCE = null;
    
    public static void create(){
        //noinspection SwitchStatementWithTooFewBranches
        switch (QuartzClientConfig.OPERATION_MODE){
            case GL21_JAVA:{
                INSTANCE = new QuartzRendererGL21Java();
                break;
            }
        }
    }
    
    protected QuartzRenderer(){
    }
    
    public abstract void GLSetup();
    
    public abstract void draw();
    
    public abstract void GLShutdown();
    
    public abstract void setBlockRenderInfo(IRenderChunkSection.BlockRenderInfo info, Vector3i position);
}
