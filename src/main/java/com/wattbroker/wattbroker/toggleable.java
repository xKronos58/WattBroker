package com.wattbroker.wattbroker;

public interface toggleable {
    void toggle();

    /**
     * @param data Type of data for list * class specific
     * @return Class specific data
     */
    ToggleData getData(dataType data);

    enum dataType {
        NAME("Name"),
        EFFICIENCY("Algorithm Efficiency"),
        HOLDINGS("Algorithm Holdings"),
        PROFIT("Algorithm Profit"),
        RUNNING("Algorithm Status"),
        STATUS("API Status"),
        RESPONSE_TIME("Response Time"),
        LOCATION("Hotspot Location"),
        RANGE("Hotspot Range"),
        PRICE("Hotspot Price");

        final String Name;

        dataType(String Name) {
            this.Name = Name;
        }

        public String getName() {
            return Name;
        }
    }
}


