package com.jobsite_management_service.abstraction;

import java.util.Map;

//Singleton for volume hierarchy corresponding to a provider
public interface VolumeDiscountHierarchy {
    Integer getLevels();
    //return 1-indexed map of level:quantity
    Map<Integer, Integer> getVolumeMap();

    //return 1-indexed map of level:discount
    Map<Integer, Double> getDiscountMap();
}
