package net.roguelogix.phosphophyllite.quartz.client.gl21java;

import net.roguelogix.phosphophyllite.quartz.client.renderer.IRenderChunkSection;
import net.roguelogix.phosphophyllite.quartz.client.renderer.QuartzRenderer;
import net.roguelogix.phosphophyllite.quartz.common.QuartzBlockRenderInfo;
import net.roguelogix.phosphophyllite.threading.WorkQueue;

import java.util.ArrayList;

public class QuartzRendererGL21Java extends QuartzRenderer {
    
    public static QuartzRendererGL21Java INSTANCE;
    
    public QuartzRendererGL21Java() {
        INSTANCE = this;
    }
    
    public final WorkQueue primaryQueue = new WorkQueue();
    public final WorkQueue secondaryQueue = new WorkQueue();
    public final WorkQueue tertiaryQueue = new WorkQueue().addProcessingThreads(4);
    
    @Override
    public void GLSetup() {
    
    }
    
    @Override
    public void draw() {
        primaryQueue.runAll();
    }
    
    @Override
    public void GLShutdown() {
        primaryQueue.runAll();
        secondaryQueue.enqueue(()->{}).join();
    }
    
    @Override
    public int loadTexture(String textureLocation) {
        return 0;
    }
    
    @Override
    public void setBlockRenderInfo(ArrayList<QuartzBlockRenderInfo> info) {
    
    }
}
