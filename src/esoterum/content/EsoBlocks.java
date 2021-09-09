package esoterum.content;

import esoterum.type.*;
import mindustry.ctype.ContentList;
import mindustry.world.*;
import mindustry.world.meta.*;

public class EsoBlocks implements ContentList {
    public static Block
        esoBlock, esoButton, esoWire, esoAnd, esoNot;

    public void load(){
        esoBlock = new BinaryBlock("test-binary-block"){{
            buildVisibility = BuildVisibility.hidden;
        }};

        esoWire = new BinaryAcceptor("binary-wire");

        esoButton = new BinarySwitch("binary-switch");

        // LOGIC GATES
        // Skipping OR because they're basically just two wires side by side
        esoAnd = new BinaryGate("binary-AND"){{
            inputs = new boolean[]{true, false, true};
        }};

        esoNot = new BinaryGate("binary-NOT"){
            {
                inputs = new boolean[]{false, true, false};
            }

            @Override
            public boolean operation(boolean[] in) {
                return !in[1];
            }
        };
    }
}