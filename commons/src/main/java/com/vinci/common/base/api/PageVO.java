package com.vinci.common.base.api;

import java.io.Serializable;

/**
 * Created by tim@vinci on 15-1-27.
 */
public class PageVO<T, F> implements Serializable {

    private static final long serialVersionUID = -7943272693955634096L;
    private T query;

    private Result<F> result;

    public PageVO() {
        super();
    }

    public PageVO(T query, Result<F> result) {
        this.query = query;
        this.result = result;
    }

    public static class Result<F> implements Serializable{

        private int totalCount;
        private F list;

        public Result() {
            super();
        }

        public Result(int totalCount, F list) {
            this.totalCount = totalCount;
            this.list = (F) list;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public F getList() {
            return list;
        }

        public void setList(F list) {
            this.list = list;
        }
    }

    public T getQuery() {
        return query;
    }

    public void setQuery(T query) {
        this.query = query;
    }

    public Result<F> getResult() {
        return result;
    }

    public void setResult(Result<F> result) {
        this.result = result;
    }
}

