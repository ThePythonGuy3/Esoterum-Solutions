package esoterum.type;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import mindustry.graphics.*;

public class BinaryLed extends BinaryAcceptor {
    public boolean[] inputs = {true, true, true};
    public BinaryLed(String name){
        super(name);
        rotate = true;
        emits = false;
        drawArrow = false;
    }

    @Override
    protected TextureRegion[] icons() {
        return new TextureRegion[]{
            region,
            topRegion,
            Core.atlas.find("eso-gate-connections")
        };
    }

    public class BinaryLedBuild extends BinaryAcceptorBuild {

        @Override
        public void updateTile(){
            super.updateTile();
            lastSignal = nextSignal;
            nextSignal = signal();
            signalOverride = false;
            // sBack() | sLeft() | sRight()
        }

        @Override
        public boolean signal(){
            return false;
        }

        @Override
        public void draw(){
            Draw.rect(region, x, y);
            for(int i = 0; i < 3; i++){
                if(!inputs[i])continue;
                new Color(0, 0, 0, 1);
                Color inputColor = switch (i) {
                    case 0 -> new Color(sLeft() ? 1 : 0, 0, 0);
                    case 1 -> new Color(0, sBack() ? 1 : 0, 0);
                    case 2 -> new Color(0, 0, sRight() ? 1 : 0);
                    default -> new Color(1, 1, 1);
                };
                Draw.color(inputColor);
                Draw.rect(connectionRegion, x, y, (90f + 90f * i) + rotdeg());
            }
            Draw.color(getColor());
            Draw.rect(topRegion, x, y, rotdeg());
        }

        public Color getColor(){
            int r = sLeft() ? 1 : 0;
            int g = sBack() ? 1 : 0;
            int b = sRight() ? 1 : 0;
            return new Color(r, g, b, 1);
        }

    }
}
