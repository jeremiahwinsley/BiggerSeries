package net.roguelogix.phosphophyllite.quartz.common;

import net.roguelogix.phosphophyllite.robn.ROBNObject;

import java.util.HashMap;
import java.util.Map;

/**
 * unpacked raw render data
 * passed almost directly to OpenGL
 *
 * DO NOT EDIT THIS
 * this object is used to pass data between Java and C++ via ROBN
 * editing it can potentially break Quartz
 */
public class QuartzBlockRenderInfo implements ROBNObject {
    
    public int x, y, z;
    
    // -1 signals to not render that face
    // will be ignored in buffer
    public int textureIDWest = -1;
    public int textureIDEast = -1;
    public int textureIDBottom = -1;
    public int textureIDTop = -1;
    public int textureIDSouth = -1;
    public int textureIDNorth = -1;
    
    public byte textureRotationWest = 0;
    public byte textureRotationEast = 0;
    public byte textureRotationBottom = 0;
    public byte textureRotationTop = 0;
    public byte textureRotationSouth = 0;
    public byte textureRotationNorth = 0;
    
    public byte lightmapBlocklightWestLYLZ = 0x3F;
    public byte lightmapSkylightWestLYLZ = 0x3F;
    public byte lightmapBlocklightWestHYLZ = 0x3F;
    public byte lightmapSkylightWestHYLZ = 0x3F;
    public byte lightmapBlocklightWestLYHZ = 0x3F;
    public byte lightmapSkylightWestLYHZ = 0x3F;
    public byte lightmapBlocklightWestHYHZ = 0x3F;
    public byte lightmapSkylightWestHYHZ = 0x3F;
    
    public byte lightmapBlocklightEastLYLZ = 0x3F;
    public byte lightmapSkylightEastLYLZ = 0x3F;
    public byte lightmapBlocklightEastHYLZ = 0x3F;
    public byte lightmapSkylightEastHYLZ = 0x3F;
    public byte lightmapBlocklightEastLYHZ = 0x3F;
    public byte lightmapSkylightEastLYHZ = 0x3F;
    public byte lightmapBlocklightEastHYHZ = 0x3F;
    public byte lightmapSkylightEastHYHZ = 0x3F;
    
    public byte lightmapBlocklightTopLYLZ = 0x3F;
    public byte lightmapSkylightTopLYLZ = 0x3F;
    public byte lightmapBlocklightTopHYLZ = 0x3F;
    public byte lightmapSkylightTopHYLZ = 0x3F;
    public byte lightmapBlocklightTopLYHZ = 0x3F;
    public byte lightmapSkylightTopLYHZ = 0x3F;
    public byte lightmapBlocklightTopHYHZ = 0x3F;
    public byte lightmapSkylightTopHYHZ = 0x3F;
    
    public byte lightmapBlocklightBottomLYLZ = 0x3F;
    public byte lightmapSkylightBottomLYLZ = 0x3F;
    public byte lightmapBlocklightBottomHYLZ = 0x3F;
    public byte lightmapSkylightBottomHYLZ = 0x3F;
    public byte lightmapBlocklightBottomLYHZ = 0x3F;
    public byte lightmapSkylightBottomLYHZ = 0x3F;
    public byte lightmapBlocklightBottomHYHZ = 0x3F;
    public byte lightmapSkylightBottomHYHZ = 0x3F;
    
    public byte lightmapBlocklightSouthLYLZ = 0x3F;
    public byte lightmapSkylightSouthLYLZ = 0x3F;
    public byte lightmapBlocklightSouthHYLZ = 0x3F;
    public byte lightmapSkylightSouthHYLZ = 0x3F;
    public byte lightmapBlocklightSouthLYHZ = 0x3F;
    public byte lightmapSkylightSouthLYHZ = 0x3F;
    public byte lightmapBlocklightSouthHYHZ = 0x3F;
    public byte lightmapSkylightSouthHYHZ = 0x3F;
    
    public byte lightmapBlocklightNorthLYLZ = 0x3F;
    public byte lightmapSkylightNorthLYLZ = 0x3F;
    public byte lightmapBlocklightNorthHYLZ = 0x3F;
    public byte lightmapSkylightNorthHYLZ = 0x3F;
    public byte lightmapBlocklightNorthLYHZ = 0x3F;
    public byte lightmapSkylightNorthLYHZ = 0x3F;
    public byte lightmapBlocklightNorthHYHZ = 0x3F;
    public byte lightmapSkylightNorthHYHZ = 0x3F;
    
    @Override
    public Map<String, Object> toROBNMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("x", x);
        map.put("y", y);
        map.put("z", z);
        
        map.put("textureRotationWest", textureRotationWest);
        map.put("textureRotationEast", textureRotationEast);
        map.put("textureRotationBottom", textureRotationBottom);
        map.put("textureRotationTop", textureRotationTop);
        map.put("textureRotationSouth", textureRotationSouth);
        map.put("textureRotationNorth", textureRotationNorth);
        
