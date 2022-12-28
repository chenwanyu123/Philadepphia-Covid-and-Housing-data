package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.PropertyData;

public class MktSelector implements PropertySelector {
    @Override
    public Double getField(PropertyData data) {
        return data.getMarketValue();
    }
}
