package id.co.jst.siar.Models.sqlite;

/**
 * Created by endro.ngujiharto on 4/7/2017.
 */

public class RAAActualModel {
    private int IRPeriodID;
    private int IRAssetNo;
    private int IRModel;
    private String IRMFGNo;
    private int IRLocationID;
    private String IRGenerateDate;
    private String IRGenerateUser;
    private int IRDeptCode;

    public RAAActualModel()
    {
    }

    public RAAActualModel(int IRPeriodID, int IRAssetNo, int IRModel, String IRMFGNo, int IRLocationID, String IRGenerateDate, String IRGenerateUser, int IRDeptCode)
    {
        this.IRPeriodID = IRPeriodID;
        this.IRAssetNo = IRAssetNo;
        this.IRModel = IRModel;
        this.IRMFGNo = IRMFGNo;
        this.IRLocationID = IRLocationID;
        this.IRGenerateDate = IRGenerateDate;
        this.IRGenerateUser = IRGenerateUser;
        this.IRDeptCode = IRDeptCode;
    }

    // SET Method
    public void setIRPeriodID(int IRPeriodID) {
        this.IRPeriodID = IRPeriodID;
    }

    public void setIRAssetNo(int IRAssetNo) {
        this.IRAssetNo = IRAssetNo;
    }

    public void setIRModel(int IRModel) {
        this.IRModel = IRModel;
    }

    public void setIRMFGNo(String IRMFGNo) {
        this.IRMFGNo = IRMFGNo;
    }

    public void setIRLocationID(int IRLocationID) {
        this.IRLocationID = IRLocationID;
    }

    public void setIRGenerateUser(String IRGenerateUser) {
        this.IRGenerateUser = IRGenerateUser;
    }

    public void setIRDeptCode(int IRDeptCode) { this.IRDeptCode = IRDeptCode; }

    // GET Method
    public int getIRPeriodID() { return IRPeriodID; }

    public int getIRAssetNo() {
        return IRAssetNo;
    }

    public int getIRModel() {
        return IRModel;
    }

    public String getIRMFGNo() {
        return IRMFGNo;
    }

    public int getIRLocationID() {
        return IRLocationID;
    }

    public String getIRGenerateUser() { return IRGenerateUser; }

    public int getIRDeptCode() { return IRDeptCode; }
}
