package MyVersion.NEAT;

import java.io.Serializable;

/**
 * Created by vishnughosh on 04/03/17.
 */
public class InnovationCounter implements Serializable {

    private static int innovation = 0;

    public static int newInnovation() {
        innovation++;
        return innovation;
    }
}
