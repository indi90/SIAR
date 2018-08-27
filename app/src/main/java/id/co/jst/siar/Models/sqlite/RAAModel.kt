package id.co.jst.siar.Models.sqlite

/**
 * Created by endro.ngujiharto on 4/7/2017.
 */

class RAAModel {
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

    constructor() {}

    constructor(IRPeriodID: Int, IRAssetNo: Int, IRModel: String, IRMFGNo: String, IRLocationID: Int, IRGenerateDate: String, IRGenerateUser: String, IRDeptCode: Int) {
        this.irPeriodID = IRPeriodID
        this.irAssetNo = IRAssetNo
        this.irModel = IRModel
        this.irmfgNo = IRMFGNo
        this.irLocationID = IRLocationID
        this.irGenerateDate = IRGenerateDate
        this.irGenerateUser = IRGenerateUser
        this.irDeptCode = IRDeptCode
    }

}
