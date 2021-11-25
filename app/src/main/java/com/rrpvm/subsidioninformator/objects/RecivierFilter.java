package com.rrpvm.subsidioninformator.objects;

public class RecivierFilter {
    public enum statement {
        WORK(0), CLEAR(42);
        int code;

        statement(int code) {
            this.code = code;
        }
    }

    private filterObj<String> stringFilter;
    private filterObj<Boolean[]> genderFilter;
    private filterObj<String> cityFilter;
    private filterObj<String> oblastFilter;
    private filterObj<Integer> birth_year;
    private filterObj<String> birth_month;
    private filterObj<Integer> birth_day;

    public filterObj<String> getStringFilter() {
        return stringFilter;
    }

    public void setStringFilter(filterObj<String> stringFilter) {
        this.stringFilter = stringFilter;
    }

    public filterObj<String> getCityFilter() {
        return cityFilter;
    }

    public void setCityFilter(filterObj<String> cityFilter) {
        this.cityFilter = cityFilter;
    }

    public filterObj<String> getOblastFilter() {
        return oblastFilter;
    }

    public void setOblastFilter(filterObj<String> oblastFilter) {
        this.oblastFilter = oblastFilter;
    }

    public filterObj<Integer> getBirth_year() {
        return birth_year;
    }

    public void setBirth_year(filterObj<Integer> birth_year) {
        this.birth_year = birth_year;
    }

    public filterObj<String> getBirth_month() {
        return birth_month;
    }

    public void setBirth_month(filterObj<String> birth_month) {
        this.birth_month = birth_month;
    }

    public filterObj<Integer> getBirth_day() {
        return birth_day;
    }

    public filterObj<Boolean[]> getGenderFilter() {
        return genderFilter;
    }

    public void setGenderFilter(filterObj<Boolean[]> genderFilter) {
        this.genderFilter = genderFilter;
    }

    public void setBirth_day(filterObj<Integer> birth_day) {
        this.birth_day = birth_day;
    }

    public RecivierFilter() {
        this.stringFilter = new filterObj<>();
        this.cityFilter = new filterObj<>();
        this.oblastFilter = new filterObj<>();
        this.birth_year = new filterObj<>();
        this.birth_month = new filterObj<>();
        this.birth_day = new filterObj<>();
        this.genderFilter = new filterObj<>();
        this.genderFilter.object = new Boolean[2];
        this.genderFilter.object[0] = true;
        this.genderFilter.object[1] = true;
    }
}