package id.co.jst.siar.Models.sqlite

/**
 * Created by endro.ngujiharto on 4/7/2017.
 */

class RAAActualModel {
    // GET Method
    // SET Method
    var irPeriodID: Int = 0
    var irAssetNo: Int = 0
    var irModel: String? = null
    var irmfgNo: String? = null
    var irLocationID: Int = 0
    var irGenerateDate: String? = null
    var irGenerateUser: String? = null
    var irDeptCode: Int = 0
    var irpic: String? = null
    var irScanDate: String? = null

    constructor() {}

    constructor(IRPeriodID: Int, IRAssetNo: Int, IRModel: String, IRMFGNo: String, IRLocationID: Int, IRGenerateDate: String, IRGenerateUser: String, IRDeptCode: Int, IRPIC: String, IRScanDate: String) {
        this.irPeriodID = IRPeriodID
        this.irAssetNo = IRAssetNo
        this.irModel = IRModel
        this.irmfgNo = IRMFGNo
        this.irLocationID = IRLocationID
        this.irGenerateDate = IRGenerateDate
        this.irGenerateUser = IRGenerateUser
        this.irDeptCode = IRDeptCode
        this.irpic = IRPIC
        this.irScanDate = IRScanDate
    }
}
