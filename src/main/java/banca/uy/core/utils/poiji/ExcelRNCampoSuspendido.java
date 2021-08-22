package banca.uy.core.utils.poiji;

import com.poiji.annotation.ExcelCellName;

public class ExcelRNCampoSuspendido {

    @ExcelCellName("Desde")
    private String suspendidoDesde = "";
    @ExcelCellName("Hasta")
    private String suspendidoHasta = "";

    public ExcelRNCampoSuspendido() {
    }

    public ExcelRNCampoSuspendido(String suspendidoDesde, String suspendidoHasta) {
        this.suspendidoDesde = suspendidoDesde;
        this.suspendidoHasta = suspendidoHasta;
    }

    public String getSuspendidoDesde() {
        return this.suspendidoDesde;
    }

    public void setSuspendidoDesde(String suspendidoDesde) {
        this.suspendidoDesde = suspendidoDesde;
    }

    public String getSuspendidoHasta() {
        return this.suspendidoHasta;
    }

    public void setSuspendidoHasta(String suspendidoHasta) {
        this.suspendidoHasta = suspendidoHasta;
    }

    public ExcelRNCampoSuspendido suspendidoDesde(String suspendidoDesde) {
        this.suspendidoDesde = suspendidoDesde;
        return this;
    }

    public ExcelRNCampoSuspendido suspendidoHasta(String suspendidoHasta) {
        this.suspendidoHasta = suspendidoHasta;
        return this;
    }

    @Override
    public String toString() {
        return "{" + " suspendidoDesde='" + getSuspendidoDesde() + "'" + ", suspendidoHasta='" + getSuspendidoHasta()
                + "'" + "}";
    }

}