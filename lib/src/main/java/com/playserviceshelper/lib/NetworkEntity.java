package com.playserviceshelper.lib;

/**
 * Created by Pierre-Olivier on 03/02/2015.
 */
public abstract class NetworkEntity {
    public abstract String getName();

    public String toString() {
        return "NetworkEntity [name=" + getName() + "]";
    }
}
