package id.co.jst.siar.Models.sql

/**
 * Created by endro.ngujiharto on 9/13/2017.
 */

class TINV_RAAPeriodModel {
    var irpid: Int = 0
    var irpPeriod: Int = 0
    var irpYear: Int = 0
    var irpMonth: Int = 0
    var irpStatus: Int = 0
    var irpGenerateDate: String? = null
    var irpInventoryOpen: String? = null
    var irpInventoryClose: String? = null

    constructor() {}

    constructor(irp_id: Int, irp_period: Int, irp_year: Int, irp_month: Int, irp_status: Int, irp_generatedate: String, irp_inventoryopen: String, irp_inventoryclose: String) {
        this.irpid = irp_id
        this.irpPeriod = irp_period
        this.irpYear = irp_year
        this.irpMonth = irp_month
        this.irpStatus = irp_status
        this.irpGenerateDate = irp_generatedate
        this.irpInventoryOpen = irp_inventoryopen
        this.irpInventoryClose = irp_inventoryclose
    }
}
