package MyVersion.Frame.Wrappers;

import MyVersion.Core.Network;
import MyVersion.Core.Network_Like;
import MyVersion.Core.SimplifiedNetwork;

public class WrapperChooser {
	public static NetworkWrapperLike getRightWrapper(Network_Like net) {
		if(net instanceof Network) {
			return new NetworkWrapper((Network)net);
		}else if(net instanceof SimplifiedNetwork) {
			return new SimplifiedNetworkWrapper((SimplifiedNetwork)net);
		}
		return null;
	}
}