        map.put("lightmapBlocklightWestLYLZ", lightmapBlocklightWestLYLZ);
        map.put("lightmapSkylightWestLYLZ", lightmapSkylightWestLYLZ);
        map.put("lightmapBlocklightWestHYLZ", lightmapBlocklightWestHYLZ);
        map.put("lightmapSkylightWestHYLZ", lightmapSkylightWestHYLZ);
        map.put("lightmapBlocklightWestLYHZ", lightmapBlocklightWestLYHZ);
        map.put("lightmapSkylightWestLYHZ", lightmapSkylightWestLYHZ);
        map.put("lightmapBlocklightWestHYHZ", lightmapBlocklightWestHYHZ);
        map.put("lightmapSkylightWestHYHZ", lightmapSkylightWestHYHZ);
        
        map.put("lightmapBlocklightEastLYLZ", lightmapBlocklightEastLYLZ);
        map.put("lightmapSkylightEastLYLZ", lightmapSkylightEastLYLZ);
        map.put("lightmapBlocklightEastHYLZ", lightmapBlocklightEastHYLZ);
        map.put("lightmapSkylightEastHYLZ", lightmapSkylightEastHYLZ);
        map.put("lightmapBlocklightEastLYHZ", lightmapBlocklightEastLYHZ);
        map.put("lightmapSkylightEastLYHZ", lightmapSkylightEastLYHZ);
        map.put("lightmapBlocklightEastHYHZ", lightmapBlocklightEastHYHZ);
        map.put("lightmapSkylightEastHYHZ", lightmapSkylightEastHYHZ);
        
        map.put("lightmapBlocklightTopLYLZ", lightmapBlocklightTopLYLZ);
        map.put("lightmapSkylightTopLYLZ", lightmapSkylightTopLYLZ);
        map.put("lightmapBlocklightTopHYLZ", lightmapBlocklightTopHYLZ);
        map.put("lightmapSkylightTopHYLZ", lightmapSkylightTopHYLZ);
        map.put("lightmapBlocklightTopLYHZ", lightmapBlocklightTopLYHZ);
        map.put("lightmapSkylightTopLYHZ", lightmapSkylightTopLYHZ);
        map.put("lightmapBlocklightTopHYHZ", lightmapBlocklightTopHYHZ);
        map.put("lightmapSkylightTopHYHZ", lightmapSkylightTopHYHZ);
        
        map.put("lightmapBlocklightBottomLYLZ", lightmapBlocklightBottomLYLZ);
        map.put("lightmapSkylightBottomLYLZ", lightmapSkylightBottomLYLZ);
        map.put("lightmapBlocklightBottomHYLZ", lightmapBlocklightBottomHYLZ);
        map.put("lightmapSkylightBottomHYLZ", lightmapSkylightBottomHYLZ);
        map.put("lightmapBlocklightBottomLYHZ", lightmapBlocklightBottomLYHZ);
        map.put("lightmapSkylightBottomLYHZ", lightmapSkylightBottomLYHZ);
        map.put("lightmapBlocklightBottomHYHZ", lightmapBlocklightBottomHYHZ);
        map.put("lightmapSkylightBottomHYHZ", lightmapSkylightBottomHYHZ);
        
        map.put("lightmapBlocklightSouthLYLZ", lightmapBlocklightSouthLYLZ);
        map.put("lightmapSkylightSouthLYLZ", lightmapSkylightSouthLYLZ);
        map.put("lightmapBlocklightSouthHYLZ", lightmapBlocklightSouthHYLZ);
        map.put("lightmapSkylightSouthHYLZ", lightmapSkylightSouthHYLZ);
        map.put("lightmapBlocklightSouthLYHZ", lightmapBlocklightSouthLYHZ);
        map.put("lightmapSkylightSouthLYHZ", lightmapSkylightSouthLYHZ);
        map.put("lightmapBlocklightSouthHYHZ", lightmapBlocklightSouthHYHZ);
        map.put("lightmapSkylightSouthHYHZ", lightmapSkylightSouthHYHZ);
        
        map.put("lightmapBlocklightNorthLYLZ", lightmapBlocklightNorthLYLZ);
        map.put("lightmapSkylightNorthLYLZ", lightmapSkylightNorthLYLZ);
        map.put("lightmapBlocklightNorthHYLZ", lightmapBlocklightNorthHYLZ);
        map.put("lightmapSkylightNorthHYLZ", lightmapSkylightNorthHYLZ);
        map.put("lightmapBlocklightNorthLYHZ", lightmapBlocklightNorthLYHZ);
        map.put("lightmapSkylightNorthLYHZ", lightmapSkylightNorthLYHZ);
        map.put("lightmapBlocklightNorthHYHZ", lightmapBlocklightNorthHYHZ);
        map.put("lightmapSkylightNorthHYHZ", lightmapSkylightNorthHYHZ);
        
        
        return map;
    }
    
    @Override
    public void fromROBNMap(Map<String, Object> map) {
    
    }
}
