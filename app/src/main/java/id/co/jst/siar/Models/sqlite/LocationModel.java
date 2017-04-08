package id.co.jst.siar.Models.sqlite;

import java.util.Date;

/**
 * Created by endro.ngujiharto on 4/6/2017.
 */

public class LocationModel {
    private int pl_code;
    private String pl_building;
    private String pl_floor;
    private String pl_place;
    private String pl_description;
    private String pl_date;

    public LocationModel()
    {
    }

    public LocationModel(int pl_code, String pl_building, String pl_floor, String pl_place, String pl_description, String pl_date)
    {
        this.pl_code = pl_code;
        this.pl_building = pl_building;
        this.pl_floor = pl_floor;
        this.pl_place = pl_place;
        this.pl_description = pl_description;
        this.pl_date = pl_date;
    }

    public void setPl_code(int pl_code) {
        this.pl_code = pl_code;
    }

    public void setPl_building(String pl_building) {
        this.pl_building = pl_building;
    }

    public void setPl_floor(String pl_floor) {
        this.pl_floor = pl_floor;
    }

    public void setPl_place(String pl_place) {
        this.pl_place = pl_place;
    }

    public void setPl_description(String pl_description) {
        this.pl_description = pl_description;
    }

    public void setPl_date(String pl_date) {
        this.pl_date = pl_date;
    }

    public int getPl_code() {
        return pl_code;
    }

    public String getPl_building() {
        return pl_building;
    }

    public String getPl_floor() {
        return pl_floor;
    }

    public String getPl_place() {
        return pl_place;
    }

    public String getPl_description() {
        return pl_description;
    }

    public String getPl_date() {
        return pl_date;
    }
}
