package com.humanlegion.integrationtests;

import com.humanlegion.Bob;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BobIT {
    @Test
    public void testBobConstructor() {
        Bob bob1 = new Bob();
        Bob bob2 = new Bob();
        assertEquals(bob1.canWeBuildIt(), bob2.canWeBuildIt());
    }

    @Test
    public void testBobMessage() {
        Bob bob = new Bob();
        assertEquals("Err, yes, I think so, Bob.", bob.canWeBuildIt());
    }

}
