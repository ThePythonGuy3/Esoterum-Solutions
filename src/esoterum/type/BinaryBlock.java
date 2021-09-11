package esoterum.type;

import arc.Core;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.util.io.*;
import esoterum.content.EsoVars;
import esoterum.interfaces.*;
import mindustry.core.*;
import mindustry.gen.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.ConstructBlock;
import mindustry.world.meta.*;

public class BinaryBlock extends Block {
    public TextureRegion connectionRegion;
    public TextureRegion topRegion;
    public boolean drawConnection;
    public boolean emits = false;
    public boolean emitAllDirections = false;

    public BinaryBlock(String name){
        super(name);
        solid = true;
        update = true;
        destructible = true;
        drawDisabled = false;
        drawConnection = true;
        buildVisibility = BuildVisibility.sandboxOnly;
        alwaysUnlocked = true;
        category = Category.logic;
        conveyorPlacement = true;
        emitAllDirections = false;
    }

    @Override
    public void load(){
        super.load();
        region = Core.atlas.find("eso-binary-base");
        connectionRegion = Core.atlas.find("eso-connection");
        topRegion = Core.atlas.find(name + "-top");
    }

    @Override
    protected TextureRegion[] icons() {
        return new TextureRegion[]{
            region,
            topRegion,
            Core.atlas.find("eso-full-connections")
        };
    }

    public class BinaryBuild extends Building implements Binaryc {
        public boolean lastSignal;
        public boolean nextSignal;
        // used for overriding signals, will come in useful for junctions and other stuff
        public boolean signalOverride;
        // used for drawing
        public BinaryBuild[] nb = new BinaryBuild[]{null, null, null, null};

        @Override
        // this hurts me
        public void draw(){
            Draw.rect(region, x, y);
            Draw.color(Color.white, EsoVars.connectionColor, lastSignal ? 1f : 0f);
            if(drawConnection) for (BinaryBuild b : nb) {
                if(b == null || b.team != team) continue;
                if(!b.block.rotate || (b.front() == this || b.back() == this) || front() == b){
                    if(!(b.back() == this && front() != b) || !b.block.rotate){
                        Draw.rect(connectionRegion, x, y, relativeTo(b) * 90);
                    }
                }
            }
            Draw.rect(topRegion, x, y, rotdeg());
        }

        public boolean getSignal(BinaryBuild b){
            return getSignal(this, b);
        }

        public boolean emits(){
            return emits;
        }

        public boolean emitAllDirections(){
            return emitAllDirections;
        }

        @Override
        public void onProximityUpdate() {
            super.onProximityUpdate();
            nb[0] = back() == null ? null : back() instanceof ConstructBlock.ConstructBuild ? null : (BinaryBuild) back();
            nb[1] = left() == null ? null : left() instanceof ConstructBlock.ConstructBuild ? null : (BinaryBuild) left();
            nb[2] = right() == null ? null : right() instanceof ConstructBlock.ConstructBuild ? null : (BinaryBuild) right();
            nb[3] = front() == null ? null : front() instanceof ConstructBlock.ConstructBuild ? null : (BinaryBuild) front();
        }

        @Override
        public byte version(){
            return 1;
        }

        @Override
        public void read(Reads read, byte revision){
            if(revision == 1){
                lastSignal = nextSignal = read.bool();
            }
        }

        @Override
        public void write(Writes write){
            write.bool(nextSignal);
        }

        @Override
        public double sense(LAccess sensor) {
            return switch(sensor){
                case x -> World.conv(x);
                case y -> World.conv(y);
                case dead -> !isValid() ? 1 : 0;
                case team -> team.id;
                case health -> health;
                case maxHealth -> maxHealth;
                case rotation -> rotation;
                case enabled -> lastSignal ? 1 : 0;
                case size -> block.size;
                default -> Float.NaN; //gets converted to null in logic
            };
        }
    }
}
