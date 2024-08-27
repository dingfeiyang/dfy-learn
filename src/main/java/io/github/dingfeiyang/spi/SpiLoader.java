package io.github.dingfeiyang.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class SpiLoader {
    private static volatile SpiLoader LOADER;
    private SpiInterface spiInterface;
    private final List<SpiInterface> spiInterfaces;

    private SpiLoader() {
        ServiceLoader<SpiInterface> loader = ServiceLoader.load(SpiInterface.class);
        List<SpiInterface> list = new ArrayList<>();
        for (SpiInterface spiInterface : loader) {
            list.add(spiInterface);
        }
        spiInterfaces = list;
        if (!list.isEmpty()) {
           spiInterface = spiInterfaces.get(0);
        } else {
            spiInterface = null;
        }
    }

    public static SpiLoader getLoad() {
        if (LOADER == null) {
            synchronized (SpiLoader.class) {
                if (LOADER == null) {
                    LOADER = new SpiLoader();
                }
            }
        }

        return LOADER;
    }

    public void land() {
        if (spiInterfaces.isEmpty()) {
            System.out.println("service not loading");
        } else {
            SpiInterface spiImplement = spiInterfaces.get(0);
            spiImplement.land();
        }
    }

}
