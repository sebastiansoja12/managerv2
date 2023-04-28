package com.warehouse.tsp.domain.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomElementsSwapperImpl implements RandomElementsSwapper {

    @Override
    public List<Integer> swapRandomElements(List<Integer> route) {
        final List<Integer> newRoute = new ArrayList<>(route);
        final int index1 = (int) (Math.random() * newRoute.size());
        final int index2 = (int) (Math.random() * newRoute.size());
        Collections.swap(newRoute, index1, index2);
        return newRoute;
    }
}
