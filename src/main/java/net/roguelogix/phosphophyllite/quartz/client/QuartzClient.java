package net.roguelogix.phosphophyllite.quartz.client;

import net.roguelogix.phosphophyllite.quartz.client.renderer.QuartzRenderer;

public class QuartzClient {
    static void onClientSetup(){
        QuartzRenderer.create();
        QuartzRenderer.INSTANCE.GLSetup();
    }
}
