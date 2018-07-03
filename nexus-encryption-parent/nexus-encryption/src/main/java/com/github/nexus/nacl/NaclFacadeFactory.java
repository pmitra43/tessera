package com.github.nexus.nacl;

import java.util.ServiceLoader;

public interface NaclFacadeFactory {
    
    NaclFacade create();

    static NaclFacadeFactory newFactory() {
         return ServiceLoader.load(NaclFacadeFactory.class).iterator().next();
    } 
    
}
