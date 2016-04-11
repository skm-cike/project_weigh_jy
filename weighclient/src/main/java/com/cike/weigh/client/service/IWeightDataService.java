package com.cike.weigh.client.service;

import java.util.List;

import com.cike.weigh.client.vo.Weight;

public interface IWeightDataService {
    List<Weight> getWeightList(String startdate);

    void updateUploadWeighByWater(String s);
}
