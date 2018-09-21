package com.cusbee.kiosk.controller;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Alex Horbatiuk on 01.02.2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageContainer {
    private long totalElements = 0;
    private long totalPages = 0;

    public PageContainer(long totalElements, long totalPages) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public static final class Builder {
        private long totalElements = 0;
        private long totalPages = 0;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder totalElements(long totalElements) {
            this.totalElements = totalElements;
            return this;
        }

        public Builder totalPages(long totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public PageContainer build() {
            return new PageContainer(totalElements, totalPages);
        }
    }
}
