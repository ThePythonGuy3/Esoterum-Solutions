package esoterum.type;

import arc.Core;
import arc.graphics.g2d.Draw;

public class BinaryRouter extends BinaryAcceptor{
    public BinaryRouter(String name){
        super(name);
        rotate = false;
        emits = true;
        drawConnection = false;
        connectionRegion = Core.atlas.find("eso-full-connections");
    }

    public class BinaryRouterBuild extends BinaryAcceptorBuild {
        @Override
        public void draw() {
            super.draw();
            Draw.rect(connectionRegion, x, y);
        }

        @Override
        public boolean signal() {
            return super.signal() | sFront();
        }
    }
}
