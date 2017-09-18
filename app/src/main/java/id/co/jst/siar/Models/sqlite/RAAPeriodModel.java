package id.co.jst.siar.Models.sqlite;

/**
 * Created by endro.ngujiharto on 9/14/2017.
 */

public class RAAPeriodModel {
    private int irp_id;
    private int irp_period;
    private int irp_year;
    private int irp_month;
    private int irp_status;
    private String irp_generatedate;
    private String irp_inventoryopen;
    private String irp_inventoryclose;

    public RAAPeriodModel()
    {
    }

    public RAAPeriodModel(int irp_id, int irp_period, int irp_year, int irp_month, int irp_status, String irp_generatedate, String irp_inventoryopen, String irp_inventoryclose)
    {
        this.irp_id = irp_id;
        this.irp_period = irp_period;
        this.irp_year = irp_year;
        this.irp_month = irp_month;
        this.irp_status = irp_status;
        this.irp_generatedate = irp_generatedate;
        this.irp_inventoryopen = irp_inventoryopen;
        this.irp_inventoryclose = irp_inventoryclose;
    }

    public void setIRPID(int irp_id) {
        this.irp_id = irp_id;
    }

    public void setIRPPeriod(int irp_period) {
        this.irp_period = irp_period;
    }

    public void setIRPYear(int irp_year) {
        this.irp_year = irp_year;
    }

    public void setIRPMonth(int irp_month) {
        this.irp_month = irp_month;
    }

    public void setIRPStatus(int irp_status) {
        this.irp_status = irp_status;
    }

    public void setIRPGenerateDate(String irp_generatedate) {
        this.irp_generatedate = irp_generatedate;
    }

    public void setIRPInventoryOpen(String irp_inventoryopen) {
        this.irp_inventoryopen = irp_inventoryopen;
    }

    public void setIRPInventoryClose(String irp_inventoryclose) {
        this.irp_inventoryclose = irp_inventoryclose;
    }

    public int getIRPID() {
        return irp_id;
    }

    public int getIRPPeriod() {
        return irp_period;
    }

    public int getIRPYear() {
        return irp_year;
    }

    public int getIRPMonth() {
        return irp_month;
    }

    public int getIRPStatus() {
        return irp_status;
    }

    public String getIRPGenerateDate() {
        return irp_generatedate;
    }

    public String getIRPInventoryOpen() {
        return irp_inventoryopen;
    }

    public String getIRPInventoryClose() {
        return irp_inventoryclose;
    }
}
