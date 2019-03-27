package id.co.outlabs.adhi.arkanfotoclient.getset;

/**
 * Created by adhi on 27/03/19.
 */

public class DataBankController {

    String idbank;
    String namabank;
    String kodebank;

    public DataBankController(){

    }

    public DataBankController(String idbank, String namabank, String kodebank) {
        this.idbank = idbank;
        this.namabank = namabank;
        this.kodebank = kodebank;
    }

    public String getIdbank() {
        return idbank;
    }

    public void setIdbank(String idbank) {
        this.idbank = idbank;
    }

    public String getNamabank() {
        return namabank;
    }

    public void setNamabank(String namabank) {
        this.namabank = namabank;
    }

    public String getKodebank() {
        return kodebank;
    }

    public void setKodebank(String kodebank) {
        this.kodebank = kodebank;
    }
}
