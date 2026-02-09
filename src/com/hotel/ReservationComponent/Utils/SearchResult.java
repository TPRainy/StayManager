package com.hotel.ReservationComponent.Utils;

import java.util.List;

public class SearchResult<T> {
    private final List<T> data;
    private final String message;
    private final boolean success;

    public SearchResult(List<T> data, String message, boolean success) {
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public List<T> getData() { return data; }
    public String getMessage() { return message; }
    public boolean isSuccess() { return success; }
}
