package org.cnodejs.android.md.model.entity;

public class DataResult<Data> extends Result {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
