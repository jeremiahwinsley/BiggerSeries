package net.roguelogix.phosphophyllite.quartz.client;

import net.roguelogix.phosphophyllite.Phosphophyllite;
import net.roguelogix.phosphophyllite.config.ConfigType;
import net.roguelogix.phosphophyllite.config.PhosphophylliteConfig;

@PhosphophylliteConfig(
        folder = Phosphophyllite.modid,
        name = "quartz",
        type = ConfigType.CLIENT
)
public class QuartzClientConfig {
    
    public enum OperationMode{
        GL21_JAVA
    }
    
    @PhosphophylliteConfig.Value
    public static OperationMode OPERATION_MODE = OperationMode.GL21_JAVA;
}

