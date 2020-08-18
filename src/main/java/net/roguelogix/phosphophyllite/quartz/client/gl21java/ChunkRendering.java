package net.roguelogix.phosphophyllite.quartz.client.gl21java;


import net.minecraft.util.ResourceLocation;
import net.roguelogix.phosphophyllite.quartz.client.renderer.IRenderChunkSection;
import net.roguelogix.phosphophyllite.threading.Event;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.opengl.GL21.*;
import static net.roguelogix.phosphophyllite.Phosphophyllite.modid;
import static net.roguelogix.phosphophyllite.quartz.client.gl21java.QuartzRendererGL21Java.INSTANCE;

public class ChunkRendering {
    
    private static Program blockProgram;
    
    public static void GLStartup() {
        blockProgram = ShaderRegistry.getOrLoadProgram(new ResourceLocation(modid, "shaders/120/block"));
    }
    
    public static void GLShutdown() {
        blockProgram = null;
        chunks.clear();
    }
    
    private static final Map<Vector3i, RenderChunk> chunks = new ConcurrentHashMap<>();
    
    public static void draw() {
        blockProgram.bind();
        
        synchronized (chunks) {
            chunks.values().forEach(RenderChunk::draw);
        }
        
    }
    
//    ThreadLocal<HashMap<Vector3i, >>
    
    /**
     * seems like a mundane function doesnt need
     * WRONG!!! this can cause render chunks to be created or destroyed!
     * <p>
     * while it may seem slow to have this done as it is, chances are neighboring blocks aren't
     * neighboring in the buffer, and this is updated on a secondary OpenGL thread
     * so, this never hits the main render thread
     * <p>
     * safe to call from any thread, and concurrently, recommended to use the tertiary work queue for it
     **/
    public static void setRenderInfos(IRenderChunkSection.BlockRenderInfo... info) {
//        // ok, first thing, i need the render chunk
//        Vector3i chunkLocation = new Vector3i(location).div(16);
//        location = new Vector3i(location);
//        RenderChunk chunk = chunks.get(chunkLocation);
//        if (chunk == null) {
//            chunk = new RenderChunk(chunkLocation);
//        }
//        location.x &= 0xFF;
//        location.y &= 0xFF;
//        location.z &= 0xFF;
//        final RenderChunk finalChunk = chunk;
//        Vector3i finalLocation = location;
//        chunk.setupEvent.registerCallback(() -> INSTANCE.tertiaryQueue.enqueue(() -> {
//            finalChunk.setRenderInfo(info, finalLocation);
//            if (finalChunk.isEmpty()) {
//                chunks.remove(chunkLocation);
//                INSTANCE.secondaryQueue.enqueue(finalChunk::shutdown);
//            }
//        }));
    }
    
    static class RenderChunk {
        final Event setupEvent;
        
        RenderChunk(Vector3i location) {
            setupEvent = INSTANCE.secondaryQueue.enqueue(() -> {
                startup();
                chunks.put(location, this);
            });
        }
        
        private boolean hasUpdate = false;
        private int newElements = 0;
        private HashMap<Vector3i, Integer> positions = new HashMap<>();
        private ArrayList<Integer> VBOpositions = new ArrayList<>();
        private ArrayList<Integer> EBOpositions = new ArrayList<>();
        
        private int elements = 0;
        private int VBO;
        private int EBO;
        
        void setRenderInfos(IRenderChunkSection.BlockRenderInfo... info) {
        
        }
        
        void startup() {
            VBO = glGenBuffers();
            EBO = glGenBuffers();
        }
        
        void shutdown() {
            glDeleteBuffers(VBO);
            glDeleteBuffers(EBO);
        }
        
        boolean isEmpty() {
            return elements == 0;
        }
        
        synchronized void draw() {
            if (elements == 0) {
                return;
            }
            glBindBuffer(VBO, GL_ARRAY_BUFFER);
            glBindBuffer(VBO, GL_ELEMENT_ARRAY_BUFFER);
            glDrawElements(GL_TRIANGLES, elements, GL_UNSIGNED_INT, 0);
        }
    }
}
