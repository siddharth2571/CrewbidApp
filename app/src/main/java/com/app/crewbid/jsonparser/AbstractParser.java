package com.app.crewbid.jsonparser;

import com.app.crewbid.interfaces.KeyInterface;
import com.app.crewbid.network.ClsNetworkResponse;

public abstract class AbstractParser implements KeyInterface {
	public ClsNetworkResponse clsResponse;

	public abstract ClsNetworkResponse parse();
}
