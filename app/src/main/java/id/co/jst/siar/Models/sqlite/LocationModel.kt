package id.co.jst.siar.Models.sqlite

import java.util.Date

/**
 * Created by endro.ngujiharto on 4/6/2017.
 */

class LocationModel {
    var pl_code: Int = 0
    var pl_building: String? = null
    var pl_floor: String? = null
    var pl_place: String? = null
    var pl_description: String? = null
    var pl_date: String? = null

    constructor() {}

    constructor(pl_code: Int, pl_building: String, pl_floor: String, pl_place: String, pl_description: String, pl_date: String) {
        this.pl_code = pl_code
        this.pl_building = pl_building
        this.pl_floor = pl_floor
        this.pl_place = pl_place
        this.pl_description = pl_description
        this.pl_date = pl_date
    }
}
